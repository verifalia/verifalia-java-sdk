package com.verifalia.api.emailvalidations.models.output;

import java.util.List;

import com.verifalia.api.common.models.ResponseMeta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a snapshot of the validation jobs submitted by the API consumer
 */
@Getter
@Setter
@ToString
public class ValidationJobs {

	/**
     * Meta information for the jobs
     */
	private ResponseMeta meta;

	/**
     * List of all the validation jobs
     */
	private List<ValidationOverview> data;
}
