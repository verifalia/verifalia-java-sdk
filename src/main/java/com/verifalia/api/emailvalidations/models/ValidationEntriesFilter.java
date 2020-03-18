package com.verifalia.api.emailvalidations.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Filter object represents the various filters that can be applied when fetching validation entries.
 */
@Getter
@Setter
@ToString
public class ValidationEntriesFilter {
	/**
	 * One or more job status codes representing the values you wish to include from the API result. Multiple values are separated with the comma (,) symbol.
	 */
	private Iterable<String> statuses;

	/**
	 * One or more job status codes representing the values you wish to exclude from the API result. Multiple values are separated with the comma (,) symbol.
	 */
	private Iterable<String> excludeStatuses;
}
