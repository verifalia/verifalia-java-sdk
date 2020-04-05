package com.verifalia.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.rest.HttpStatusCode;
import com.verifalia.api.rest.RestResponse;
import com.verifalia.api.test.utils.TestData;

public class RestResponseTest {

	private final String EMPTY_STRING = StringUtils.EMPTY;

	private final int okCode = HttpStatusCode.OK;
	private final String okMessage = "OK";

	private final int errorCode = HttpStatusCode.UNAUTHORIZED;
	private final String errorMessage = "Authorization failed";

	private RestResponse goodResponse1;
	private RestResponse goodResponse2;
	private RestResponse errorResponse1;
	private RestResponse errorResponse2;


	@Before
	public void setUp() throws Exception {
		errorResponse1 = new RestResponse(HttpStatusCode.UNAUTHORIZED, errorMessage, null);
		errorResponse2 = new RestResponse(HttpStatusCode.UNAUTHORIZED, errorMessage, Validation.class);
		goodResponse1 = new RestResponse(okCode, okMessage, null);
		goodResponse2 = new RestResponse(okCode, TestData.JSON_STRING, Validation.class);
	}

	@Test
	public void testRestResponseIntInputStream() throws IOException {
		RestResponse response = new RestResponse(okCode, okMessage, null);
		System.out.println(response.toString());
	}

	@Test
	public void testRestResponseIntInputStreamClassOfQ() throws IOException {
		RestResponse response = new RestResponse(okCode, TestData.JSON_STRING, null);
		System.out.println(response.toString());
	}

	@Test
	public void testGetStatusCode() throws IOException {
		assertEquals(errorCode, errorResponse1.getStatusCode());
		assertEquals(errorCode, errorResponse2.getStatusCode());
		assertEquals(okCode, goodResponse1.getStatusCode());
		assertEquals(okCode, goodResponse2.getStatusCode());
	}

	@Test
	public void testGetData() {
		assertNull(errorResponse1.getData());
		assertNull(errorResponse2.getData());
		assertNull(goodResponse1.getData());
		assertNotNull(goodResponse2.getData());
	}

	@Test
	public void testGetErrorMessage() throws IOException {
		assertEquals(errorMessage, errorResponse1.getErrorMessage());
		assertEquals(errorMessage, errorResponse2.getErrorMessage());
		assertEquals(EMPTY_STRING, goodResponse1.getErrorMessage());
		assertEquals(EMPTY_STRING, goodResponse2.getErrorMessage());
	}

}
