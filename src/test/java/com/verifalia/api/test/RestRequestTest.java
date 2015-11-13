package com.verifalia.api.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.test.utils.TestData;

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

	@Test
	public void testAddAndGetBody() {
		request.addEntries(Arrays.asList(TestData.JSON_ARRAY));
		assertEquals(TestData.JSON_ARRAY.length, request.getEntries().size());
		Iterator<RestRequest.Entry> it = request.getEntries().iterator();
		for(int i = 0, n = TestData.JSON_ARRAY.length; i < n; ++i) {
			System.out.println(">>> i = " + i);
			assertEquals(TestData.JSON_ARRAY[i], it.next().getInputData());
		}
	}

}
