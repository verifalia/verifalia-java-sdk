package com.verifalia.api.emailvalidations.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Filter and sort object represents the various filters that can be applied when fetching validation jobs.
 */
@Getter
@Setter
@ToString
public class ValidationsFilter {

	/**
	 * A local date representing the required date, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private LocalDate createdOn;

	/**
	 * A local date representing the inclusive beginning date of the required period, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private LocalDate createdOnSince;

	/**
	 * A local date representing the inclusive ending date of the required period, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private LocalDate createdOnUntil;

	/**
	 * One or more job status codes representing the values you wish to include from the API result. Multiple values are separated with the comma (,) symbol.
	 */
	private Iterable<ValidationStatus> statuses;

	/**
	 * One or more job status codes representing the values you wish to exclude from the API result. Multiple values are separated with the comma (,) symbol.
	 */
	private Iterable<ValidationStatus> excludeStatuses;

	/**
	 * A string representing the Id of the user which you wish to filter the results for; if present, the API will return only the jobs submitted by the specified user.
	 */
	private String owner;

	/**
	 * A object representing the required sorting to apply to the listing operation. Can be one of the following values: createdOn, -createdOn
	 */
	private ValidationsSort sort;
}
