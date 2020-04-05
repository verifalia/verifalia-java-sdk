package com.verifalia.api.emailvalidations.models.input;

import java.util.List;

import com.verifalia.api.emailvalidations.models.ValidationDeDuplication;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the input object for email validations request
 */
@Getter
@Setter
@ToString
public class ValidationInput {
	/**
	 * Entries for emails that needs to be processed
	 */
	private List<ValidationEntryInput> entries;

	 /**
     * Identifies the results quality level requested for the job; can be one of Standard, High and Extreme (or other values for custom quality levels).
     */
    private String quality;

    /**
     * Identifies the deduplication algorithm requested for the job; can be one of Off, Safe and Relaxed.
     */
    private ValidationDeDuplication deduplication;

    /**
     * Identifies the priority of the validation job, relative to the parent Verifalia account. In the event of an account with many concurrent jobs at the same time, this value allows to increase the processing slot time percentage of a job with respect to the others. The allowed range of integer values spans from 0 (lowest priority) to 255 (highest priority), where the midway value 127 means normal priority; if not specified, Verifalia processes the validation job without a specific priority.
     */
    private Integer priority;
}
