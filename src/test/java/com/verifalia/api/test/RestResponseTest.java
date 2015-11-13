package com.verifalia.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import com.verifalia.api.emailvalidation.models.Validation;
import com.verifalia.api.rest.HttpStatusCode;
import com.verifalia.api.rest.RestResponse;
import com.verifalia.api.test.utils.TestData;

public class RestResponseTest {

	private final String EMPTY_STRING = "";
		
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
		try(InputStream stream1 = new ByteArrayInputStream(errorMessage.getBytes("UTF-8"))) {
			errorResponse1 = new RestResponse(HttpStatusCode.UNAUTHORIZED, stream1, null);
		}
		try(InputStream stream2 = new ByteArrayInputStream(errorMessage.getBytes("UTF-8"))) {
			errorResponse2 = new RestResponse(HttpStatusCode.UNAUTHORIZED, stream2, Validation.class);
		}
		try(InputStream stream1 = new ByteArrayInputStream(okMessage.getBytes("UTF-8"))) {
			goodResponse1 = new RestResponse(okCode, stream1, null);
		}
		try(InputStream stream1 = new ByteArrayInputStream(TestData.JSON_STRING.getBytes("UTF-8"))) {
			goodResponse2 = new RestResponse(okCode, stream1, Validation.class);
		}
	}
	
	@Test
	public void testRestResponseIntInputStream() throws IOException {
		try(InputStream stream = new ByteArrayInputStream(okMessage.getBytes("UTF-8"))) {
			RestResponse response = new RestResponse(okCode, stream, null);
			System.out.println(response.toString());
		}
	}

	@Test
	public void testRestResponseIntInputStreamClassOfQ() throws UnsupportedEncodingException, IOException {
		try(InputStream stream = new ByteArrayInputStream(TestData.JSON_STRING.getBytes("UTF-8"))) {
			RestResponse response = new RestResponse(okCode, stream, null);
			System.out.println(response.toString());
		}
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
