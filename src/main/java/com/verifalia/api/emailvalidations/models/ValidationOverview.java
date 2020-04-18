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

import com.verifalia.api.common.serialization.DurationDeserializer;
import com.verifalia.api.emailvalidations.serialization.QualityLevelNameDeserializer;
import com.verifalia.api.emailvalidations.serialization.ValidationPriorityDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.time.Duration;
import java.util.Date;

/**
 * Overview information for a {@link Validation}.
 */
@Getter
@Setter
@ToString
public class ValidationOverview {

    /**
     * The unique identifier for the validation job.
     */
    private String id;

    /**
     * The processing {@link ValidationStatus} for the validation job.
     */
    private ValidationStatus status;

    /**
     * An optional user-defined name for the validation job, for your own reference.
     */
    private String name;

    /**
     * The unique ID of the Verifalia user who submitted the validation job.
     */
    private String owner;

    /**
     * The IP address of the client which submitted the validation job.
     */
    private String clientIP;

    /**
     * The eventual priority (speed) of the validation job, relative to the parent Verifalia account. In the event of an account
     * with many concurrent validation jobs, this value allows to increase the processing speed of a job with respect to the others.
     * The allowed range of values spans from {@link ValidationPriority#Lowest} (0 - lowest priority) to
     * {@link ValidationPriority#Highest} (255 - highest priority), where the midway value
     * {@link ValidationPriority#Normal} (127) means normal priority; if not specified, Verifalia processes all the
     * concurrent validation jobs for an account using the same priority.
     */
    @JsonDeserialize(using = ValidationPriorityDeserializer.class)
    private ValidationPriority priority;

    /**
     * A reference to the quality level this job was validated against.
     */
    @JsonDeserialize(using = QualityLevelNameDeserializer.class)
    private QualityLevelName quality;

    /**
     * A {@link DeduplicationMode} option which affected the way Verifalia eventually marked entries as duplicates upon
     * processing.
     */
    private DeduplicationMode deduplication;

    /**
     * The number of entries the validation job contains.
     */
    private Integer noOfEntries;

    /**
     * The eventual completion progress for the validation job.
     */
    private ValidationProgress progress;

    /**
     * The maximum data retention period Verifalia observes for this verification job, after which the job will be
     * automatically deleted.
     * A verification job can be deleted anytime prior to its retention period through the
     * {@link com.verifalia.api.emailvalidations.EmailValidationsRestClient#delete(String)} method.
     */
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration retention;

    /**
     * The date and time this validation job has been submitted to Verifalia.
     */
    private Date submittedOn;

    /**
     * The date and time the validation job was created.
     */
    private Date createdOn;

    /**
     * The date and time this validation job has been eventually completed.
     */
    private Date completedOn;
}
