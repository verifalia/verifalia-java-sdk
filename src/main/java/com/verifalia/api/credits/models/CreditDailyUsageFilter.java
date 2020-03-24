package com.verifalia.api.credits.models;

import java.time.LocalDate;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Filter object for various parameters when making API call to daily usage details.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditDailyUsageFilter {
	/**
	 * A local date for which the data is supposed to be retrieved, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private LocalDate date;

	/**
	 * A local date representing inclusive beginning date of the required period, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private LocalDate dateSince;

	/**
	 * A local date representing the inclusive ending date of the required period, expressed according to ISO 8601 (yyyy-mm-dd).
	 */
	private LocalDate dateUntil;

	public CreditDailyUsageFilter(){
	}

	public CreditDailyUsageFilter(LocalDate date){
		this.date = date;
	}

	public CreditDailyUsageFilter(LocalDate dateSince, LocalDate dateUntil){
		this.dateSince = dateSince;
		this.dateUntil = dateUntil;
	}
}
