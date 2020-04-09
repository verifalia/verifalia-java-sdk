package com.verifalia.api.exceptions;

import java.io.IOException;

import com.verifalia.api.rest.HttpStatusCode;
import com.verifalia.api.rest.RestResponse;

/**
 * Base class for exceptions thrown from this library.
 */
@SuppressWarnings("serial")
public class VerifaliaException extends IOException {

	/**
	 * Original response sent by Verifalia servers.
	 */
	private RestResponse response;

	/**
	 * Returns original response sent by Verifalia servers.
	 */
	public RestResponse getRestResponse() {
		return response;
	}

	/***
	 * Returns HTTP status code of the response
	 */
	public int getStatusCode() {
		return response.getStatusCode();
	}

	/**
	 * Creates new object with given message and server response
	 * @param response Response form a Verifalia server
	 */
	public VerifaliaException(RestResponse response) {
		super(Integer.toString(response.getStatusCode()) + ": "+  response.getErrorMessage());
		this.response = response;
	}

	/**
	 * Creates new object with given message and server response
	 * @param errorMessage Error message for HTTP request failure
	 */
	public VerifaliaException(String errorMessage) {
		super(Integer.toString(HttpStatusCode.SERVICE_UNAVAILABLE) + ": "+  errorMessage);
	}
}
