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
import lombok.Setter;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import static java.util.Objects.nonNull;

/**
 * Allows to authenticate a REST client against the Verifalia API using an X509 client certificate.
 */
@Getter
@Setter
public class ClientCertificateAuthenticationProvider extends AuthenticationProvider {
    private static final String TLS_AUTHENTICATION_JKS = "jks";

    /**
     * Certificate alias.
     */
    private String certAlias;

    /**
     * Certificate password.
     */
    private String certPassword;

    /**
     * Identity store JKS file.
     */
    private File identityStoreJksFile;

    /**
     * Trust key store JKS file.
     */
    private File trustKeyStoreJksFile;

    /**
     * SSL Connection socket factory.
     */
    private SSLConnectionSocketFactory sslConnectionSocketFactory;

    /**
     * Constructs an object for TLS client certificate authentication.
     *
     * @param certAlias            Certificate alias
     * @param certPassword         Certificate password
     * @param identityStoreJksFile Identity key-store JKS file
     * @param trustKeyStoreJksFile Trust key-store JKS file
     * @throws Exception
     */
    public ClientCertificateAuthenticationProvider(String certAlias, String certPassword, File identityStoreJksFile, File trustKeyStoreJksFile) {
        this.certAlias = certAlias;
        this.certPassword = certPassword;
        this.identityStoreJksFile = identityStoreJksFile;
        this.trustKeyStoreJksFile = trustKeyStoreJksFile;
    }

    /**
     * Gets SSL connection socket factory object for TLS Authentication
     *
     * @return SSLConnectionSocketFactory SSL connection socket factory
     * @throws Exception
     */
    public SSLConnectionSocketFactory getSSlConnectionSocketFactory() throws IOException {
        if (!nonNull(this.sslConnectionSocketFactory)) {
            try {
                this.sslConnectionSocketFactory = getSSlConnectionSocketFactory(this.certAlias, this.certPassword,
                        this.identityStoreJksFile, this.trustKeyStoreJksFile);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return this.sslConnectionSocketFactory;
    }

    private SSLConnectionSocketFactory getSSlConnectionSocketFactory(final String certAlias, final String certPassword,
                                                                     File identityStoreJksFile, File trustKeyStoreJksFile) throws Exception {
        // Load identity key store
        KeyStore identityKeyStore = KeyStore.getInstance(TLS_AUTHENTICATION_JKS);
        FileInputStream identityKeyStoreFile = new FileInputStream(identityStoreJksFile);
        identityKeyStore.load(identityKeyStoreFile, certPassword.toCharArray());

        // Load trust key store
        KeyStore trustKeyStore = KeyStore.getInstance(TLS_AUTHENTICATION_JKS);
        FileInputStream trustKeyStoreFile = new FileInputStream(trustKeyStoreJksFile);
        trustKeyStore.load(trustKeyStoreFile, certPassword.toCharArray());

        // Load SSL context
        SSLContext sslContext = SSLContexts
                .custom()
                .loadKeyMaterial(identityKeyStore,
                        certPassword.toCharArray(),
                        (aliases, socket) -> certAlias)
                .loadTrustMaterial(trustKeyStore, null)
                .build();

        // Initialize socket factory
        return new SSLConnectionSocketFactory(sslContext,
                new String[]{
                        "TLSv1.1",
                        "TLSv1.2"
                },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    @Override
    public CloseableHttpClient buildClient(RestClient client) throws IOException {
        return HttpClients.custom().setSSLSocketFactory(this.getSSlConnectionSocketFactory()).build();
    }
}
