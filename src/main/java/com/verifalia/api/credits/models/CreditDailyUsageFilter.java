package com.verifalia.api.credits.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Filter object for fetching credit daily usage
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditDailyUsageFilter {
	/**
	 * A string representing the required date, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private String date;

	/**
	 * A string representing the inclusive beginning date of the required period, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private String dateSince;

	/**
	 * A string representing the inclusive ending date of the required period, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private String dateUntil;

	public CreditDailyUsageFilter(){
	}

	public CreditDailyUsageFilter(String date){
		this.date = date;
	}

	public CreditDailyUsageFilter(String dateSince, String dateUntil){
		this.dateSince = dateSince;
		this.dateUntil = dateUntil;
	}
}
