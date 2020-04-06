package com.verifalia.api.rest.security;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.exceptions.AuthorizationException;
import com.verifalia.api.exceptions.InsufficientCreditException;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.HttpStatusCode;
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
	 */
	public BearerAuthentication(String accountSid, String authToken) throws URISyntaxException {
		this.accountSid = accountSid;
		this.authToken = authToken;
		this.authString = StringUtils.EMPTY;
	}

	/**
	 * Gets authentication string for bearer authentication
	 * @return String Bearer authentication string
	 */
	public String getAuthString() throws IOException{
		if(StringUtils.isBlank(this.authString)){
			this.authString = getAuthString(accountSid, authToken);
		}
		return this.authString;
	}

	private String getAuthString(String accountSid, String authToken) throws IOException {
		String bearerToken = getBearerToken(accountSid, authToken);
		String authString = "Bearer " + bearerToken;
		return authString;
	}

	private static String getBearerToken(String accountSid, String authToken) throws IOException {
		// Build URL
		StringBuilder sb = new StringBuilder();
		URI uri = null;
		try {
			sb.append(new URI(Constants.DEFAULT_BASE_URL).toString()).append('/')
			.append(Constants.DEFAULT_API_VERSION).append('/')
			.append(Constants.AUTH_TOKEN_RESOURCE);
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
	    	String errorMsg = StringUtils.EMPTY;
	    	if(nonNull(resultJson) && nonNull(resultJson.get("help"))){
	    		errorMsg = resultJson.getString("help");
	    	} else {
	    		errorMsg = Constants.UNKNOWN_ERROR_MSG;
	    	}
	    	if(statusCode == HttpStatusCode.UNAUTHORIZED){
				throw new AuthorizationException(new RestResponse(statusCode, errorMsg));
			} else if(statusCode == HttpStatusCode.PAYMENT_REQUIRED){
				throw new InsufficientCreditException(new RestResponse(statusCode, errorMsg));
			} else {
				throw new VerifaliaException(new RestResponse(statusCode, errorMsg));
			}
	    }
	}
}
