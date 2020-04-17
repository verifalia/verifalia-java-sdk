/**
 *
 */
package com.verifalia.api.exceptions;

/**
 * Indicates that a Verifalia API endpoint returned a server error status code (HTTP 5xx).
 */
@SuppressWarnings("serial")
public class EndpointServerErrorException extends VerifaliaException {
    public EndpointServerErrorException(String errorMessage) {
        super(errorMessage);
    }
}