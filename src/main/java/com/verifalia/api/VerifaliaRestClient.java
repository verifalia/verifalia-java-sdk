package com.verifalia.api;

import java.net.URISyntaxException;

import com.verifalia.api.common.Constants;
import com.verifalia.api.credits.CreditsRestClient;
import com.verifalia.api.emailvalidations.EmailValidationsRestClient;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.security.BearerAuthentication;

/**
 * This is main Verifalia SDK REST client "facade" class to be used directly by the SDK end-users.
 * It provides access to various SDK sub-facilities, including email validation service.
 */
public class VerifaliaRestClient {


	/**
	 * REST client object
	 */
	RestClient restClient;

	/**
	 * Email validation service client object
	 */
	EmailValidationsRestClient emailValidations;

	/**
	 * Credits service client object
	 */
	CreditsRestClient credits;

	/**
	 * Returns current client verion
	 */
	public static String getClientVersion() {
		return Constants.CLIENT_VERSION;
	}

	/**
	 * Creates new object using given host name and API version.
         * <p>Your account SID and Authentication token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
	 * @param baseURL Base URL of the Verifalia server
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public VerifaliaRestClient(String baseURL, String apiVersion, String accountSid, String authToken) throws URISyntaxException {
		restClient = new RestClient(baseURL, apiVersion, accountSid, authToken, Constants.USER_AGENT);
	}

	/**
	 * Creates new object using given host name and API version.
         * <p>Your account SID and Authentication token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
	 * @param baseURL Base URL of the Verifalia server
	 * @param apiVersion API version name
	 * @param bearerAuth Bearer authentication object which needs accountSid and authentication token
	 * @throws URISyntaxException
	 */
	public VerifaliaRestClient(String baseURL, String apiVersion, BearerAuthentication bearerAuth) throws URISyntaxException {
		restClient = new RestClient(baseURL, apiVersion, bearerAuth, Constants.USER_AGENT);
	}

	/**
	 * Creates new object using {@link Constants#DEFAULT_BASE_URL} and given API version.
         * <p>Your account SID and authentication token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public VerifaliaRestClient(String apiVersion, String accountSid, String authToken) throws URISyntaxException {
		restClient = new RestClient(apiVersion, accountSid, authToken);
	}

	/**
	 * Creates new object using {@link Constants#DEFAULT_BASE_URL} and given API version.
         * <p>Your account SID and authentication token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
	 * @param apiVersion API version name
	 * @param bearerAuth Bearer authentication object which needs accountSid and authentication token
	 * @throws URISyntaxException
	 */
	public VerifaliaRestClient(String apiVersion, BearerAuthentication bearerAuth) throws URISyntaxException {
		restClient = new RestClient(apiVersion, bearerAuth);
	}

	/**
	 * Creates new object using {@link Constants#DEFAULT_BASE_URL} and {@link Constants#DEFAULT_API_VERSION}.
         * <p>Your account SID and authentication token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public VerifaliaRestClient(String accountSid, String authToken) throws URISyntaxException {
		restClient = new RestClient(accountSid, authToken);
	}

	/**
	 * Creates new object using {@link Constants#DEFAULT_BASE_URL} and {@link Constants#DEFAULT_API_VERSION}.
         * <p>Your account SID and authentication token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: <a href="https://verifalia.com/client-area/subscriptions">https://verifalia.com/client-area/subscriptions</a>
	 * @param bearerAuth Bearer authentication object which needs accountSid and authentication token
	 * @throws URISyntaxException
	 */
	public VerifaliaRestClient(BearerAuthentication bearerAuth) throws URISyntaxException {
		restClient = new RestClient(bearerAuth);
	}

	/**
	 * Returns Verifalia email validations service client object
	 */
	public EmailValidationsRestClient getEmailValidations () {
		if(emailValidations == null)
			emailValidations = new EmailValidationsRestClient(restClient);
		return emailValidations;
	}

	/**
	 * Returns Verifalia credits service client object
	 */
	public CreditsRestClient getCredits() {
		if(credits == null)
			credits = new CreditsRestClient(restClient);
		return credits;
	}
}
