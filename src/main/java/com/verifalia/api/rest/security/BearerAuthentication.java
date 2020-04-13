package com.verifalia.api.rest.security;

import java.io.IOException;
import java.net.URISyntaxException;

import com.verifalia.api.common.Constants;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpRequestBase;

@Getter
@Setter
public class BearerAuthentication extends AuthenticationProvider {

    /**
     * Account ID
     */
    private String username;

    /**
     * Account token
     */
    private String password;

    /**
     * Authentication string
     */
    private String accessToken;

    /**
     * Constructs an object for bearer authentication to authenticate API client
     *
     * @param username Username used to authenticate to Verifalia
     * @param password Password used to authenticate to Verifalia
     */
    public BearerAuthentication(String username, String password) {
        if (username == null)
            throw new IllegalArgumentException("username is null.");
        if (password == null)
            throw new IllegalArgumentException("password is null.");

        this.username = username;
        this.password = password;
    }

    private String getAuthString(RestClient client) throws IOException {
        if (accessToken == null) {
            accessToken = getBearerToken(client, username, password);
        }

        return "Bearer " + accessToken;
    }

    private static String getBearerToken(RestClient client, String username, String password) throws IOException {
        // Build input parameters
        JSONObject authenticateReqJson = new JSONObject();
        authenticateReqJson.put("username", username);
        authenticateReqJson.put("password", password);

        // Make rest request
        RestRequest request = new RestRequest(HttpRequestMethod.POST, "auth/tokens", authenticateReqJson.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = client.execute(request, JSONObject.class, new NoAuthenticationProvider());
        return ((JSONObject) response.getData()).getString("accessToken");
    }

    @Override
    public void decorateRequest(RestClient client, HttpRequestBase request) throws IOException {
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthString(client));
    }
}
