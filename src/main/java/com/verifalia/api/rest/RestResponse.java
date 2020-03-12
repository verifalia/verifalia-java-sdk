package com.verifalia.api.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.verifalia.api.common.Utils;

/**
 * Represents REST service response
 */
public class RestResponse {
	/**
	 * Creates new object
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public RestResponse(int statusCode, InputStream input, Class<?> responseDataClass)
			throws JsonParseException, JsonMappingException, IOException {
		this.statusCode = statusCode;
		if(HttpStatusCode.isSuccess(statusCode)) {
			if(responseDataClass != null) {
				JsonFactory factory = new JsonFactory();
				factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
				factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
				ObjectMapper mapper = new ObjectMapper(factory);
				data = mapper.readValue(input, responseDataClass);
			}
		} else
			readErrorMessage(input);
	}

	private void readErrorMessage(InputStream input) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if(sb.length() > 0)
					sb.append('\n');
			    sb.append(Utils.chomp(line));
			}
		}
		this.errorMessage = sb.toString();
	}

	/**
	 * Returns HTTP response code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Returns data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Return error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * HTTP response code
	 */
	private int statusCode;

	/**
	 * Response data
	 */
	private Object data;

	/**
	 * Error message
	 */
	private String errorMessage = Utils.EMPTY_STRING;
}
