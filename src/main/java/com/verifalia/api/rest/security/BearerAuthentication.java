package com.verifalia.api.rest.security;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

@Getter
@Setter
public class BearerAuthentication {

	/**
	 * Account ID
	 */
	private String accountSid;

	/**
	 * Account token
	 */
	private String authToken;

	/**
	 * Authentication string
	 */
	private String authString;

	/**
	 * Constructs an object for bearer authentication to authenticate API client
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public BearerAuthentication(String accountSid, String authToken) throws URISyntaxException {
		this.accountSid = accountSid;
		this.authToken = authToken;
 		this.authString = StringUtils.EMPTY;
	}

	/**
	 * Gets authentication string for bearer authentication
	 * @return String Bearer authentication string
	 * @throws IOException
	 */
	public String getAuthString() throws IOException {
		if(StringUtils.isBlank(this.authString)){
			this.authString = getAuthString(this.accountSid, this.authToken);
		}
		return this.authString;
	}

	private String getAuthString(String accountSid, String authToken) throws IOException {
		String bearerToken = getBearerToken(accountSid, authToken);
		String authString = "Bearer " + bearerToken;
		return authString;
	}

	private static String getBearerToken(String accountSid, String authToken) throws IOException {
		// Build input parameters
		JSONObject authenticateReqJson = new JSONObject();
		authenticateReqJson.put("username", accountSid);
		authenticateReqJson.put("password", authToken);

		// Make rest request
    	RestRequest request = new RestRequest(HttpRequestMethod.POST, Constants.AUTH_TOKEN_RESOURCE, authenticateReqJson.toString());

    	// Sends the request to the Verifalia servers
    	try {
    		RestResponse response = new RestClient().execute(request, JSONObject.class);
    		return ((JSONObject)response.getData()).getString("accessToken");
    	} catch(URISyntaxException e){
    		throw new IOException(e.toString());
    	}
	}
}
