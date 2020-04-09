package com.verifalia.api.rest;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import com.verifalia.api.exceptions.AuthorizationException;
import com.verifalia.api.exceptions.InsufficientCreditException;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.security.BasicAuthentication;
import com.verifalia.api.rest.security.BearerAuthentication;
import com.verifalia.api.rest.security.TLSAuthentication;

import lombok.Getter;
import lombok.Setter;

/***
 * Represents REST client
 */
@Getter
@Setter
public class RestClient {

	/**
	 * Base URI List
	 */
	private List<URI> baseURIs;

	/**
	 * API version
	 */
	private String apiVersion;

	/**
	 * User agent string
	 */
	private String userAgent;

	/**
	 * Authentication type
	 */
	private String authType;

	/**
	 * Basic Authentication
	 */
	private BasicAuthentication basicAuth;

	/**
	 * Bearer Authentication
	 */
	private BearerAuthentication bearerAuth;

	/**
	 * TLS Authentication
	 */
	private TLSAuthentication tlsAuth;

	/**
	 * Creates new object using given with default values
	 * @throws URISyntaxException
	 */
	public RestClient() throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(Constants.DEFAULT_BASE_URL_LIST);
		this.apiVersion = Constants.DEFAULT_API_VERSION;
		this.userAgent = Constants.USER_AGENT;
	}

	/**
	 * Creates new object using given host name, API version, authentication details and user agent.
	 * @param baseURL Base URL of the server
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @param userAgent User agent
	 * @throws URISyntaxException
	 */
	public RestClient(String baseURL, String apiVersion, String accountSid, String authToken, String userAgent)
			throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(baseURL);
		this.apiVersion = apiVersion;
		this.userAgent = userAgent;
		this.authType = Constants.AUTHENTICATION_BASIC;
		this.basicAuth = new BasicAuthentication(accountSid, authToken);
	}

	/**
	 * Creates new object using given host name, API version, bearer authentication details and user agent.
	 * @param baseURL Base URL of the server
	 * @param apiVersion API version name
	 * @param bearerAuth Bearer authentication object for implementing bearer authentication
	 * @param userAgent User agent
	 * @throws URISyntaxException
	 */
	public RestClient(String baseURL, String apiVersion, BearerAuthentication bearerAuth, String userAgent)
			throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(baseURL);
		this.apiVersion = apiVersion;
		this.userAgent = userAgent;
		this.authType = Constants.AUTHENTICATION_BEARER;
		this.bearerAuth = bearerAuth;
	}

	/**
	 * Creates new object using given authentication details.
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public RestClient(String accountSid, String authToken) throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(Constants.DEFAULT_BASE_URL_LIST);
		this.apiVersion = Constants.DEFAULT_API_VERSION;
		this.authType = Constants.AUTHENTICATION_BASIC;
		this.basicAuth = new BasicAuthentication(accountSid, authToken);
	}

	/**
	 * Creates new object using given bearer authentication details.
	 * @param bearerAuth Bearer authentication object for implementing bearer authentication
	 * @throws URISyntaxException
	 */
	public RestClient(BearerAuthentication bearerAuth) throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(Constants.DEFAULT_BASE_URL_LIST);
		this.apiVersion = Constants.DEFAULT_API_VERSION;
		this.userAgent = Constants.USER_AGENT;
		this.authType = Constants.AUTHENTICATION_BEARER;
		this.bearerAuth = bearerAuth;
	}

	/**
	 * Creates new object using given TLS authentication details.
	 * @param tlsAuthentication TLS authentication object for implementing two way SSL authentication
	 * @throws URISyntaxException
	 */
	public RestClient(TLSAuthentication tlsAuthentication) throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(Constants.DEFAULT_TLS_BASE_URL_LIST);
		this.apiVersion = Constants.DEFAULT_API_VERSION;
		this.userAgent = Constants.USER_AGENT;
		this.authType = Constants.AUTHENTICATION_TLS;
		this.tlsAuth = tlsAuthentication;
	}

	/**
	 * Creates new object using given API version and authentication details.
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public RestClient(String apiVersion, String accountSid, String authToken) throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(Constants.DEFAULT_BASE_URL_LIST);
		this.apiVersion = apiVersion;
		this.userAgent = Constants.USER_AGENT;
		this.authType = Constants.AUTHENTICATION_BASIC;
		this.basicAuth = new BasicAuthentication(accountSid, authToken);
	}

	/**
	 * Creates new object using given API version and bearer authentication details.
	 * @param apiVersion API version name
	 * @param bearerAuth Bearer authentication object for implementing bearer authentication
	 * @throws URISyntaxException
	 */
	public RestClient(String apiVersion, BearerAuthentication bearerAuth) throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(Constants.DEFAULT_BASE_URL_LIST);
		this.apiVersion = apiVersion;
		this.userAgent = Constants.USER_AGENT;
		this.authType = Constants.AUTHENTICATION_BEARER;
		this.bearerAuth = bearerAuth;
	}

	/**
	 * Creates new object using given TLS authentication details.
	 * @param apiVersion API version name
	 * @param tlsAuthentication TLS authentication object for implementing two way SSL authentication
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	public RestClient(String apiVersion, TLSAuthentication tlsAuthentication) throws URISyntaxException {
		this.baseURIs = convertStringBaseURIToURIList(Constants.DEFAULT_TLS_BASE_URL_LIST);
		this.apiVersion = apiVersion;
		this.userAgent = Constants.USER_AGENT;
		this.authType = Constants.AUTHENTICATION_TLS;
		this.tlsAuth = tlsAuthentication;
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
		if(nonNull(this.baseURIs) && this.baseURIs.size() > 0){
			int index = -1;
			int baseURIsSize = this.baseURIs.size();
			for(int i=0; i<baseURIsSize; i++){
				if(index == -1){
					index = Utils.getRandomNumberInRange(baseURIsSize);
				} else {
					index = (index + 1) % baseURIsSize;
				}
				URI requestURI = baseURIs.get(index);
				CloseableHttpResponse response = null;
				try {
					response = sendRequest(requestURI, request);
				} catch(AuthorizationException e){
		    		throw e;
		    	} catch(InsufficientCreditException e){
		    		throw e;
		    	} catch(VerifaliaException e){
		    		throw e;
		    	} catch(IOException e){
					if(!isHttpRetryNeeded(HttpStatusCode.SERVICE_UNAVAILABLE, i, baseURIsSize)){
						throw e;
					}
				}
				if(nonNull(response)){
					int responseCode = response.getStatusLine().getStatusCode();
					String result = processHttpResponseEntity(response);
					if(isHttpRetryNeeded(responseCode, i, baseURIsSize)){
						continue;
					} else {
						return new RestResponse(responseCode, result, responseObjectClass);
					}
				}
			}
			throw new IOException(Constants.UNKNOWN_ERROR_MSG);
		} else {
			throw new IOException("No URIs defined when making request");
		}
	}

	private CloseableHttpResponse sendRequest(URI baseURI, RestRequest request)
			throws UnsupportedEncodingException, MalformedURLException, IOException, ProtocolException {
		StringBuilder sb = new StringBuilder();
		sb.append(baseURI.toString()).append("/").append(apiVersion).append("/").append(request.getResource());
		URI uri = null;
		try {
			uri = new URI(sb.toString());
		} catch(URISyntaxException e){
			throw new IOException("Invalid URI");
		}

		HttpRequestMethod method = request.getMethod();
		CloseableHttpClient client = HttpClients.createDefault();
		if(isAuthRequired(request.getResource()) &&
				StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_TLS)){
			client = HttpClients.custom().setSSLSocketFactory(this.tlsAuth.getSSlConnectionSocketFactory()).build();
		}
		CloseableHttpResponse response = null;

		switch(method){
			case POST: {
				StringEntity entity = new StringEntity(request.getData());
				HttpPost httpPost = new HttpPost(uri);
			    httpPost.setEntity(entity);
			    httpPost.setHeader(HttpHeaders.USER_AGENT, this.userAgent);
			    if(isAuthRequired(request.getResource()) && isAuthBasicOrBearer()){ // If authentication is basic or bearer
			    	httpPost.setHeader(HttpHeaders.AUTHORIZATION, getAuthString());
			    }
			    httpPost.setHeader(HttpHeaders.CONTENT_TYPE, Constants.REQUEST_CONTENT_TYPE);
		    	httpPost.setHeader(HttpHeaders.ACCEPT, Constants.RESPONSE_ACCEPT_TYPE);
		    	httpPost.setHeader(HttpHeaders.ACCEPT_ENCODING, Constants.RESPONSE_ACCEPT_TYPE_ENCODING);
			    response = client.execute(httpPost);
				break;
			}

			case GET: {
				HttpGet httpGet = new HttpGet(uri);
				httpGet.setHeader(HttpHeaders.USER_AGENT, userAgent);
				if(isAuthRequired(request.getResource()) && isAuthBasicOrBearer()){ // If authentication is basic or bearer
					httpGet.setHeader(HttpHeaders.AUTHORIZATION, getAuthString());
			    }
				httpGet.setHeader(HttpHeaders.CONTENT_TYPE, Constants.REQUEST_CONTENT_TYPE);
			    httpGet.setHeader(HttpHeaders.ACCEPT, Constants.RESPONSE_ACCEPT_TYPE);
			    httpGet.setHeader(HttpHeaders.ACCEPT_ENCODING, Constants.RESPONSE_ACCEPT_TYPE_ENCODING);
			    response = client.execute(httpGet);
				break;
			}

			default: break;
		}
		return response;
	}

	private boolean isAuthRequired(String requestResource){
		if(StringUtils.equalsIgnoreCase(Constants.AUTH_TOKEN_RESOURCE, requestResource)){
			return false;
		}
		return true;
	}

	private boolean isAuthBasicOrBearer(){
		if(StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BASIC) ||
				StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BEARER)){
			return true;
		}
		return false;
	}

	private String getAuthString() throws IOException{
		if(isAuthBasicOrBearer()){
			if(StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BASIC)){
				return this.basicAuth.getAuthString();
			} else if(StringUtils.equalsIgnoreCase(this.authType, Constants.AUTHENTICATION_BEARER)){
				return this.bearerAuth.getAuthString();
			} else {
				return StringUtils.EMPTY;
			}
		}
		return StringUtils.EMPTY;
	}

	private List<URI> convertStringBaseURIToURIList(List<String> strBaseURIList) throws URISyntaxException{
		List<URI> uriList = new ArrayList<URI>();
		for(String strBaseURI: strBaseURIList){
			uriList.add(new URI(strBaseURI));
		}
		return uriList;
	}

	private List<URI> convertStringBaseURIToURIList(String strBaseURI) throws URISyntaxException{
		List<URI> uriList = new ArrayList<URI>();
		uriList.add(new URI(strBaseURI));
		return uriList;
	}

	private String processHttpResponseEntity(CloseableHttpResponse response) throws ParseException, IOException{
		String result = StringUtils.EMPTY;
		if(nonNull(response)){
			HttpEntity entity = response.getEntity();
			if(nonNull(entity)){
				result = EntityUtils.toString(entity);
			}
		}
		return result;
	}

	private boolean isHttpRetryNeeded(int responseCode, int currentIterValue, int totalBaseUriSize){
		boolean isHttpRetryNeeded = false;
		if(HttpStatusCode.isRetryNeeded(responseCode)){ // If retry needed for the status code
			if(currentIterValue != totalBaseUriSize-1){ // Check to see if any base URI is there for processing
				isHttpRetryNeeded = true;
			}
		}
		return isHttpRetryNeeded;
	}

}
