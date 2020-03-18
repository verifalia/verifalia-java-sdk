package com.verifalia.api.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
	 * User Agent string
	 */
	private String userAgent;

	/**
	 * Authentication string
	 */
	private String authString;

	/**
	 * Creates new object using given host name and API version.
	 * @param baseURL Base URL of the server
	 * @param apiVersion API version name
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws URISyntaxException
	 */
	public RestClient(String baseURL, String apiVersion, String accountSid, String authToken, String userAgent) throws URISyntaxException {
		this.baseURI = new URI(baseURL);
		this.apiVersion = apiVersion;
		this.userAgent = userAgent;
		try {
			byte[] authBytes = (accountSid + ':' + authToken).getBytes("UTF-8");
			// Compatible with JDK 1.7
			authString = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(authBytes);
		} catch(UnsupportedEncodingException ex) {/* just ignore it, UTF-8 is supported encoding */ }
	}

	/**
	 * Executes given request
	 * @param request The request
	 * @return REST response object
	 * @throws IOException
	 */
	public RestResponse execute(RestRequest request) throws IOException {
		HttpURLConnection conn = sendRequest(request);
		int responseCode = conn.getResponseCode();
		InputStream input = HttpStatusCode.isSuccess(responseCode) ? conn.getInputStream() : conn.getErrorStream();
		return new RestResponse(responseCode, input, null);
	}

	/**
	 * Executes given request
	 * @param request The request
	 * @param responseObjectClass Java class of the expected response
	 * @return REST response object
	 * @throws IOException
	 */
	public RestResponse execute(RestRequest request, Class<?> responseObjectClass) throws IOException {
		HttpURLConnection conn = sendRequest(request);
		int responseCode = conn.getResponseCode();
		InputStream input = HttpStatusCode.isSuccess(responseCode) ? conn.getInputStream() : conn.getErrorStream();
		return new RestResponse(responseCode, input, responseObjectClass);
	}

	private HttpURLConnection sendRequest(RestRequest request)
			throws UnsupportedEncodingException, MalformedURLException, IOException, ProtocolException {

		StringBuilder sb = new StringBuilder();
		sb.append(baseURI.toString()).append('/').append(apiVersion).append('/').append(request.getResource());
		URL url = new URL(sb.toString());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		HttpRequestMethod method = request.getMethod();
		conn.setRequestMethod(method.name());
		conn.setRequestProperty("User-Agent", userAgent);
		conn.setRequestProperty("Authorization", authString);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("charset", "utf-8");
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(false);

		switch(method){
			case POST: {
				conn.setRequestProperty("Accept", "application/json");
				byte[] body = request.getEncodedEntries().getBytes("UTF-8");
				conn.setRequestProperty("Content-Length", Integer.toString(body.length));
				conn.setDoOutput(true);
				OutputStream out = conn.getOutputStream();
				out.write(body);
				out.flush();
				break;
			}

			case GET: {
				conn.setRequestProperty("Accept", "application/json");
				break;
			}

			default: break;
		}

		return conn;
	}
}
