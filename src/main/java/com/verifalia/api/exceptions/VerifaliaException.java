package com.verifalia.api.exceptions;

import com.verifalia.api.rest.RestResponse;
import lombok.NonNull;

/**
 * Base class for exceptions thrown from this library.
 */
@SuppressWarnings("serial")
public class VerifaliaException extends Exception {

    /**
     * Original response sent by Verifalia servers.
     */
    private RestResponse response;

    /**
     * Creates new object with given message and server response
     *
     * @param response Response form a Verifalia server
     */
    public VerifaliaException(@NonNull final RestResponse response) {
        super(response.getStatusCode() + ": " + response.readAsString());
        this.response = response;
    }

    /**
     * Creates new object with given message and server response
     *
     * @param errorMessage Error message for HTTP request failure
     */
    public VerifaliaException(String errorMessage) {
        super(errorMessage);
    }

    public VerifaliaException(String message, Throwable cause) {
        super(message, cause);
    }
}