package com.verifalia.api.rest;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.net.URI;

/**
 * Represents REST request
 */
@Getter
@Setter
public class RestRequest {

    /**
     * Base URI
     */
    private URI baseURI;

    /**
     * HTTP request method
     */
    private HttpRequestMethod method;

    /**
     * Request target resource
     */
    private String resource;

    /**
     * HTTP request data
     */
    private String data;

    /**
     * Constructs new object for a given resource with given method
     */
    public RestRequest(@NonNull final HttpRequestMethod method, @NonNull final String resource) {
        this(method, resource, null);
    }

    /**
     * Constructs new object for a given resource with given method and given data
     */
    public RestRequest(@NonNull final HttpRequestMethod method, @NonNull final String resource, final Object data) {
        this.method = method;
        this.resource = resource;

        if (data != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
                this.data = mapper.writeValueAsString(data);
            } catch (IOException exception) {
                throw new IllegalArgumentException("Cannot convert the payload into a JSON string.", exception);
            }
        }
    }
}