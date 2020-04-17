package com.verifalia.api.emailvalidations.models;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.DurationSerializer;
import com.verifalia.api.common.QualityLevelNameSerializer;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Represents the input object for email validations request
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationRequest {
    /**
     * Entries for emails that needs to be processed
     */
    private List<ValidationRequestEntry> entries;
    /**
     * An optional results quality level requested for the job; can be one of Standard, High and Extreme (or other values for custom quality levels).
     */
    @JsonSerialize(using = QualityLevelNameSerializer.class)
    private QualityLevelName quality;
    /**
     * An optional deduplication algorithm requested for the job; can be one of Off, Safe and Relaxed.
     */
    private DeduplicationMode deduplication;
    /**
     * An optional priority for the validation job, relative to the parent Verifalia account. In the event of an account with many concurrent jobs at the same time, this value allows to increase the processing slot time percentage of a job with respect to the others. The allowed range of integer values spans from 0 (lowest priority) to 255 (highest priority), where the midway value 127 means normal priority; if not specified, Verifalia processes the validation job without a specific priority.
     */
    private Integer priority;
    /**
     * An optional desired data retention period to observe for the validation job; Verifalia will delete the job and its data once its data retention period is over, starting to count when it gets completed.
     */
    @JsonSerialize(using = DurationSerializer.class)
    private Duration retention;

    public ValidationRequest(@NonNull final String emailAddress) {
        this(Arrays.asList(emailAddress), null, null);
    }

    public ValidationRequest(@NonNull final String emailAddress, QualityLevelName quality) {
        this(Arrays.asList(emailAddress), quality, null);
    }

    public ValidationRequest(@NonNull String[] emailAddresses) {
        this(Arrays.asList(emailAddresses), null, null);
    }

    public ValidationRequest(@NonNull final String[] emailAddresses, QualityLevelName quality) {
        this(Arrays.asList(emailAddresses), quality, null);
    }

    public ValidationRequest(@NonNull final String[] emailAddresses, DeduplicationMode deduplication) {
        this(Arrays.asList(emailAddresses), null, deduplication);
    }

    public ValidationRequest(@NonNull final String[] emailAddresses, QualityLevelName quality, DeduplicationMode deduplication) {
        this(Arrays.asList(emailAddresses), quality, deduplication);
    }

    public ValidationRequest(@NonNull Iterable<String> emailAddresses) {
        this(emailAddresses, null, null);
    }

    public ValidationRequest(@NonNull final Iterable<String> emailAddresses, QualityLevelName quality) {
        this(emailAddresses, quality, null);
    }

    public ValidationRequest(@NonNull final Iterable<String> emailAddresses, DeduplicationMode deduplication) {
        this(emailAddresses, null, deduplication);
    }

    public ValidationRequest(@NonNull final Iterable<String> emailAddresses, QualityLevelName quality, DeduplicationMode deduplication) {
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

    public void setPriority(Integer value) {
        if (value != null) {
            if (value < Constants.VALIDATION_INPUT_PRIORITY_MIN_VALUE
                    || value > Constants.VALIDATION_INPUT_PRIORITY_MAX_VALUE) {
                throw new IllegalArgumentException("Invalid priority value. It must be in range 0 to 255");
            }
        }

        this.priority = value;
    }

    public static class ValidationRequestBuilder {
        private Integer priority;

        public ValidationRequestBuilder priority(Integer value) {
            if (value != null) {
                if (value < Constants.VALIDATION_INPUT_PRIORITY_MIN_VALUE
                        || value > Constants.VALIDATION_INPUT_PRIORITY_MAX_VALUE) {
                    throw new IllegalArgumentException("Invalid priority value. It must be in range 0 to 255");
                }
            }

            this.priority = value;
            return this;
        }
    }
}