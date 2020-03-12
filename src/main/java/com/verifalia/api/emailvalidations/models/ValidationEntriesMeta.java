package com.verifalia.api.emailvalidations.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a snapshot of an email validation batch.
 */
@Getter
@Setter
@ToString
public class ValidationEntriesMeta {

	/**
     * A string representing the cursor meta details for the entries result
     */
    private String cursor;

    /**
     * Signals whether the response is truncated or not. If not present, the response is not truncated.
     */
    private Boolean isTruncated;
}
