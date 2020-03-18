package com.verifalia.api.credits;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import com.verifalia.api.credits.models.CreditBalanceData;
import com.verifalia.api.credits.models.CreditDailyUsage;
import com.verifalia.api.credits.models.CreditDailyUsageFilter;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.HttpStatusCode;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;

/**
 * Allows to submit and manage credits for the Verifalia service.
 * <p>The functionalities of this type are exposed by way of the {@link com.verifalia.api.VerifaliaRestClient#getEmailValidations getmailValidations()}
 * of {@link com.verifalia.api.VerifaliaRestClient VerifaliaRestClient}.
 */
public class CreditsRestClient {
    private RestClient restClient;

    public CreditsRestClient(RestClient restClient) {
        if (restClient == null)
            throw new IllegalArgumentException("restClient is null.");

        this.restClient = restClient;
    }

    /**
     * Gets Verifalia API current credit balance information.
     * Makes a GET request to the <b>"/credits/balance"</b> resource.
     * @throws IOException
     */
    public CreditBalanceData balance() throws IOException {
    	// Make rest request
    	RestRequest request = new RestRequest(HttpRequestMethod.GET, Constants.CREDITS_BALANCE_RESOURCE);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, CreditBalanceData.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);

        return (CreditBalanceData)response.getData();
    }

    /**
     * Gets Verifalia API daily usage credit balance information.
     * Makes a GET request to the <b>"/credits/daily-usage"</b> resource.
     * @throws IOException
     */
    public CreditDailyUsage dailyUsage() throws IOException {
    	CreditDailyUsageFilter creditDailyUsageFilter = null;
    	return dailyUsage(creditDailyUsageFilter);
    }

    /**
     * Gets Verifalia API daily usage credit balance information for a given date
     * Makes a GET request to the <b>"/credits/daily-usage?date={DATE_YYYY-MM-DD}"</b> resource.
     * @param date Date in format YYYY-MM-DD for which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @throws IOException
     */
    public CreditDailyUsage dailyUsage(String date) throws IOException {
    	CreditDailyUsageFilter creditDailyUsageFilter = new CreditDailyUsageFilter(date);
    	return dailyUsage(creditDailyUsageFilter);
    }

    /**
     * Gets Verifalia API daily usage credit balance information for a given date
     * Makes a GET request to the <b>"/credits/daily-usage?date={DATE_YYYY-MM-DD}"</b> resource.
     * @param dateSince Date in format YYYY-MM-DD from which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @param dateUntil Date in format YYYY-MM-DD till which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @throws IOException
     */
    public CreditDailyUsage dailyUsage(String dateSince, String dateUntil) throws IOException {
    	CreditDailyUsageFilter creditDailyUsageFilter = new CreditDailyUsageFilter(dateSince, dateUntil);
    	return dailyUsage(creditDailyUsageFilter);
    }

    public CreditDailyUsage dailyUsage(CreditDailyUsageFilter creditDailyUsageFilter) throws IOException {
    	// Build query string parameters map
    	Map<String, String> paramMap = getDailyUsageParamMap(creditDailyUsageFilter);

    	// Build request URI with the param map
    	URI requestUri = Utils.getHttpUri(null, null, null, paramMap);

    	// Build query entries resource string
    	StringBuilder dailyUsageResource = new StringBuilder(Constants.CREDITS_DAILY_USAGE_RESOURCE);
    	if(nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())){
    		dailyUsageResource.append(requestUri.toString());
    	}
    	System.out.println("URI: " + dailyUsageResource.toString());

    	// Make request object for the rest call
    	RestRequest request = new RestRequest(HttpRequestMethod.GET, dailyUsageResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, CreditDailyUsage.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);

        return (CreditDailyUsage)response.getData();
    }

    private Map<String, String> getDailyUsageParamMap(CreditDailyUsageFilter creditDailyUsageFilter){
    	Map<String, String> paramMap = new HashMap<String, String>();
    	if(nonNull(creditDailyUsageFilter)){
    		// TODO - Validate daily usage filter
	    	// Date filter
	    	if(!StringUtils.isBlank(creditDailyUsageFilter.getDate())){
	    		paramMap.put("date", creditDailyUsageFilter.getDate());
	    	}
	    	// Date since filter
	    	if(!StringUtils.isBlank(creditDailyUsageFilter.getDateSince())){
	    		paramMap.put("date:since", creditDailyUsageFilter.getDateSince());
	    	}
	    	// Date until filter
	    	if(!StringUtils.isBlank(creditDailyUsageFilter.getDateUntil())){
	    		paramMap.put("date:until", creditDailyUsageFilter.getDateUntil());
	    	}
    	}
    	return paramMap;
    }

}
