/*
 * Verifalia - Email list cleaning and real-time email verification service
 * https://verifalia.com/
 * support@verifalia.com
 *
 * Copyright (c) 2005-2020 Cobisi Research
 *
 * Cobisi Research
 * Via Prima Strada, 35
 * 35129, Padova
 * Italy - European Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.verifalia.api.emailvalidations.models;

import com.verifalia.api.common.DurationDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.time.Duration;
import java.util.Date;

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
    private String quality;

    /**
     * Identifies the deduplication algorithm requested for the job; can be one of Off, Safe and Relaxed.
     */
    private DeduplicationMode deduplication;

    /**
     * The number of email addresses included in this job.
     */
    private Integer noOfEntries;

    /**
     * The completion progress for the batch.
     */
    private ValidationProgress progress;

    /**
     * The data retention period to observe for the validation job. Verifalia deletes the job and its data once
     * its retention period is over, starting to count when the job gets completed.
     */
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration retention;

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
