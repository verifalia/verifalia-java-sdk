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

import com.verifalia.api.common.serialization.DurationSerializer;
import com.verifalia.api.emailvalidations.serialization.QualityLevelNameSerializer;
import com.verifalia.api.emailvalidations.serialization.ValidationPriorityDeserializer;
import com.verifalia.api.emailvalidations.serialization.ValidationPrioritySerializer;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Represents an email validation request to be submitted against the Verifalia API.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationRequest {
    private static final Integer VALIDATION_INPUT_PRIORITY_MIN_VALUE = 0;
    private static final Integer VALIDATION_INPUT_PRIORITY_MAX_VALUE = 255;

    /**
     * One or more {@link ValidationEntry} containing with the email addresses to validate, each along with an optional
     * custom state to be passed back upon completion.
     */
    private List<ValidationRequestEntry> entries;

    /**
     * A reference to the expected results quality level for this request. Quality levels determine how Verifalia validates
     * email addresses, including whether and how the automatic reprocessing logic occurs (for transient statuses) and the
     * verification timeouts settings.
     * Use one of {@link QualityLevelName#Standard}, {@link QualityLevelName#High} or {@link QualityLevelName#Extreme}
     * values or a custom quality level ID if you have one (custom quality levels are available to premium plans only).
     */
    @JsonSerialize(using = QualityLevelNameSerializer.class)
    private QualityLevelName quality;

    /**
     * The strategy Verifalia follows while determining which email addresses are duplicates, within a multiple items job.
     * Duplicated items (after the first occurrence) will have the {@link ValidationEntryStatus#Duplicate} status.
     */
    private DeduplicationMode deduplication;

    /**
     * The eventual priority (speed) of the validation job, relative to the parent Verifalia account. In the event of an account
     * with many concurrent validation jobs, this value allows to increase the processing speed of a job with respect to the others.
     * The allowed range of values spans from {@link ValidationPriority#Lowest} (0 - lowest priority) to
     * {@link ValidationPriority#Highest} (255 - highest priority), where the midway value
     * {@link ValidationPriority#Normal} (127) means normal priority; if not specified, Verifalia processes all the
     * concurrent validation jobs for an account using the same priority.
     */
    @JsonSerialize(using = ValidationPrioritySerializer.class)
    private ValidationPriority priority;

    /**
     * The maximum data retention period Verifalia observes for this verification job, after which the job will be
     * automatically deleted. A verification job can be deleted anytime prior to its retention period through the
     * {@link com.verifalia.api.emailvalidations.EmailValidationsRestClient#delete(String)} method.
     */
    @JsonSerialize(using = DurationSerializer.class)
    private Duration retention;

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddress The email address to validate.
     */
    public ValidationRequest(@NonNull final String emailAddress) {
        this(Arrays.asList(emailAddress), null, null);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddress The email addresses to validate.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public ValidationRequest(@NonNull final String emailAddress, final QualityLevelName quality) {
        this(Arrays.asList(emailAddress), quality, null);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     */
    public ValidationRequest(@NonNull String[] emailAddresses) {
        this(Arrays.asList(emailAddresses), null, null);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public ValidationRequest(@NonNull final String[] emailAddresses, final QualityLevelName quality) {
        this(Arrays.asList(emailAddresses), quality, null);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public ValidationRequest(@NonNull final String[] emailAddresses, final DeduplicationMode deduplication) {
        this(Arrays.asList(emailAddresses), null, deduplication);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public ValidationRequest(@NonNull final String[] emailAddresses, final QualityLevelName quality, final DeduplicationMode deduplication) {
        this(Arrays.asList(emailAddresses), quality, deduplication);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     */
    public ValidationRequest(@NonNull Iterable<String> emailAddresses) {
        this(emailAddresses, null, null);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public ValidationRequest(@NonNull final Iterable<String> emailAddresses, final QualityLevelName quality) {
        this(emailAddresses, quality, null);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public ValidationRequest(@NonNull final Iterable<String> emailAddresses, final DeduplicationMode deduplication) {
        this(emailAddresses, null, deduplication);
    }

    /**
     * Initializes a {@link ValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param emailAddresses One or more email addresses to validate.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public ValidationRequest(@NonNull final Iterable<String> emailAddresses, final QualityLevelName quality, final DeduplicationMode deduplication) {
        // Populate validation input object
        List<ValidationRequestEntry> entries = new ArrayList<ValidationRequestEntry>();

        for (String emailAddress : emailAddresses) {
            entries.add(new ValidationRequestEntry(StringUtils.defaultString(emailAddress)));
        }

        setEntries(entries);

        if (nonNull(quality)) {
            setQuality(quality);
        }
        if (nonNull(deduplication)) {
            setDeduplication(deduplication);
        }
    }
}