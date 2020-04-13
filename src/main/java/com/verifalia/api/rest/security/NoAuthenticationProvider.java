package com.verifalia.api.rest.security;

import com.verifalia.api.rest.RestClient;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Getter
@Setter
public class NoAuthenticationProvider extends AuthenticationProvider {
}
