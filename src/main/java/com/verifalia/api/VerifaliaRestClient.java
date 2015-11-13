package com.verifalia.api;

import java.net.URISyntaxException;

import com.verifalia.api.emailvalidation.EmailValidationRestClient;
import com.verifalia.api.rest.RestClient;

/**
 * This is main Verifalia SDK REST client "facade" class to be used directly by the SDK end-users.
 * It provides access to various SDK sub-facilities, including email validation service.
 */
public class VerifaliaRestClient
{
	private static final String CLIENT_VERSION = "1.0.0";
	private static final String DEFAULT_BASE_URL = "https://api.verifalia.com";
	private static final String DEFAULT_API_VERSION = "v1.1";
	private static final String USER_AGENT_BASE = "verifalia-rest-client/java/";

	/**
	 * Returns current client verion
	 */
	public static String getClientVersion() {
		return CLIENT_VERSION;
	}
	
	/**
	 * Creates new object using given host name and API version.
         * <p>Your Account SID and Authentication token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: {@link https://verifalia.com/client-area/subscriptions}
	 * @param baseURL Base URL of the Verifalia server
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException 
	 */
	public VerifaliaRestClient(String baseURL, String apiVersion, String accountSid, String authToken) throws URISyntaxException {
		restClient = new RestClient(baseURL, apiVersion, accountSid, authToken, USER_AGENT_BASE + CLIENT_VERSION);
	}
	
	/**
	 * Creates new object using {@link #DEFAULT_BASE_URL} and given API version.
         * <p>Your Account SID and Auth token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: {@link https://verifalia.com/client-area/subscriptions}
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException 
	 */
	public VerifaliaRestClient(String apiVersion, String accountSid, String authToken) throws URISyntaxException {
		restClient = new RestClient(DEFAULT_BASE_URL, apiVersion, accountSid, authToken, USER_AGENT_BASE + CLIENT_VERSION);
	}
	
	/**
	 * Creates new object using {@link #DEFAULT_BASE_URL} and {@link #DEFAULT_API_VERSION}.
         * <p>Your Account SID and Auth token values can be retrieved in your client area,
         * upon clicking on your subscription details, on Verifalia web site at: {@link https://verifalia.com/client-area/subscriptions}
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException 
	 */
	public VerifaliaRestClient(String accountSid, String authToken) throws URISyntaxException {
		restClient = new RestClient(DEFAULT_BASE_URL, DEFAULT_API_VERSION, accountSid, authToken, USER_AGENT_BASE + CLIENT_VERSION);
	}
	
	/**
	 * Returns Verifalia email validations service client object
	 */
	public EmailValidationRestClient getEmailValidations () {
		if(emailValidations == null)
			emailValidations = new EmailValidationRestClient(restClient);
		return emailValidations;
	}

	
	/**
	 * REST client object
	 */
	RestClient restClient;
	
	/**
	 * Email validation service client object
	 */
	EmailValidationRestClient emailValidations;
 
}
