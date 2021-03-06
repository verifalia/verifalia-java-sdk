/*
 * Verifalia - Email list cleaning and real-time email verification service
 * https://verifalia.com/
 * support@verifalia.com
 *
 * Copyright (c) 2005-2020 Cobisi Research
 *
 * Cobisi Research
 * Via Prima Strada, 35
 * 35129, Padova
 * Italy - European Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.verifalia.api.rest;

import com.verifalia.api.exceptions.EndpointServerErrorException;
import com.verifalia.api.exceptions.ServiceUnreachableException;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.security.AuthenticationProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

/***
 * Represents the internal REST client used by the SDK.
 */
public class RestClient {
    private final List<URI> baseURIs;
    private final String apiVersion;
    private final String userAgent;
    private final AuthenticationProvider defaultAuthenticationProvider;
    private int currentBaseURIIndex;

    public RestClient(@NonNull final AuthenticationProvider defaultAuthenticationProvider, @NonNull final List<URI> baseURIs, @NonNull final String apiVersion) {
        this.baseURIs = baseURIs;
        this.apiVersion = apiVersion;
        this.userAgent = getUserAgent();
        this.defaultAuthenticationProvider = defaultAuthenticationProvider;
    }

    public RestResponse execute(@NonNull final RestRequest request) throws VerifaliaException {
        return execute(request, this.defaultAuthenticationProvider);
    }

    public RestResponse execute(@NonNull final RestRequest request, @NonNull final AuthenticationProvider authenticationProvider)
            throws VerifaliaException {
        @Getter
        @Setter
        class EndpointServerError {
            URI baseUri;
            Exception exception;

            EndpointServerError(@NonNull final URI baseUri, @NonNull final Exception exception) {
                this.setBaseUri(baseUri);
                this.setException(exception);
            }
        }

        ArrayList<EndpointServerError> errors = new ArrayList<>();

        if (nonNull(this.baseURIs)) {
            for (int idxAttempt = 0; idxAttempt < this.baseURIs.size(); idxAttempt++) {
                CloseableHttpResponse response;
                URI baseURI = this.baseURIs.get(currentBaseURIIndex++ % this.baseURIs.size());

                try {
                    response = sendRequest(baseURI, request, authenticationProvider);
                } catch (IOException e) {
                    // Continue with the next attempt on IO exceptions, if needed
                    errors.add(new EndpointServerError(baseURI, e));
                    continue;
                }

                if (nonNull(response)) {
                    int statusCode = response.getStatusLine().getStatusCode();

                    // Automatically retry with another host on HTTP 5xx status codes

                    if (statusCode >= 500 && statusCode <= 599) {
                        errors.add(new EndpointServerError(baseURI, new EndpointServerErrorException(String.format("The API endpoint %s returned a server error HTTP status code %d.", baseURI, statusCode))));
                        continue;
                    }

                    return new RestResponse(statusCode, response.getEntity());
                }
            }
        }

        // Aggregate exception

        StringBuilder sbAggregateError = new StringBuilder("All the base URIs are unreachable: ");
        errors.forEach(e -> sbAggregateError.append(e.baseUri).append(" => ").append(e.exception.getMessage()).append(" "));

        throw new ServiceUnreachableException(sbAggregateError.toString());
    }

    private CloseableHttpResponse sendRequest(@NonNull final URI baseURI, @NonNull final RestRequest restRequest, @NonNull final AuthenticationProvider authenticationProviderOverride)
            throws VerifaliaException, IOException {

        // Determine the intermediate URI, including the API version, for this invocation

        StringBuilder sbApiVersionURI = new StringBuilder();
        sbApiVersionURI.append(baseURI.toString()).append("/").append(apiVersion).append("/");

        URI apiVersionURI;

        try {
            apiVersionURI = new URI(sbApiVersionURI.toString());
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI " + sbApiVersionURI);
        }

        // Build the HTTP client and the HTTP request out of the provided RestRequest

        CloseableHttpClient client = authenticationProviderOverride.buildClient(this);
        HttpRequestBase request = restRequest.buildHttpRequest(apiVersionURI);

        // Common headers and authentication handling

        request.setHeader(HttpHeaders.USER_AGENT, this.userAgent);
        authenticationProviderOverride.decorateRequest(this, request);

        return client.execute(request);
    }

    private String getUserAgent() {
        StringBuilder sbUserAgent = new StringBuilder("verifalia-rest-client/java");

        // Java version

        sbUserAgent.append(System.getProperty("java.version"));

        // Package version

        String packageVersion = getClass().getPackage().getImplementationVersion();

        if (packageVersion != null) {
            sbUserAgent.append("/");
            sbUserAgent.append(packageVersion);
        }

        return sbUserAgent.toString();
    }
}
