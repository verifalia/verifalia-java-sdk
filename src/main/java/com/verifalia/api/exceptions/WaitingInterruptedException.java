/**
 *
 */
package com.verifalia.api.exceptions;

/**
 * Indicates that an unhandled exception was thrown while waiting for a job completion.
 */
@SuppressWarnings("serial")
public class WaitingInterruptedException extends VerifaliaException {
    public WaitingInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}