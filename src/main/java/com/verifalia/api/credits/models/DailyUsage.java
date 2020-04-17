package com.verifalia.api.credits.models;

import com.verifalia.api.common.DateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.time.LocalDate;

/**
 * Represents a single credit balance details for Verifalia API service.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyUsage {

    /**
     * The date for which credit balance data is given.
     */
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate date;

    /**
     * The number of credit packs (that is, the non-expiring credits).
     */
    private Double creditPacks;

    /**
     * The number of daily credits, where available.
     */
    private Double freeCredits;
}
