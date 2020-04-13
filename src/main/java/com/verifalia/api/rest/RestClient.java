package com.verifalia.api.rest;

import com.verifalia.api.baseURIProviders.BaseURIProvider;
import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import com.verifalia.api.exceptions.EndpointServerErrorException;
import com.verifalia.api.exceptions.ServiceUnreachableException;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.security.AuthenticationProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

/***
 * Represents REST client
 */
@Getter
@Setter
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

    /**
     * Creates new object using given with default values
     */
    public RestClient(AuthenticationProvider authenticationProvider, BaseURIProvider baseURIProvider) {
        this.baseURIs = baseURIProvider.provideBaseURIs();
        this.apiVersion = Constants.DEFAULT_API_VERSION;
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
    public RestResponse execute(RestRequest request) throws IOException {
        return execute(request, null);
    }

    public RestResponse execute(RestRequest request, Class<?> responseObjectClass) throws IOException {
        return execute(request, responseObjectClass, this.authenticationProvider);
    }

    /**
     * Executes given request
     *
     * @param request             The request
     * @param responseObjectClass Java class of the expected response
     * @return RestResponse response object
     * @throws IOException
     */
    public RestResponse execute(RestRequest request, Class<?> responseObjectClass, AuthenticationProvider authenticationProviderOverride) throws IOException {
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

        if (nonNull(this.baseURIs) && this.baseURIs.size() > 0) {
            int index = -1;
            int baseURIsSize = this.baseURIs.size();

            for (int i = 0; i < baseURIsSize; i++) {
                if (index == -1) {
                    index = Utils.getRandomNumberInRange(baseURIsSize);
                } else {
                    index = (index + 1) % baseURIsSize;
                }

                URI baseURI = baseURIs.get(index);
                CloseableHttpResponse response;

                try {
                    response = sendRequest(baseURI, request, authenticationProviderOverride);
                } catch (VerifaliaException e) {
                    throw e;
                } catch (IOException e) {
                    // Continue with the next attempt on IO exceptions, if needed

                    errors.add(new EndpointServerError(baseURI, e));
                    continue;
                }

                if (nonNull(response)) {
                    int responseCode = response.getStatusLine().getStatusCode();

                    // Automatically retry with another host on HTTP 5xx status codes

                    if (responseCode >= 500 && responseCode <= 599) {
                        errors.add(new EndpointServerError(baseURI, new EndpointServerErrorException(String.format("The API endpoint %s returned a server error HTTP status code %d.", baseURI, responseCode))));
                        continue;
                    }

                    String responseBody = StringUtils.EMPTY;

                    if (nonNull(response)) {
                        HttpEntity entity = response.getEntity();
                        if (nonNull(entity)) {
                            responseBody = EntityUtils.toString(entity);
                        }
                    }

                    return new RestResponse(responseCode,
                            responseBody,
                            responseObjectClass);
                }
            }
        }

        // Aggregate exception

        StringBuilder sbAggregateError = new StringBuilder("All the base URIs are unreachable: ");
        errors.forEach(e -> sbAggregateError.append(e.baseUri).append(" => ").append(e.exception.getMessage()).append(" "));

        throw new ServiceUnreachableException(sbAggregateError.toString());
    }

    private CloseableHttpResponse sendRequest(URI baseURI, RestRequest restRequest, AuthenticationProvider authenticationProviderOverride)
            throws IOException {

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
