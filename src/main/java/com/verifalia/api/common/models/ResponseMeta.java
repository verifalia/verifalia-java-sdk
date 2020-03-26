package com.verifalia.api.common.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents meta information for all Verifalia API responses
 */
@Getter
@Setter
@ToString
public class ResponseMeta {

	/**
     * A string representing the cursor meta details for the entries result
     */
    private String cursor;

    /**
     * Signals whether the response is truncated or not. If not present, the response is not truncated.
     */
    private Boolean isTruncated = Boolean.FALSE;
}
