package com.verifalia.api.credits;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.verifalia.api.common.Constants;
import com.verifalia.api.credits.models.CreditBalanceData;
import com.verifalia.api.credits.models.CreditDailyUsage;
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
    	RestRequest request = new RestRequest(HttpRequestMethod.GET, Constants.CREDITS_DAILY_USAGE_RESOURCE);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, CreditDailyUsage.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);

        return (CreditDailyUsage)response.getData();
    }

    /**
     * Gets Verifalia API daily usage credit balance information for a given date
     * Makes a GET request to the <b>"/credits/daily-usage?date={DATE_YYYY-MM-DD}"</b> resource.
     * @param date Date in format YYYY-MM-DD for which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @throws IOException
     */
    public CreditDailyUsage dailyUsage(String date) throws IOException {
    	String resource = Constants.CREDITS_DAILY_USAGE_RESOURCE;
    	if(!StringUtils.isBlank(date)){
    		resource += "?" + Constants.CREDITS_DAILY_UAGSE_PARAM_DATE + "=" + date;
    	}

    	RestRequest request = new RestRequest(HttpRequestMethod.GET, resource);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, CreditDailyUsage.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);

        return (CreditDailyUsage)response.getData();
    }

    /**
     * Gets Verifalia API daily usage credit balance information for a given date
     * Makes a GET request to the <b>"/credits/daily-usage?date={DATE_YYYY-MM-DD}"</b> resource.
     * @param dateSince Date in format YYYY-MM-DD from which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @param dateUntil Date in format YYYY-MM-DD till which usage details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @throws IOException
     */
    public CreditDailyUsage dailyUsage(String dateSince, String dateUntil) throws IOException {
    	String resource = Constants.CREDITS_DAILY_USAGE_RESOURCE;
    	if(!StringUtils.isBlank(dateSince) || !StringUtils.isBlank(dateUntil)){
    		resource += "?";
    		if(!StringUtils.isBlank(dateSince)){
    			resource += Constants.CREDITS_DAILY_UAGSE_PARAM_DATE_SINCE + "=" + dateSince;
    		}
    		if(!StringUtils.isBlank(dateUntil)){
    			if(!StringUtils.isBlank(dateSince)){
    				resource += "&";
    			}
    			resource += Constants.CREDITS_DAILY_UAGSE_PARAM_DATE_UNTIL + "=" + dateUntil;
    		}
    	}

    	RestRequest request = new RestRequest(HttpRequestMethod.GET, resource);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, CreditDailyUsage.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);

        return (CreditDailyUsage)response.getData();
    }
}
