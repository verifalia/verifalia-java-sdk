package com.verifalia.api.rest.security;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicAuthentication {
	/**
	 * Authentication string
	 */
	private String authString;

	/**
	 * Constructs an object for basic authentication to authenticate API client
	 * @param accountSid Account SID
	 * @param authToken Authentication token
	 * @throws IllegalArgumentException
	 */
	public BasicAuthentication(String accountSid, String authToken) {
		this.authString = getAuthString(accountSid, authToken);
	}

	private String getAuthString(String accountSid, String authToken) throws IllegalArgumentException {
		String authString = StringUtils.EMPTY;
		try {
			byte[] authBytes = (accountSid + ':' + authToken).getBytes("UTF-8");
			// Compatible with JDK 1.7
			authString = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(authBytes);
		} catch(UnsupportedEncodingException ex) {
			throw new IllegalArgumentException("Invalid format parameters passed");
		}
		return authString;
	}
}
