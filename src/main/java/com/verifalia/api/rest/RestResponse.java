package com.verifalia.api.rest;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents REST service response
 */
@Getter
@Setter
@ToString
public class RestResponse {
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
	private String errorMessage = StringUtils.EMPTY;

	/**
	 * Creates new object
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public RestResponse(int statusCode, String result, Class<?> responseDataClass)
			throws JsonParseException, JsonMappingException, IOException {
		this.statusCode = statusCode;
		if(HttpStatusCode.isSuccess(statusCode)) {
			if(responseDataClass != null) {
				JsonFactory factory = new JsonFactory();
				factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
				factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
				ObjectMapper mapper = new ObjectMapper(factory);
				data = mapper.readValue(result, responseDataClass);
			}
		} else {
			this.errorMessage = result;
		}
	}
}
