/**
 * 
 */
package com.verifalia.api.exceptions;

import com.verifalia.api.rest.RestResponse;

/**
 * Signals an issue with the credentials provided to the Verifalia service.
 */
@SuppressWarnings("serial")
public class AuthorizationException extends VerifaliaException {

	/**
	 * Creates new object
	 * @param response Response form a Verifalia server
	 */
	public AuthorizationException(RestResponse response) {
		super(response);
	}

}
