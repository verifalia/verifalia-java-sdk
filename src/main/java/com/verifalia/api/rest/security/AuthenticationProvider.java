package com.verifalia.api.rest.security;

import com.verifalia.api.common.Constants;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;

@Getter
@Setter
public abstract class AuthenticationProvider {
	public void decorateRequest(RestClient client, HttpRequestBase request) throws IOException {
	}

	public CloseableHttpClient buildClient(RestClient client) throws IOException {
		return HttpClients.createDefault();
	}
}
