package com.verifalia.api.credits.models;

import com.verifalia.api.common.DurationDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.time.Duration;

/**
 * Represents a single credit balance details for Verifalia API service.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    /**
     * The number of credit packs (that is, the non-expiring credits).
     */
    private Double creditPacks;

    /**
     * The number of daily credits, where available.
     */
    private Double freeCredits;

    /**
     * The amount of time before the daily credits expire, where available.
     */
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration freeCreditsResetIn;
}
