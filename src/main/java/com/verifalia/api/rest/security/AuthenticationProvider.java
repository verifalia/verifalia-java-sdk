package com.verifalia.api.rest.security;

import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.RestClient;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Getter
@Setter
public abstract class AuthenticationProvider {
    public void decorateRequest(RestClient client, HttpRequestBase request) throws VerifaliaException {
    }

    public CloseableHttpClient buildClient(RestClient client) throws IOException {
        return HttpClients.createDefault();
    }
}
