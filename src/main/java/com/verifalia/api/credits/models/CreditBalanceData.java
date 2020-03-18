package com.verifalia.api.credits.models;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a single credit balance details for Verifalia API service.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditBalanceData {

	 /**
     * The date for which credit balance data is given.
     */
	private Date date;

	 /**
     * The number of credit packs (that is, the non-expiring credits).
     */
    private Double creditPacks;

    /**
     * The number of daily credits, where available.
     */
    private Double freeCredits;

    /**
     * A string representing the amount of time before the daily credits expire, where available, expressed in the form hh:mm:ss.
     */
    private String freeCreditsResetIn;
}
