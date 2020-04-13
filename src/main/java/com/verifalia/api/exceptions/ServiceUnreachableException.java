/**
 * 
 */
package com.verifalia.api.exceptions;

import com.verifalia.api.rest.RestResponse;

/**
 * Indicates that all of the Verifalia API endpoints are unreachable.
 */
@SuppressWarnings("serial")
public class ServiceUnreachableException extends VerifaliaException {

	/**
	 * Creates new object
	 * @param response Response form a Verifalia server
	 */
	public ServiceUnreachableException(String errorMessage) {
		super(errorMessage);
	}

}
