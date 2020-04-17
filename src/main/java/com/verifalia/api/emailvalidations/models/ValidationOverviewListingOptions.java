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

package com.verifalia.api.emailvalidations.models;

import com.verifalia.api.common.ListingOptions;
import com.verifalia.api.common.filters.DateFilterPredicate;
import com.verifalia.api.common.filters.SetFilterPredicate;
import com.verifalia.api.common.filters.StringEqualityPredicate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Filter and sort object represents the various filters that can be applied when fetching validation jobs.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class ValidationOverviewListingOptions extends ListingOptions {
    private DateFilterPredicate createdOn;

    /**
     * A string representing the Id of the user which you wish to filter the results for; if present, the API will return only the jobs submitted by the specified user.
     */
    private StringEqualityPredicate owner;

    private SetFilterPredicate<ValidationStatus> statuses;

    /**
     * A object representing the required sorting to apply to the listing operation. Can be one of the following values: createdOn, -createdOn
     */
    private ValidationOverviewListingField orderBy;
}
