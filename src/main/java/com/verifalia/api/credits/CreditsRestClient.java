package com.verifalia.api.credits;

import com.verifalia.api.common.Direction;
import com.verifalia.api.common.IterableHelper;
import com.verifalia.api.common.ListingCursor;
import com.verifalia.api.common.Utils;
import com.verifalia.api.common.filters.FilterPredicateSegment;
import com.verifalia.api.common.models.ListSegment;
import com.verifalia.api.credits.models.Balance;
import com.verifalia.api.credits.models.DailyUsage;
import com.verifalia.api.credits.models.DailyUsageListingOptions;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Allows to submit and manage credits for the Verifalia service.
 * <p>The functionalities of this type are exposed by way of the {@link com.verifalia.api.VerifaliaRestClient#getCredits}
 * of {@link com.verifalia.api.VerifaliaRestClient VerifaliaRestClient}.
 */
public class CreditsRestClient {
    private final RestClient restClient;

    public CreditsRestClient(@NonNull RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Gets Verifalia API current credit balance information.
     * Makes a GET request to the <b>"/credits/balance"</b> resource.
     *
     * @return CreditBalanceData representing the credit balance data.
     * @throws IOException
     */
    public Balance getBalance() throws VerifaliaException {
        // Make rest request
        RestRequest request = new RestRequest(HttpRequestMethod.GET, "credits/balance");

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        return response.deserialize(Balance.class);
    }

    /**
     * Gets Verifalia API daily usage credit balance information.
     * Makes a GET request to the <b>"/credits/daily-usage"</b> resource.
     *
     * @return List<CreditDailyUsageData> List of objects where each object representing the credit daily usage details for a date.
     * @throws IOException
     */
    public Iterable<DailyUsage> listDailyUsage() throws VerifaliaException {
        return listDailyUsage(null);
    }

    /**
     * Gets Verifalia API daily usage credit balance information for given options defined in the filter object
     * Makes a GET request to the <b>"/credits/daily-usage"</b> resource with the set of filters defined in the input object.
     *
     * @param options Object representing the daily usage filter options possible for the API.
     * @return List<CreditDailyUsageData> List of objects where each object representing the credit daily usage details for a date.
     * @throws IOException
     */
    public Iterable<DailyUsage> listDailyUsage(DailyUsageListingOptions options) throws VerifaliaException {
        return IterableHelper.buildIterator(
                this::listDailyUsageSegmented,
                this::listDailyUsageSegmented,
                options);
    }

    private ListSegment<DailyUsage> listDailyUsageSegmented(DailyUsageListingOptions options) throws VerifaliaException {
        Map<String, String> paramMap = new HashMap<>();

        if (nonNull(options)) {
            if (options.getLimit() != null && options.getLimit() > 0) {
                paramMap.put("limit", (options.getLimit().toString()));
            }

            // Predicates

            if (options.getDateFilter() != null) {
                for (FilterPredicateSegment fragment : options.getDateFilter().serialize("date")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }
        }

        // Build request URI with the param map
        URI requestUri = Utils.getHttpUri(null, null, null, paramMap);

        // Build query entries resource string
        StringBuilder dailyUsageResource = new StringBuilder("credits/daily-usage");

        if (nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())) {
            dailyUsageResource.append(requestUri.toString());
        }

        // Make request object for the rest call
        RestRequest request = new RestRequest(HttpRequestMethod.GET, dailyUsageResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        // Handle pagination with meta details
        return response.deserialize(DailyUsageListSegment.class);
    }

    private DailyUsageListSegment listDailyUsageSegmented(@NonNull ListingCursor cursor) throws VerifaliaException {
        Map<String, String> paramMap = new HashMap<>();

        if (cursor.getDirection() == Direction.Forward) {
            paramMap.put("cursor", cursor.getCursor());
        } else {
            paramMap.put("cursor:prev", cursor.getCursor());
        }

        if (cursor.getLimit() != null && cursor.getLimit() > 0) {
            paramMap.put("limit", (cursor.getLimit().toString()));
        }

        // Build request URI with the param map
        URI requestUri = Utils.getHttpUri(null, null, null, paramMap);

        // Build query entries resource string
        StringBuilder dailyUsageResource = new StringBuilder("credits/daily-usage");

        if (nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())) {
            dailyUsageResource.append(requestUri.toString());
        }

        // Make request object for the rest call
        RestRequest request = new RestRequest(HttpRequestMethod.GET, dailyUsageResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        // Handle pagination with meta details
        return response.deserialize(DailyUsageListSegment.class);
    }

    public static class DailyUsageListSegment extends ListSegment<DailyUsage> {
    }
}