package com.verifalia.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

import com.verifalia.api.baseURIProviders.BaseURIProvider;
import com.verifalia.api.rest.security.BasicAuthentication;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;
import com.verifalia.api.test.utils.MiniHTTPD;
import com.verifalia.api.test.utils.TestData;

public class RestClientTest {

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
		restClient = new RestClient(new BasicAuthentication("SID", "AUTH_TOKEN"), new BaseURIProvider() {
			@Override
			public ArrayList<URI> provideBaseURIs() {
				return new ArrayList<URI>() {{
					try {
						add(new URI("http://localhost:" + httpd.getPort()));
					} catch (URISyntaxException e) {
					}
				}};
			}
		});
	}

	@Test
	public void testRestClient() throws URISyntaxException {
		RestClient client = new RestClient(new BasicAuthentication("SID", "AUTH_TOKEN"), new BaseURIProvider() {
			@Override
			public ArrayList<URI> provideBaseURIs() {
				return new ArrayList<URI>() {{
					try {
						add(new URI("http://localhost:" + httpd.getPort()));
					} catch (URISyntaxException e) {
					}
				}};
			}
		});

		System.out.println(client.toString());
	}

	@Ignore @Test
	public void testExecuteRestRequest() throws IOException {
		RestRequest request = new RestRequest(HttpRequestMethod.POST, RESOURCE + "/" + UUID.randomUUID().toString());
		RestResponse response = restClient.execute(request);
		assertNotNull(response);
		assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}

	@Ignore @Test
	public void testExecuteRestRequestClassOfQ() throws IOException {
		RestRequest request = new RestRequest(HttpRequestMethod.POST, RESOURCE + "/" + UUID.randomUUID().toString());
		RestResponse response = restClient.execute(request, Validation.class);
		assertNotNull(response);
		assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		assertNotNull(response.getData());
	}

	@Test
	public void testGetAPIVersion() {
		assertEquals("v2.1", restClient.getApiVersion());
	}

	@Test
	public void testSetAPIVersion() {
		restClient.setApiVersion("v2.1");
		assertEquals("v2.1", restClient.getApiVersion());
	}
}
