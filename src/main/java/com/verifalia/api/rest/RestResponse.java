package com.verifalia.api.rest;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.verifalia.api.common.Utils;

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
			data = Utils.convertJsonStringToObj(result, responseDataClass);
		} else {
			this.errorMessage = result;
		}
	}
}
