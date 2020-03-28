package com.verifalia.api.rest.security;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.rest.HttpStatusCode;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

@Getter
@Setter
public class BearerAuthentication {

	/**
	 * Authentication string
	 */
	private String authString;

	/**
	 * Constructs an object for bearer authentication to authenticate API client
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 */
	public BearerAuthentication(String accountSid, String authToken) throws URISyntaxException, IOException {
		this.authString = getAuthString(accountSid, authToken);
	}

	private String getAuthString(String accountSid, String authToken) throws URISyntaxException, IOException {
		String bearerToken = getBearerToken(accountSid, authToken);
		String authString = "Bearer " + bearerToken;
		return authString;
	}

	private static String getBearerToken(String accountSid, String authToken) throws URISyntaxException, IOException {
		// Build URL
		StringBuilder sb = new StringBuilder();
		sb.append(new URI(Constants.DEFAULT_BASE_URL).toString()).append('/')
			.append(Constants.DEFAULT_API_VERSION).append('/')
			.append(Constants.AUTH_TOKEN_RESOURCE);
		URI uri = null;
		try {
			uri = new URI(sb.toString());
		} catch(URISyntaxException e){
			throw new IOException("Invalid URI");
		}

		// Build input parameters
		JSONObject authenticateReqJson = new JSONObject();
		authenticateReqJson.put("username", accountSid);
		authenticateReqJson.put("password", authToken);
		StringEntity entity = new StringEntity(authenticateReqJson.toString());

		// Build request headers and set parameters
		HttpPost httpPost = new HttpPost(uri);
	    httpPost.setEntity(entity);
	    httpPost.setHeader(HttpHeaders.USER_AGENT, Constants.USER_AGENT);
	    httpPost.setHeader(HttpHeaders.CONTENT_TYPE, Constants.REQUEST_CONTENT_TYPE);
	    httpPost.setHeader(HttpHeaders.ACCEPT, Constants.RESPONSE_ACCEPT_TYPE);

	    // Execute request
		CloseableHttpClient client = HttpClients.createDefault();
	    CloseableHttpResponse response = client.execute(httpPost);

	    // Handle response
	    JSONObject resultJson = null;
	    int statusCode = response.getStatusLine().getStatusCode();
	    HttpEntity httpEntity = response.getEntity();
	    if (nonNull(httpEntity)) {
	    	resultJson = JSONObject.fromObject(EntityUtils.toString(httpEntity));
	    }
	    if(statusCode == HttpStatusCode.OK){
	    	if(nonNull(resultJson) && nonNull(resultJson.get("accessToken"))){
	    		return resultJson.getString("accessToken");
	        } else {
	        	throw new IOException("Some error occured while processing your request. Please try after sometime");
	        }
	    } else {
	    	throw new IOException(nonNull(resultJson) && nonNull(resultJson.get("help")) ? resultJson.getString("help")
	    			: "Invalid API credentials passed. Please provide valid API credentials to move forward");
	    }
	}
}
