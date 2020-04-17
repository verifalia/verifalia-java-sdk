package com.verifalia.api;

import com.verifalia.api.baseURIProviders.BaseURIProvider;
import com.verifalia.api.baseURIProviders.ClientCertificateBaseURIProvider;
import com.verifalia.api.baseURIProviders.DefaultBaseURIProvider;
import com.verifalia.api.common.Constants;
import com.verifalia.api.credits.CreditsRestClient;
import com.verifalia.api.emailvalidations.EmailValidationsRestClient;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.security.AuthenticationProvider;
import com.verifalia.api.rest.security.BasicAuthentication;
import com.verifalia.api.rest.security.TLSAuthentication;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

/**
 * This is main Verifalia SDK REST client "facade" class to be used directly by the SDK end-users.
 * It provides access to various SDK sub-facilities, including email validation service.
 */
public class VerifaliaRestClient {
    public static final String DEFAULT_API_VERSION = "v2.1";

    /**
     * REST client object
     */
    RestClient cachedRestClient;

    /**
     * Email validation service client object
     */
    EmailValidationsRestClient emailValidations;

    /**
     * Credits service client object
     */
    CreditsRestClient credits;

    /**
     * Creates new object using {@link Constants#DEFAULT_BASE_URL_LIST} and {@link Constants#DEFAULT_API_VERSION}.
     * <p>Your account SID and authentication token values can be retrieved in your client area,
     * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
     *
     * @param username Account SID
     * @param password Authentication token
     * @throws URISyntaxException
     */
    public VerifaliaRestClient(String username, String password) {
        this(new BasicAuthentication(username, password), new DefaultBaseURIProvider());
    }

    /**
     * Creates new object using {@link Constants#DEFAULT_TLS_BASE_URL_LIST} and {@link Constants#DEFAULT_API_VERSION}.
     * <p>Your account SID and authentication token values can be retrieved in your client area,
     * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
     *
     * @param tlsAuthentication TLS authentication object which needs certificate related details
     * @throws URISyntaxException
     */
    public VerifaliaRestClient(String certAlias, String certPassword, File identityStoreJksFile, File trustKeyStoreJksFile) {
        this(new TLSAuthentication(certAlias, certPassword, identityStoreJksFile, trustKeyStoreJksFile), new ClientCertificateBaseURIProvider());
    }

    public VerifaliaRestClient(AuthenticationProvider authenticationProvider) {
        this(authenticationProvider, new DefaultBaseURIProvider(), DEFAULT_API_VERSION);
    }

    public VerifaliaRestClient(AuthenticationProvider authenticationProvider, String apiVersion) {
        this(authenticationProvider, new DefaultBaseURIProvider(), apiVersion);
    }

    public VerifaliaRestClient(AuthenticationProvider authenticationProvider, BaseURIProvider baseURIProvider) {
        this(authenticationProvider, baseURIProvider, DEFAULT_API_VERSION);
    }

    /**
     * Creates new object using {@link Constants#DEFAULT_BASE_URL_LIST} and {@link Constants#DEFAULT_API_VERSION}.
     * <p>Your account SID and authentication token values can be retrieved in your client area,
     * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
     *
     * @param authenticationProvider An authentication provider object, which allows the client to authenticate to the Verifalia API
     * @throws URISyntaxException
     */
    public VerifaliaRestClient(AuthenticationProvider authenticationProvider, BaseURIProvider baseURIProvider, String apiVersion) {
        List<URI> baseURIs = baseURIProvider.provideBaseURIs();
        Collections.shuffle(baseURIs);

        cachedRestClient = new RestClient(authenticationProvider, baseURIs, apiVersion);
    }

    /**
     * Returns Verifalia email validations service client object
     */
    public EmailValidationsRestClient getEmailValidations() {
        if (emailValidations == null)
            emailValidations = new EmailValidationsRestClient(cachedRestClient);
        return emailValidations;
    }

    /**
     * Returns Verifalia credits service client object
     */
    public CreditsRestClient getCredits() {
        if (credits == null)
            credits = new CreditsRestClient(cachedRestClient);
        return credits;
    }
}
