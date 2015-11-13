package com.verifalia.api.emailvalidation.models;

/**
 * The possible statuses for an email validation batch.
 */
public enum ValidationStatus {
    /**
     * The email validation batch is being processed by Verifalia.
     */
    Pending,

    /**
     * The email validation batch has been completed and its results are available.
     */
    Completed
}
