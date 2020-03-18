package com.verifalia.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Represent the waiting options for a submission to the Verifalia API.
 */
@Getter
@Setter
public class WaitForCompletionOptions {
    /**
     * Instructs the client to avoid waiting for the completion of the submission at the Verifalia side; will
     * return it in pending status, if it has not been completed yet.
     */
    public static final WaitForCompletionOptions DontWait = new WaitForCompletionOptions();

    /**
     * The timeout to obey while waiting for completion.
     */
    private int timeout;

    /**
     * The polling interval for the completion check.
     */
    private int pollingInterval;

    /**
     * Default interval between subsequent polling requests.
     */
    public static final int DefaultPollingInterval = 5;

    private WaitForCompletionOptions() {
        // Private constructor, used by DontWait
    }

    /**
     * Creates new object with specified timeout and default polling interval
     */
    public WaitForCompletionOptions(int timeout) {
    	this.timeout = timeout;
    	this.pollingInterval = DefaultPollingInterval;
    }

    /**
     * Creates new object with specified timeout and polling interval
     */
    public WaitForCompletionOptions(int timeout, int pollingInterval) {
    	this.timeout = timeout;
    	this.pollingInterval = pollingInterval;
    }
}
