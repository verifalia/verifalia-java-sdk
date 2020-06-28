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

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Represents a REST request.
 */
@Getter
@Setter
public class RestRequest {
    /**
     * HTTP request data, if any.
     */
    private HttpEntity httpEntity;

    /**
     * HTTP request method.
     */
    @NonNull
    private HttpRequestMethod method;

    /**
     * Request target resource.
     */
    @NonNull
    private String resource;

    public static String serializeToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            return mapper.writeValueAsString(data);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot convert the payload into a JSON string.", exception);
        }
    }

    public RestRequest(@NonNull final HttpRequestMethod method, @NonNull final String resource) {
        this.setMethod(method);
        this.setResource(resource);
    }

    public RestRequest(@NonNull final HttpRequestMethod method, @NonNull final String resource, @NonNull final HttpEntity httpEntity) {
        this(method, resource);
        this.setHttpEntity(httpEntity);
    }

    public HttpRequestBase buildHttpRequest(@NonNull URI apiVersionURI) throws IOException {
        HttpRequestBase request;

        // Determine the final URI for this request

        URI requestURI = buildRequestURI(apiVersionURI);

        // Build the actual HTTP request

        switch (getMethod()) {
            case GET: {
                request = new HttpGet(requestURI);
                break;
            }

            case POST: {
                request = new HttpPost(requestURI);
                ((HttpPost) request).setEntity(httpEntity);

                if (httpEntity != null) {
                    // HACK: Since MultipartFormEntity is not public (WTF!), we will assume that all entities are multi-parts
                    // *unless* they are instances of a specific defined type.

                    if (httpEntity instanceof StringEntity) {
                        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    }
                    else {
                        request.setHeader(HttpHeaders.CONTENT_TYPE, httpEntity.getContentType().getValue());
                    }
                }

                break;
            }

            case DELETE: {
                request = new HttpDelete(requestURI);
                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported method " + getMethod());
        }

        // Common headers

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

        return request;
    }

    protected URI buildRequestURI(URI apiVersionURI) throws IOException {
        // Determine the final URI for this request

        URI requestURI;
        String requestURICandidate = apiVersionURI.toString() + getResource();

        try {
            requestURI = new URI(requestURICandidate);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI " + requestURICandidate);
        }

        return requestURI;
    }
}