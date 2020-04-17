package com.verifalia.api.emailvalidations.models;

import com.verifalia.api.common.DurationDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.time.Duration;

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
    private Double percentage;

    /**
     * The estimated remaining time for completing the email validation job, if available.
     */
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration estimatedTimeRemaining;
}