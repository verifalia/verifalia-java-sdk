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

import com.verifalia.api.rest.RestClient;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpRequestBase;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class BasicAuthentication extends AuthenticationProvider {

    /**
     * Username used to authenticate to Verifalia
     */
    private String username;

    /**
     * Password used to authenticate to Verifalia
     */
    private String password;

    /**
     * Constructs an object for basic authentication to authenticate API client
     *
     * @param username The username used to authenticate to Verifalia
     * @param password The password used to authenticate to the Verifalia
     * @throws IllegalArgumentException
     */
    public BasicAuthentication(@NonNull final String username, @NonNull final String password) {
        this.username = username;
        this.password = password;
    }

    private String getAuthString() throws IllegalArgumentException {
        String authString = StringUtils.EMPTY;
        byte[] authBytes = (username + ':' + password).getBytes(StandardCharsets.UTF_8);
        authString = "Basic " + new String(Base64.encodeBase64(authBytes));
        return authString;
    }

    @Override
    public void decorateRequest(RestClient client, HttpRequestBase request) {
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthString());
    }
}
