package com.verifalia.api.rest;

import com.verifalia.api.baseURIProviders.BaseURIProvider;
import com.verifalia.api.common.Utils;
import com.verifalia.api.exceptions.EndpointServerErrorException;
import com.verifalia.api.exceptions.ServiceUnreachableException;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.security.AuthenticationProvider;
import lombok.AccessLevel;
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
 * Represents REST client
 */
//@Getter
//@Setter
public class RestClient {
    private static final String RESPONSE_ACCEPT_TYPE_ENCODING = "gzip";

    /**
     * Base URI List
     */
    private List<URI> baseURIs;

    /**
     * API version
     */
    private String apiVersion;

    /**
     * User agent string
     */
    private String userAgent;

    /**
     * Authentication provider for the rest client.
     */
    private AuthenticationProvider authenticationProvider;

    private int currentBaseURIIndex;

    /**
     * Creates new object using given with default values
     */
    public RestClient(AuthenticationProvider authenticationProvider, List<URI> baseURIs, String apiVersion) {
        this.baseURIs = baseURIs;
        this.apiVersion = apiVersion;
        this.userAgent = getUserAgent();
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Executes given request
     *
     * @param request The request
     * @return RestResponse response object
     * @throws IOException
     */
    public RestResponse execute(RestRequest request) throws VerifaliaException {
        return execute(request, this.authenticationProvider);
    }

    /**
     * Executes given request
     *
     * @param request             The request
     * @param responseObjectClass Java class of the expected response
     * @return RestResponse response object
     * @throws IOException
     */
    public RestResponse execute(RestRequest request, AuthenticationProvider authenticationProviderOverride)
            throws VerifaliaException {
        @Getter
        @Setter
        class EndpointServerError {
            URI baseUri;
            Exception exception;

            EndpointServerError(@NonNull URI baseUri, @NonNull Exception exception) {
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
                    response = sendRequest(baseURI, request, authenticationProviderOverride);
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

    private CloseableHttpResponse sendRequest(URI baseURI, RestRequest restRequest, AuthenticationProvider authenticationProviderOverride)
            throws VerifaliaException, IOException {

        StringBuilder sb = new StringBuilder();
        sb.append(baseURI.toString()).append("/").append(apiVersion).append("/").append(restRequest.getResource());

        URI uri;

        try {
            uri = new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI " + sb);
        }

        CloseableHttpClient client = authenticationProviderOverride.buildClient(this);
        HttpRequestBase request;

        switch (restRequest.getMethod()) {
            case GET: {
                request = new HttpGet(uri);
                break;
            }

            case POST: {
                request = new HttpPost(uri);
                StringEntity entity = new StringEntity(restRequest.getData());
                ((HttpPost) request).setEntity(entity);
                break;
            }

            case DELETE: {
                request = new HttpDelete(uri);
                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported method " + restRequest.getMethod());
        }

        // Common headers and authentication handling

        request.setHeader(HttpHeaders.USER_AGENT, this.userAgent);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

        authenticationProviderOverride.decorateRequest(this, request);

        return client.execute(request);
    }

//    private boolean isAuthRequired(String requestResource) {
//        if (StringUtils.equalsIgnoreCase(Constants.AUTH_TOKEN_RESOURCE, requestResource)) {
//            return false;
//        }
//        return true;
//    }

//	private boolean isAuthBasicOrBearer(){
//		if(StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BASIC) ||
//				StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BEARER)){
//			return true;
//		}
//		return false;
//	}
//
//	private String getAuthString() throws IOException{
//		if(isAuthBasicOrBearer()){
//			if(StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BASIC)){
//				return this.basicAuth.getAuthString();
//			} else if(StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BEARER)){
//				return this.bearerAuth.getAuthString();
//			} else {
//				return StringUtils.EMPTY;
//			}
//		}
//		return StringUtils.EMPTY;
//	}

//    private List<URI> convertStringBaseURIToURIList(List<String> strBaseURIList) throws URISyntaxException {
//        List<URI> uriList = new ArrayList<URI>();
//        for (String strBaseURI : strBaseURIList) {
//            uriList.add(new URI(strBaseURI));
//        }
//        return uriList;
//    }
//
//    private List<URI> convertStringBaseURIToURIList(String strBaseURI) throws URISyntaxException {
//        List<URI> uriList = new ArrayList<URI>();
//        uriList.add(new URI(strBaseURI));
//        return uriList;
//    }

//    private String processHttpResponseEntity(CloseableHttpResponse response) throws ParseException, IOException {
//        String result = StringUtils.EMPTY;
//        if (nonNull(response)) {
//            HttpEntity entity = response.getEntity();
//            if (nonNull(entity)) {
//                result = EntityUtils.toString(entity);
//            }
//        }
//        return result;
//    }

//    private boolean isHttpRetryNeeded(int responseCode, int currentIterValue, int totalBaseUriSize) {
//        boolean isHttpRetryNeeded = false;
//
//        // Automatically retry with another host on HTTP 5xx status codes
//
//        if (responseCode >= 500 && responseCode <= 599) {
//            if (currentIterValue != totalBaseUriSize - 1) { // Check to see if any base URI is there for processing
//                isHttpRetryNeeded = true;
//            }
//        }
//        return isHttpRetryNeeded;
//    }

    private String getUserAgent() {
        return "verifalia-rest-client/java/" + getClass().getPackage().getImplementationVersion();
    }
}
