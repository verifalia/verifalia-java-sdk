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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.net.URI;

/**
 * Represents a REST request.
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