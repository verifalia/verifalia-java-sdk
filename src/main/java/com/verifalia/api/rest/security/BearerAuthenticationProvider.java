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

package com.verifalia.api.rest.security;

import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * Allows to authenticate a REST client against the Verifalia API using bearer authentication.
 */
@Getter
@Setter
public class BearerAuthenticationProvider extends AuthenticationProvider {
    private String username;
    private String password;
    private String accessToken;

    /**
     * Initializes a new bearer authentication provider for the Verifalia API, with the specified username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public BearerAuthenticationProvider(@NonNull final String username, @NonNull final String password) {
        this.username = username;
        this.password = password;
    }

    private static String getBearerToken(RestClient client, String username, String password) throws VerifaliaException {
        // Build input parameters
        Credentials credentials = new Credentials();

        credentials.setUsername(username);
        credentials.setPassword(password);

        // Make rest request

        RestRequest request = new RestRequest(HttpRequestMethod.POST,
                "auth/tokens",
                new StringEntity(RestRequest.serializeToJson(credentials), "UTF-8"));

        // Sends the request to the Verifalia servers
        RestResponse response = client.execute(request, new AuthenticationProvider() { });
        return response.deserialize(JSONObject.class).getString("accessToken");
    }

    private String getAuthString(RestClient client) throws VerifaliaException {
        if (accessToken == null) {
            accessToken = getBearerToken(client, username, password);
        }

        return "Bearer " + accessToken;
    }

    @Override
    public void decorateRequest(RestClient client, HttpRequestBase request) throws VerifaliaException {
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthString(client));
    }

    @Getter
    @Setter
    private static class Credentials {
        private String username;
        private String password;
    }
}
