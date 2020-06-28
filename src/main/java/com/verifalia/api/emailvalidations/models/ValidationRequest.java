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
public class ValidationRequest extends AbstractValidationRequest {
    /**
     * One or more {@link ValidationEntry} containing with the email addresses to validate, each along with an optional
     * custom state to be passed back upon completion.
     */
    private List<ValidationRequestEntry> entries;

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