package com.verifalia.api.rest;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import com.verifalia.api.exceptions.AuthorizationException;
import com.verifalia.api.exceptions.InsufficientCreditException;
import com.verifalia.api.exceptions.VerifaliaException;

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
	 * @param statusCode Status code
	 * @param result Result string
	 * @param responseDataClass Class in which the response data needs to be mapped
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public RestResponse(int statusCode, String result, Class<?> responseDataClass)
			throws JsonParseException, JsonMappingException, IOException {
		this.statusCode = statusCode;
		if(HttpStatusCode.isSuccess(statusCode)) {
			if(!StringUtils.isBlank(result)){
				data = Utils.convertJsonStringToObj(result, responseDataClass);
			}
		} else {
			if(!StringUtils.isBlank(result)){
				this.errorMessage = result;
			} else {
				this.errorMessage = Constants.UNKNOWN_ERROR_MSG;
			}
			// 410 status needs to be considered without throwing exception as if a job is deleted, the API returns this code
			if(statusCode != HttpStatusCode.GONE){
				if(statusCode == HttpStatusCode.UNAUTHORIZED){
					throw new AuthorizationException(this);
				} else if(statusCode == HttpStatusCode.PAYMENT_REQUIRED){
					throw new InsufficientCreditException(this);
				} else {
					throw new VerifaliaException(this);
				}
			}
		}
	}

	/**
	 * Creates new object
	 * @param statusCode Status code
	 * @param errorMessage Error message
	 */
	public RestResponse(int statusCode, String errorMessage){
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}
}
