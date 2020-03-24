package com.verifalia.api.emailvalidations.models;

/**
 * Provides enumerated values that specify the supported classification of a single email address validation entry.
 */
public enum ValidationJobsSort {
    /**
     * The default value, which sorts email validation jobs by their submission dates from the oldest to the most recent one.
     */
	CreatedOnAsc("createdOn"),

    /**
     * Sorts email validation jobs by their submission dates from the most recent to the oldest one.
     */
	CreatedOnDesc("-createdOn");

	String sortFilter;

	ValidationJobsSort(String sortFilter) {
		this.sortFilter = sortFilter;
	}

	public String getValidationJobsSort() {
		return sortFilter;
	}
}
