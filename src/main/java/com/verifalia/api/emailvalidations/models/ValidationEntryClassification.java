package com.verifalia.api.emailvalidations.models;

/**
 * Provides enumerated values that specify the supported classification of a single email address validation entry.
 */
public enum ValidationEntryClassification {
    /**
     * Refers to an email address which is deliverable.
     */
    Deliverable,

    /**
     * Refers to an email address which is either invalid or no longer deliverable.
     */
    Undeliverable,

    /**
     * Refers to an email address which could be no longer valid.
     */
    Risky,

    /**
     * Refers to an email address whose deliverability is unknown.
     */
    Unknown
}
