package com.verifalia.api.emailvalidations.models.output;

import java.util.Date;

import com.verifalia.api.emailvalidations.models.ValidationDeDuplication;
import com.verifalia.api.emailvalidations.models.ValidationQuality;
import com.verifalia.api.emailvalidations.models.ValidationStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a snapshot of an email validation batch.
 */
@Getter
@Setter
@ToString
public class ValidationOverview {

	/**
     * A string representing the unique identifier for the job which Verifalia generates upon receiving an email validation request.
     */
    private String id;

    /**
     * The completion status for the batch.
     */
    private ValidationStatus status;

    /**
     * A string with the user-defined name eventually given to this job at the time of its submission: used for your own internal reference
     */
    private String name;

    /**
     * A string representing the unique identifier of the user which created the job.
     */
    private String owner;

    /**
     * IP for client who submitted the email validation batch.
     */
    private String clientIP;

    /**
     * Identifies the results quality level requested for the job; can be one of Standard, High and Extreme (or other values for custom quality levels).
     */
    private ValidationQuality quality;

    /**
     * Identifies the deduplication algorithm requested for the job; can be one of Off, Safe and Relaxed.
     */
    private ValidationDeDuplication deduplication;

    /**
     * The number of email addresses included in this job.
     */
    private Integer noOfEntries;

    /**
     * The completion progress for the batch.
     */
    private ValidationProgress progress;

    /**
     * A string with the data retention period to observe for the validation job, expressed in the format dd.hh:mm:ss (where dd: days, hh: hours, mm: minutes, ss: seconds); the initial dd. part is added only for periods of more than 24 hours. Verifalia deletes the job and its data once its retention period is over, starting to count when the job gets completed.
     */
    private String retention;

    /**
     * A date representing the timestamp of the eventual submission of the job, in the ISO 8601 format
     */
    private Date submittedOn;

    /**
     * A date representing the timestamp of the creation of the job, in the ISO 8601 format.
     */
    private Date createdOn;

    /**
     * A date representing the timestamp of the eventual completion of the job, in the ISO 8601 format.
     */
    private Date completedOn;
}
