package com.verifalia.api.credits.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.verifalia.api.common.models.ResponseMeta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a single credit daily usage details for Verifalia API service
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditDailyUsage {
	 /**
     * Meta information related to the response
     */
    private ResponseMeta meta;

    /**
     * Credit balance data for indiviual dates
     */
    private List<CreditBalanceData> data;
}
