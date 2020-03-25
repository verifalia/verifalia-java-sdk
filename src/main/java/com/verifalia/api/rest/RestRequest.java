package com.verifalia.api.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents REST request
 */
@Getter
@Setter
public class RestRequest {

	/**
	 * HTTP request method
	 */
	HttpRequestMethod method;

	/**
	 * Request target resource
	 */
	private String resource;

	/**
	 * HTTP request data
	 */
	private String data;

	/**
	 * Constructs new object for a given resource with given method
	 */
	public RestRequest(HttpRequestMethod method, String resource) {
		this.method = method;
		this.resource = resource;
	}

	/**
	 * Constructs new object for a given resource with given method and given data
	 */
	public RestRequest(HttpRequestMethod method, String resource, String data) {
		this.method = method;
		this.resource = resource;
		this.data = data;
	}
}
