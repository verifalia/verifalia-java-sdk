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
	 * Identifies the limit for the entries to be fetched in one call of the output.
	 */
	private Integer limit;

	/**
	 * One or more job status codes representing the values you wish to include from the API result. Multiple values are separated with the comma (,) symbol.
	 */
	private Iterable<ValidationEntryStatus> statuses;

	/**
	 * One or more job status codes representing the values you wish to exclude from the API result. Multiple values are separated with the comma (,) symbol.
	 */
	private Iterable<ValidationEntryStatus> excludeStatuses;
}
