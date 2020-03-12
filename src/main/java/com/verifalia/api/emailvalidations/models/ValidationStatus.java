package com.verifalia.api.emailvalidations.models;

/**
 * The possible statuses for an email validation batch.
 */
public enum ValidationStatus {
    /**
     * Signifies that the job is being processed by Verifalia
     */
	InProgress,

    /**
     * Signifies that the job has been completed and its validated email addresses are available
     */
    Completed,

    /**
     * Signifies that the job has been deleted
     */
    Deleted,

	/**
     * Signifies that the job has been expired
     */
    Expired
}
