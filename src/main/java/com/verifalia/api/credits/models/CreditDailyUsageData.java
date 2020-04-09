package com.verifalia.api.credits.models;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;

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
public class CreditDailyUsageData {

	 /**
     * The date for which credit balance data is given.
     */
	private LocalDate date;

	 /**
     * The number of credit packs (that is, the non-expiring credits).
     */
    private Double creditPacks;

    /**
     * The number of daily credits, where available.
     */
    private Double freeCredits;

    public void setDate(String dateStr){
    	if(!StringUtils.isBlank(dateStr)){
    		this.date = Utils.convertStringToLocalDate(dateStr, Constants.DATE_FORMAT);
    	}
    }
}
