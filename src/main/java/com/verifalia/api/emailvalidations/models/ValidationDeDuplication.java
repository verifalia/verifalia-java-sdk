package com.verifalia.api.emailvalidations.models;

/**
 * An optional string with the name of the algorithm the engine will use to scrub the list of email addresses and remove its duplicates.
 * If not specified, Verifalia will not mark duplicated email addresses.
 */
public enum ValidationDeDuplication {
    /**
     * Off does not mark duplicated email addresses.
     */
	Off,

    /**
     * Safe mark duplicated email addresses with an algorithm which guarantees no false duplicates are returned.
     */
    Safe,

    /**
     * Relaxed mark duplicated email addresses using a set of relaxed rules which assume the target email service providers are configured with modern settings only.
     */
    Relaxed
}
