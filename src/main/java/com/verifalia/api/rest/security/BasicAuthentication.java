package com.verifalia.api.rest.security;

import com.verifalia.api.rest.RestClient;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpRequestBase;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class BasicAuthentication extends AuthenticationProvider {

    /**
     * Username used to authenticate to Verifalia
     */
    private String username;

    /**
     * Password used to authenticate to Verifalia
     */
    private String password;

    /**
     * Constructs an object for basic authentication to authenticate API client
     *
     * @param username The username used to authenticate to Verifalia
     * @param password The password used to authenticate to the Verifalia
     * @throws IllegalArgumentException
     */
    public BasicAuthentication(String username, String password) {
        if (username == null)
            throw new IllegalArgumentException("username is null.");

        this.username = username;
        this.password = password;
    }

    private String getAuthString() throws IllegalArgumentException {
        String authString = StringUtils.EMPTY;
        byte[] authBytes = (username + ':' + password).getBytes(StandardCharsets.UTF_8);
        authString = "Basic " + new String(Base64.encodeBase64(authBytes));
        return authString;
    }

    @Override
    public void decorateRequest(RestClient client, HttpRequestBase request) {
        request.setHeader(HttpHeaders.AUTHORIZATION, getAuthString());
    }
}
