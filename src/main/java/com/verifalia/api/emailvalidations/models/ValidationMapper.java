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
public class ValidationMapper {

	/**
     * The overview of the email validation batch.
     */
	private ValidationOverview overview;

	/**
     * Validation entries submiited for the job
     */
	private ValidationEntries entries;
}
