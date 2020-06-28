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

package com.verifalia.api;

import com.verifalia.api.baseURIProviders.BaseURIProvider;
import com.verifalia.api.baseURIProviders.ClientCertificateBaseURIProvider;
import com.verifalia.api.baseURIProviders.DefaultBaseURIProvider;
import com.verifalia.api.credits.CreditsRestClient;
import com.verifalia.api.emailvalidations.EmailValidationsRestClient;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.security.AuthenticationProvider;
import com.verifalia.api.rest.security.UsernamePasswordAuthenticationProvider;
import com.verifalia.api.rest.security.ClientCertificateAuthenticationProvider;
import lombok.NonNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

/**
 * HTTPS-based REST client for Verifalia.
 */
public class VerifaliaRestClient {
    /**
     * The default API version supported by the SDK.
     */
    public static final String DEFAULT_API_VERSION = "v2.2";

    /**
     * Cached REST client object.
     */
    RestClient cachedRestClient;

    /**
     * Email validation service client object.
     */
    EmailValidationsRestClient emailValidations;

    /**
     * Credits service client object.
     */
    CreditsRestClient credits;

    /**
     * Initializes a new HTTPS-based REST client for Verifalia with the specified username and password. While
     * authenticating with your Verifalia main account credentials is possible, it is strongly advised to create one
     * or more users (formerly known as sub-accounts) with just the required permissions, for improved security.
     * To create a new user or manage existing ones, please visit https://verifalia.com/client-area#/users
     * @param username The username to authenticate with.
     * @param password The password to authenticate with.
     */
    public VerifaliaRestClient(@NonNull final String username, @NonNull final String password) {
        this(new UsernamePasswordAuthenticationProvider(username, password), new DefaultBaseURIProvider());
    }

    /**
     * Initializes a new HTTPS-based REST client for Verifalia with the specified client certificate (for enterprise-grade
     * mutual TLS authentication). TLS client certificate authentication is available to premium plans only.
     * To create a new user or manage existing ones, please visit https://verifalia.com/client-area#/users
     * @param certAlias
     * @param certPassword
     * @param identityStoreJksFile
     * @param trustKeyStoreJksFile
     */
    public VerifaliaRestClient(String certAlias, String certPassword, File identityStoreJksFile, File trustKeyStoreJksFile) {
        this(new ClientCertificateAuthenticationProvider(certAlias, certPassword, identityStoreJksFile, trustKeyStoreJksFile), new ClientCertificateBaseURIProvider());
    }

    /**
     * Initializes a new HTTPS-based REST client for Verifalia with the specified {@link AuthenticationProvider}.
     * @param authenticationProvider The {@link AuthenticationProvider} which authenticates to the Verifalia API.
     */
    public VerifaliaRestClient(@NonNull final AuthenticationProvider authenticationProvider) {
        this(authenticationProvider, new DefaultBaseURIProvider(), DEFAULT_API_VERSION);
    }

    /**
     * Initializes a new HTTPS-based REST client for Verifalia with the specified {@link AuthenticationProvider}, using the
     * specified API version. Warning: using an unsupported API version here may affect the stability of the SDK itself.
     * @param authenticationProvider The {@link AuthenticationProvider} which authenticates to the Verifalia API.
     * @param apiVersion The specific API version to use while connecting to the Verifalia API. Warning: this value may
     *                  affect the stability of the SDK itself. If unsure, please just use {@link #VerifaliaRestClient(AuthenticationProvider)}.
     */
    public VerifaliaRestClient(@NonNull final AuthenticationProvider authenticationProvider, @NonNull final String apiVersion) {
        this(authenticationProvider, new DefaultBaseURIProvider(), apiVersion);
    }

    /**
     * Initializes a new HTTPS-based REST client for Verifalia with the specified {@link AuthenticationProvider}, using the
     * specified {@link BaseURIProvider base URIs provider}.
     * @param authenticationProvider The {@link AuthenticationProvider} which authenticates to the Verifalia API.
     * @param baseURIProvider A {@link BaseURIProvider} instance which overrides the default values provided by the SDK.
     */
    public VerifaliaRestClient(@NonNull final AuthenticationProvider authenticationProvider, @NonNull final BaseURIProvider baseURIProvider) {
        this(authenticationProvider, baseURIProvider, DEFAULT_API_VERSION);
    }

    /**
     * Initializes a new HTTPS-based REST client for Verifalia with the specified {@link AuthenticationProvider}, using the
     * specified {@link BaseURIProvider base URIs provider} and API version.
     * @param authenticationProvider The {@link AuthenticationProvider} which authenticates to the Verifalia API.
     * @param baseURIProvider A {@link BaseURIProvider} instance which overrides the default values provided by the SDK.
     * @param apiVersion The specific API version to use while connecting to the Verifalia API. Warning: this value may
     *                  affect the stability of the SDK itself. If unsure, please just use {@link #VerifaliaRestClient(AuthenticationProvider)}.
     */
    public VerifaliaRestClient(@NonNull final AuthenticationProvider authenticationProvider, @NonNull final BaseURIProvider baseURIProvider, @NonNull final String apiVersion) {
        List<URI> baseURIs = baseURIProvider.provideBaseURIs();
        Collections.shuffle(baseURIs);

        cachedRestClient = new RestClient(authenticationProvider, baseURIs, apiVersion);
    }

    /**
     * Allows to submit and manage email validations using the Verifalia service.
     */
    public EmailValidationsRestClient getEmailValidations() {
        if (emailValidations == null)
            emailValidations = new EmailValidationsRestClient(cachedRestClient);
        return emailValidations;
    }

    /**
     * Allows to manage the credits for the Verifalia account.
     */
    public CreditsRestClient getCredits() {
        if (credits == null)
            credits = new CreditsRestClient(cachedRestClient);
        return credits;
    }
}
