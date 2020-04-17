package com.verifalia.api.rest.security;

import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;
import lombok.Getter;
import lombok.NonNull;
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
    public BearerAuthentication(@NonNull final String username, @NonNull final String password) {
        this.username = username;
        this.password = password;
    }

    private static String getBearerToken(RestClient client, String username, String password) throws VerifaliaException {
        // Build input parameters
        Credentials credentials = new Credentials();

        credentials.setUsername(username);
        credentials.setPassword(password);

        // Make rest request
        RestRequest request = new RestRequest(HttpRequestMethod.POST, "auth/tokens", credentials);

        // Sends the request to the Verifalia servers
        RestResponse response = client.execute(request, new NoAuthenticationProvider());
        return response.deserialize(JSONObject.class).getString("accessToken");
    }

    private String getAuthString(RestClient client) throws VerifaliaException {
        if (accessToken == null) {
            accessToken = getBearerToken(client, username, password);
        }

        return "Bearer " + accessToken;
    }

    @Override
    public void decorateRequest(RestClient client, HttpRequestBase request) throws VerifaliaException {
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthString(client));
    }

    @Getter
    @Setter
    private static class Credentials {
        private String username;
        private String password;
    }
}
