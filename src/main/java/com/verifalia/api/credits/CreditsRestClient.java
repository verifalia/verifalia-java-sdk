/*
 * Verifalia - Email list cleaning and real-time email verification service
 * https://verifalia.com/
 * support@verifalia.com
 *
 * Copyright (c) 2005-2020 Cobisi Research
 *
 * Cobisi Research
 * Via Prima Strada, 35
 * 35129, Padova
 * Italy - European Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.verifalia.api.credits;

import com.verifalia.api.common.Direction;
import com.verifalia.api.common.iterables.IterableHelper;
import com.verifalia.api.common.ListingCursor;
import com.verifalia.api.common.Utils;
import com.verifalia.api.common.filters.FilterPredicateFragment;
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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Manages credit packs, daily free credits and usage consumption for the Verifalia account.
 */
public class CreditsRestClient {
    private final RestClient restClient;

    /**
     * Internal method used to initialize the object. Do not use this directly: instead, use the {@link com.verifalia.api.VerifaliaRestClient#credits}
     * property of the {@link com.verifalia.api.VerifaliaRestClient}.
     */
    public CreditsRestClient(@NonNull final RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Returns the current credits balance for the Verifalia account.
     * @return Balance representing the current credits balance.
     */
    public Balance getBalance() throws VerifaliaException {
        // Make rest request
        RestRequest request = new RestRequest(HttpRequestMethod.GET, "credits/balance");

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        return response.deserialize(Balance.class);
    }

    /**
     * Lists the daily usages of the credits for the Verifalia account.
     * @return Iterable<DailyUsage> A collection where each item represents the daily usage of credits for a date.
     */
    public Iterable<DailyUsage> listDailyUsages() throws VerifaliaException {
        return listDailyUsages(null);
    }

    /**
     * Lists the daily usages of the credits for the Verifalia account.
     * @return Iterable<DailyUsage> A collection where each item represents the daily usage of credits for a date.
     */
    public Iterable<DailyUsage> listDailyUsages(final DailyUsageListingOptions options) throws VerifaliaException {
        return IterableHelper.buildIterator(
                this::listDailyUsageSegmented,
                this::listDailyUsageSegmented,
                options);
    }

    private ListSegment<DailyUsage> listDailyUsageSegmented(final DailyUsageListingOptions options) throws VerifaliaException {
        Map<String, String> paramMap = new HashMap<>();

        if (nonNull(options)) {
            if (options.getLimit() != null && options.getLimit() > 0) {
                paramMap.put("limit", (options.getLimit().toString()));
            }

            // Predicates

            if (options.getDateFilter() != null) {
                for (FilterPredicateFragment fragment : options.getDateFilter().serialize("date")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }
        }

        // Build request URI with the param map
        URI requestUri = Utils.getHttpUri(paramMap);

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

    private DailyUsageListSegment listDailyUsageSegmented(@NonNull final ListingCursor cursor) throws VerifaliaException {
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
        URI requestUri = Utils.getHttpUri(paramMap);

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