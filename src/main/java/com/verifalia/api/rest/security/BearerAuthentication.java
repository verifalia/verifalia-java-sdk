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

@Getter
@Setter
public class BearerAuthentication extends AuthenticationProvider {
    /**
     * Account ID
     */
    private String username;
    /**
     * Account token
     */
    private String password;
    /**
     * Authentication string
     */
    private String accessToken;

    /**
     * Constructs an object for bearer authentication to authenticate API client
     *
     * @param username Username used to authenticate to Verifalia
     * @param password Password used to authenticate to Verifalia
     */
    public BearerAuthentication(@NonNull final String username, @NonNull final String password) {
        this.username = username;
        this.password = password;
    }

    private static String getBearerToken(RestClient client, String username, String password) throws VerifaliaException {
        // Build input parameters
        Credentials credentials = new Credentials();

        credentials.setUsername(username);
        credentials.setPassword(password);

        // Make rest request
        RestRequest request = new RestRequest(HttpRequestMethod.POST, "auth/tokens", credentials);

        // Sends the request to the Verifalia servers
        RestResponse response = client.execute(request, new NoAuthenticationProvider());
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
