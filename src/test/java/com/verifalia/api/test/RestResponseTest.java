package com.verifalia.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import com.verifalia.api.emailvalidations.models.ValidationMapper;
import com.verifalia.api.rest.RestResponse;
import com.verifalia.api.test.utils.TestData;

public class RestResponseTest {

	private final String EMPTY_STRING = StringUtils.EMPTY;

	private final int okCode = HttpStatus.SC_OK;
	private final String okMessage = "OK";

	private RestResponse goodResponse1;
	private RestResponse goodResponse2;

	@Before
	public void setUp() throws Exception {
		goodResponse1 = new RestResponse(okCode, okMessage, null);
		goodResponse2 = new RestResponse(okCode, TestData.JSON_STRING, ValidationMapper.class);
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
		assertEquals(okCode, goodResponse1.getStatusCode());
		assertEquals(okCode, goodResponse2.getStatusCode());
	}

	@Test
	public void testGetData() {
		assertNull(goodResponse1.getData());
		assertNotNull(goodResponse2.getData());
	}

	@Test
	public void testGetErrorMessage() throws IOException {
		assertEquals(EMPTY_STRING, goodResponse1.getErrorMessage());
		assertEquals(EMPTY_STRING, goodResponse2.getErrorMessage());
	}

}
