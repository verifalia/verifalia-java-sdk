package com.verifalia.api.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.verifalia.api.rest.HttpStatusCode;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestResponse;
import com.verifalia.api.test.utils.MiniHTTPD;
import com.verifalia.api.test.utils.TestData;

public class RestClientTest {

	private static final String USER_AGENT = "agent/1.0";
	private static final String RESOURCE = "email-verification";
	
	private static MiniHTTPD httpd;

	private RestClient restClient;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		httpd = new MiniHTTPD(TestData.JSON_STRING);
		httpd.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(httpd != null) {
			httpd.stop();
			httpd = null;
		}
	}	

	@Before
	public void setUp() throws Exception {
		restClient = new RestClient("http://localhost:" + httpd.getPort(), "v1.1", "SID", "AUTH_TOKEN", USER_AGENT);
	}

	@Test
	public void testRestClient() throws URISyntaxException {
		RestClient client = new RestClient("http://localhost:" + httpd.getPort(), "v1.1", "SID", "AUTH_TOKEN", USER_AGENT);
		System.out.println(client.toString());
	}

	@Ignore @Test
	public void testExecuteRestRequest() throws IOException {
		RestRequest request = new RestRequest(HttpRequestMethod.POST, RESOURCE + "/" + UUID.randomUUID().toString());
		RestResponse response = restClient.execute(request);
		assertNotNull(response);
		assertEquals(HttpStatusCode.OK, response.getStatusCode());
	}

	@Ignore @Test
	public void testExecuteRestRequestClassOfQ() throws IOException {
		RestRequest request = new RestRequest(HttpRequestMethod.POST, RESOURCE + "/" + UUID.randomUUID().toString());
		RestResponse response = restClient.execute(request, Validation.class);
		assertNotNull(response);
		assertEquals(HttpStatusCode.OK, response.getStatusCode());
		assertNotNull(response.getData());
	}


	@Test
	public void testGetSetBaseURI() {
		restClient.setBaseURI(URI.create("https://verifalia.com:8888"));
		URI uri = restClient.getBaseURI();
		assertEquals("verifalia.com", uri.getHost());
		assertEquals(8888, uri.getPort());
	}

	@Test
	public void testGetAPIVersion() {
		assertEquals("v1.1", restClient.getAPIVersion());
	}

	@Test
	public void testSetAPIVersion() {
		restClient.setAPIVersion("v2.0");
		assertEquals("v2.0", restClient.getAPIVersion());
	}

	@Test
	public void testGetUserAgent() {
		assertEquals(USER_AGENT, restClient.getUserAgent());
	}

}
