/**
 *
 */
package com.verifalia.api.exceptions;

import com.verifalia.api.rest.RestResponse;

/**
 * Indicates that the credit of the requesting account is not large enough to perform an operation.
 */
@SuppressWarnings("serial")
public class InsufficientCreditException extends VerifaliaException {

    /**
     * Creates new object
     * @param response Response form a Verifalia server
     */
    public InsufficientCreditException(RestResponse response) {
        super(response);
    }

}
