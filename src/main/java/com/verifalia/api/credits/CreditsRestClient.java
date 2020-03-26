package com.verifalia.api.credits;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import com.verifalia.api.common.models.ResponseMeta;
import com.verifalia.api.credits.models.CreditBalanceData;
import com.verifalia.api.credits.models.CreditDailyUsage;
import com.verifalia.api.credits.models.CreditDailyUsageData;
import com.verifalia.api.credits.models.CreditDailyUsageFilter;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.HttpStatusCode;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;

/**
 * Allows to submit and manage credits for the Verifalia service.
 * <p>The functionalities of this type are exposed by way of the {@link com.verifalia.api.VerifaliaRestClient#getCredits}
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
     * @return CreditBalanceData representing the credit balance data.
     * @throws IOException
     */
    public CreditBalanceData getBalance() throws IOException {
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
     * @return List<CreditDailyUsageData> List of objects where each object representing the credit daily usage details for a date.
     * @throws IOException
     */
    public List<CreditDailyUsageData> getDailyUsage() throws IOException {
    	CreditDailyUsageFilter creditDailyUsageFilter = null;
    	return getDailyUsage(creditDailyUsageFilter);
    }

    /**
     * Gets Verifalia API daily usage credit balance information for a given date
     * Makes a GET request to the <b>"/credits/daily-usage?date={DATE_YYYY-MM-DD}"</b> resource.
     * @param date Local date for which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @return List<CreditDailyUsageData> List of objects where each object representing the credit daily usage details for a date.
     * @throws IOException
     */
    public List<CreditDailyUsageData> getDailyUsage(LocalDate date) throws IOException {
    	CreditDailyUsageFilter creditDailyUsageFilter = new CreditDailyUsageFilter(date);
    	return getDailyUsage(creditDailyUsageFilter);
    }

    /**
     * Gets Verifalia API daily usage credit balance information for a given date range
     * Makes a GET request to the <b>"/credits/daily-usage?date:since={DATE_YYYY-MM-DD}&date:until={DATE_YYYY-MM-DD}"</b> resource.
     * @param dateSince Local date from which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @param dateUntil Local date till which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @return List<CreditDailyUsageData> List of objects where each object representing the credit daily usage details for a date.
     * @throws IOException
     */
    public List<CreditDailyUsageData> getDailyUsage(LocalDate dateSince, LocalDate dateUntil) throws IOException {
    	CreditDailyUsageFilter creditDailyUsageFilter = new CreditDailyUsageFilter(dateSince, dateUntil);
    	return getDailyUsage(creditDailyUsageFilter);
    }

    /**
     * Gets Verifalia API daily usage credit balance information for given options defined in the filter object
     * Makes a GET request to the <b>"/credits/daily-usage"</b> resource with the set of filters defined in the input object.
     * @param creditDailyUsageFilter Object representing the daily usage filter options possible for the API.
     * @return List<CreditDailyUsageData> List of objects where each object representing the credit daily usage details for a date.
     * @throws IOException
     */
    public List<CreditDailyUsageData> getDailyUsage(CreditDailyUsageFilter creditDailyUsageFilter) throws IOException {
    	// Assign with default values to handle pagination
    	String cursor = StringUtils.EMPTY;
    	Boolean isTruncated = Boolean.TRUE;
    	List<CreditDailyUsageData> creditDailyUsagesData = new ArrayList<CreditDailyUsageData>();

    	// Run through responses to handle pagination
    	while(isTruncated){
	    	// Build query string parameters map
	    	Map<String, String> paramMap = getDailyUsageParamMap(creditDailyUsageFilter, cursor);

	    	// Build request URI with the param map
	    	URI requestUri = Utils.getHttpUri(null, null, null, paramMap);

	    	// Build query entries resource string
	    	StringBuilder dailyUsageResource = new StringBuilder(Constants.CREDITS_DAILY_USAGE_RESOURCE);
	    	if(nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())){
	    		dailyUsageResource.append(requestUri.toString());
	    	}
	    	System.out.println("Daily usage URI: " + dailyUsageResource.toString());

	    	// Make request object for the rest call
	    	RestRequest request = new RestRequest(HttpRequestMethod.GET, dailyUsageResource.toString());

	        // Sends the request to the Verifalia servers
	        RestResponse response = restClient.execute(request, CreditDailyUsage.class);

	        if(response.getStatusCode() != HttpStatusCode.OK)
	        	throw new VerifaliaException(response);

	        // Handle pagination with meta details
	        CreditDailyUsage creditDailyUsage = ((CreditDailyUsage)response.getData());
	        creditDailyUsagesData.addAll(creditDailyUsage.getData());
	        ResponseMeta meta = creditDailyUsage.getMeta();
	        isTruncated = meta.getIsTruncated();
	        cursor = meta.getCursor();
    	}

        return creditDailyUsagesData;
    }

    private Map<String, String> getDailyUsageParamMap(CreditDailyUsageFilter creditDailyUsageFilter, String cursor){
    	Map<String, String> paramMap = new HashMap<String, String>();

    	// Add cursor as param for handling pagination. If cursor is passed, no need to pass other params as per the documentation.
    	if(!StringUtils.isBlank(cursor)){
			paramMap.put(Constants.API_PARAM_CURSOR, cursor);
		} else {
	    	if(nonNull(creditDailyUsageFilter)){
	    		if(validateDailyUsageFilterInputs(creditDailyUsageFilter)){ // Validate the daily usage request filter
			    	// Date filter
			    	if(nonNull(creditDailyUsageFilter.getDate())){
			    		paramMap.put("date", Utils.convertLocalDateToString(creditDailyUsageFilter.getDate(),
			    				Constants.DATE_FORMAT));
			    	}
			    	// Date since filter
			    	if(nonNull(creditDailyUsageFilter.getDateSince())){
			    		paramMap.put("date:since", Utils.convertLocalDateToString(creditDailyUsageFilter.getDateSince(),
			    				Constants.DATE_FORMAT));
			    	}
			    	// Date until filter
			    	if(nonNull(creditDailyUsageFilter.getDateUntil())){
			    		paramMap.put("date:until", Utils.convertLocalDateToString(creditDailyUsageFilter.getDateUntil(),
			    				Constants.DATE_FORMAT));
			    	}
	    		}
	    	}
		}
    	return paramMap;
    }

    private boolean validateDailyUsageFilterInputs(CreditDailyUsageFilter creditDailyUsageFilter){
    	// Validation related to dates
    	if(nonNull(creditDailyUsageFilter.getDate())
    			&& (nonNull(creditDailyUsageFilter.getDateSince()) || nonNull(creditDailyUsageFilter.getDateUntil()))){
    		throw new IllegalArgumentException("One cannot have both date and date since or date until or both when making request");
    	}
    	// Validation related to dates
    	if(nonNull(creditDailyUsageFilter.getDateSince())
    			&& nonNull(creditDailyUsageFilter.getDateUntil())){
    		if(creditDailyUsageFilter.getDateUntil().isBefore(creditDailyUsageFilter.getDateSince())){
    			throw new IllegalArgumentException("One cannot have created until date before created on date");
    		}
    	}
    	return true;
    }

}
