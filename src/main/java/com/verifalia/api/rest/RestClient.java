package com.verifalia.api.rest;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.rest.security.BasicAuthentication;
import com.verifalia.api.rest.security.BearerAuthentication;

import lombok.Getter;
import lombok.Setter;

/***
 * Represents REST client
 */
@Getter
@Setter
public class RestClient {

	/**
	 * Base URI
	 */
	private URI baseURI;

	/**
	 * API version
	 */
	private String apiVersion;

	/**
	 * User agent string
	 */
	private String userAgent;

	/**
	 * Authentication string
	 */
	private String authString;

	/**
	 * Creates new object using given host name, API version, authentication details and user agent.
	 * @param baseURL Base URL of the server
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @param userAgent User agent
	 * @throws URISyntaxException
	 */
	public RestClient(String baseURL, String apiVersion, String accountSid, String authToken, String userAgent) throws URISyntaxException {
		this.baseURI = new URI(baseURL);
		this.apiVersion = apiVersion;
		this.userAgent = userAgent;
		this.authString = new BasicAuthentication(accountSid, authToken).getAuthString();
	}

	/**
	 * Creates new object using given host name, API version, bearer authentication details and user agent.
	 * @param baseURL Base URL of the server
	 * @param apiVersion API version name
	 * @param bearerAuth Bearer authentication object for implementing bearer authentication
	 * @param userAgent User agent
	 * @throws URISyntaxException
	 */
	public RestClient(String baseURL, String apiVersion, BearerAuthentication bearerAuth, String userAgent) throws URISyntaxException {
		// TODO - Apply input validation
		this.baseURI = new URI(baseURL);
		this.apiVersion = apiVersion;
		this.userAgent = userAgent;
		this.authString = bearerAuth.getAuthString();
	}

	/**
	 * Creates new object using given authentication details.
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public RestClient(String accountSid, String authToken) throws URISyntaxException {
		this.baseURI = new URI(Constants.DEFAULT_BASE_URL);
		this.apiVersion = Constants.DEFAULT_API_VERSION;
		this.userAgent = Constants.USER_AGENT;
		this.authString = new BasicAuthentication(accountSid, authToken).getAuthString();
	}

	/**
	 * Creates new object using given bearer authentication details.
	 * @param bearerAuth Bearer authentication object for implementing bearer authentication
	 * @throws URISyntaxException
	 */
	public RestClient(BearerAuthentication bearerAuth) throws URISyntaxException {
		this.baseURI = new URI(Constants.DEFAULT_BASE_URL);
		this.apiVersion = Constants.DEFAULT_API_VERSION;
		this.userAgent = Constants.USER_AGENT;
		this.authString = bearerAuth.getAuthString();
	}

	/**
	 * Creates new object using given API version and authentication details.
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public RestClient(String apiVersion, String accountSid, String authToken) throws URISyntaxException {
		this.baseURI = new URI(Constants.DEFAULT_BASE_URL);
		this.apiVersion = apiVersion;
		this.userAgent = Constants.USER_AGENT;
		this.authString = new BasicAuthentication(accountSid, authToken).getAuthString();
	}

	/**
	 * Creates new object using given API version and bearer authentication details.
	 * @param apiVersion API version name
	 * @param bearerAuth Bearer authentication object for implementing bearer authentication
	 * @throws URISyntaxException
	 */
	public RestClient(String apiVersion, BearerAuthentication bearerAuth) throws URISyntaxException {
		this.baseURI = new URI(Constants.DEFAULT_BASE_URL);
		this.apiVersion = apiVersion;
		this.userAgent = Constants.USER_AGENT;
		this.authString = bearerAuth.getAuthString();
	}

	/**
	 * Executes given request
	 * @param request The request
	 * @return RestResponse response object
	 * @throws IOException
	 */
	public RestResponse execute(RestRequest request) throws IOException{
		return execute(request, null);
	}

	/**
	 * Executes given request
	 * @param request The request
	 * @param responseObjectClass Java class of the expected response
	 * @return RestResponse response object
	 * @throws IOException
	 */
	public RestResponse execute(RestRequest request, Class<?> responseObjectClass) throws IOException {
		CloseableHttpResponse response = sendRequest(request);
		if(nonNull(response)){
			int responseCode = response.getStatusLine().getStatusCode();
			String result = StringUtils.EMPTY;
			HttpEntity entity = response.getEntity();
			if(nonNull(entity)){
				result = EntityUtils.toString(entity);
			}
			return new RestResponse(responseCode, result, responseObjectClass);
		}
		return null;
	}

	private CloseableHttpResponse sendRequest(RestRequest request)
			throws UnsupportedEncodingException, MalformedURLException, IOException, ProtocolException {
		StringBuilder sb = new StringBuilder();
		sb.append(baseURI.toString()).append('/').append(apiVersion).append('/').append(request.getResource());
		URI uri = null;
		try {
			uri = new URI(sb.toString());
		} catch(URISyntaxException e){
			throw new IOException("Invalid URI");
		}

		HttpRequestMethod method = request.getMethod();
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;

		switch(method){
			case POST: {
				StringEntity entity = new StringEntity(request.getData());
				HttpPost httpPost = new HttpPost(uri);
			    httpPost.setEntity(entity);
			    httpPost.setHeader(HttpHeaders.USER_AGENT, userAgent);
			    httpPost.setHeader(HttpHeaders.AUTHORIZATION, this.authString);
			    httpPost.setHeader(HttpHeaders.CONTENT_TYPE, Constants.REQUEST_CONTENT_TYPE);
			    httpPost.setHeader(HttpHeaders.ACCEPT, Constants.RESPONSE_ACCEPT_TYPE);
			    response = client.execute(httpPost);
				break;
			}

			case GET: {
				HttpGet httpGet = new HttpGet(uri);
				httpGet.setHeader(HttpHeaders.USER_AGENT, userAgent);
				httpGet.setHeader(HttpHeaders.AUTHORIZATION, this.authString);
				httpGet.setHeader(HttpHeaders.CONTENT_TYPE, Constants.REQUEST_CONTENT_TYPE);
				httpGet.setHeader(HttpHeaders.ACCEPT, Constants.RESPONSE_ACCEPT_TYPE);
			    response = client.execute(httpGet);
				break;
			}

			default: break;
		}
		return response;
	}
}
