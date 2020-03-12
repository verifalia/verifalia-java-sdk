package com.verifalia.api.emailvalidations.models;

/**
 * Provides enumerated values that specify the supported classification of a single email address validation entry.
 */
public enum ValidationEntryDataClassification {
    /**
     * Signifies that email address should be able to accept messages into its mailbox
     */
	Deliverable,

    /**
     * Signifies that the email address is believed to be invalid or inactive;
     */
	Undeliverable,

    /**
     * Signifies that the email address appears to be valid but we are not completely sure about its quality and deliverability
     */
	Risky,

    /**
     * Signifies that Verifalia is unable to validate the email address for technical reasons.
     */
	Unknown
}
