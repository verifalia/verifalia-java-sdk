package com.verifalia.api.rest.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.verifalia.api.rest.RestClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;

@Getter
@Setter
public class BasicAuthentication extends AuthenticationProvider {

	/**
	 * Username used to authenticate to Verifalia
	 */
	private String username;

	/**
	 * Password used to authenticate to Verifalia
	 */
	private String password;

	/**
	 * Constructs an object for basic authentication to authenticate API client
	 * @param username The username used to authenticate to Verifalia
	 * @param password The password used to authenticate to the Verifalia
	 * @throws IllegalArgumentException
	 */
	public BasicAuthentication(String username, String password) {
		if (username == null)
			throw new IllegalArgumentException("username is null.");

		this.username = username;
		this.password = password;
	}

	private String getAuthString() throws IllegalArgumentException {
		String authString = StringUtils.EMPTY;
		try {
			byte[] authBytes = (username + ':' + password).getBytes("UTF-8");
			authString = "Basic " + new String(Base64.encodeBase64(authBytes));
		} catch(UnsupportedEncodingException ex) {
			throw new IllegalArgumentException("Invalid format parameters passed");
		}
		return authString;
	}

	@Override
	public void decorateRequest(RestClient client, HttpRequestBase request) throws IOException {
		request.setHeader(HttpHeaders.AUTHORIZATION, getAuthString());
	}
}
