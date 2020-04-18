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
 * Provides options for a listing of validation jobs.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class ValidationOverviewListingOptions extends ListingOptions {
    /**
     * Allows to filter the resulting list by the creation date of its {@link ValidationOverview} items.
     */
    private DateFilterPredicate createdOn;

    /**
     * Allows to filter the resulting list by the ID of its owner; if present, the API will return only the jobs
     * submitted by the specified user.
     */
    private StringEqualityPredicate owner;

    /**
     * Allows to filter the results by their {@link ValidationStatus}.
     */
    private SetFilterPredicate<ValidationStatus> statuses;

    /**
     * The {@link ValidationOverview} field to order the resulting listing by; can be used used in conjunction with the
     * {@link #direction} property to vary the order of the returned elements.
     */
    private ValidationOverviewListingField orderBy;
}
