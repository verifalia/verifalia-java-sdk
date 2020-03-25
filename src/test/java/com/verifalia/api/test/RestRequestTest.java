package com.verifalia.api.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestRequest;

public class RestRequestTest {

	private final String RESOURCE = "email-verification";
	private final HttpRequestMethod REQUEST_METHOD = HttpRequestMethod.POST;
	private RestRequest request;

	@Before
	public void setUp() throws Exception {
		request = new RestRequest(REQUEST_METHOD, RESOURCE);
	}

	@Test
	public void testRestRequest() {
		RestRequest request = new RestRequest(REQUEST_METHOD, RESOURCE);
		System.out.println(request.toString());
	}

	@Test
	public void testGetMethod() {
		assertEquals(REQUEST_METHOD, request.getMethod());
	}

	@Test
	public void testGetResource() {
		assertEquals(RESOURCE, request.getResource());
	}
}
