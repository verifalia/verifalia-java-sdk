package com.verifalia.api.emailvalidations.models.output;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the completion progress of an email validation batch.
 */
@Getter
@Setter
@ToString
public class ValidationProgress {

	/**
     * The overall progress percentage for the job, when available, expressed as a number between 0 and 1.
     */
	private Integer percentage;

	/**
     * A string representing the estimated remaining time for completing the email validation job, expressed in the format dd.hh:mm:ss (where dd: days, hh: hours, mm: minutes, ss: seconds); the initial dd. part is added only for huge lists requiring more than 24 hours to complete.
     */
	private String estimatedTimeRemaining;
}