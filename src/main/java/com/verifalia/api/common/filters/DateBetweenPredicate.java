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

package com.verifalia.api.common.filters;

import com.verifalia.api.common.Utils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A filter predicate used to filter dates between two optional values.
 */
@Getter
@Setter
public class DateBetweenPredicate extends DateFilterPredicate {
    /**
     * The minimum date to be included in the filter.
     */
    private LocalDate since;

    /**
     * The maximum date to be included in the filter.
     */
    private LocalDate until;

    /**
     * Initializes a filter predicate used to filter dates between two optional values.
     *
     * @param since The minimum date to be included in the filter.
     * @param until The maximum date to be included in the filter.
     */
    public DateBetweenPredicate(final LocalDate since, final LocalDate until) {
        if (since == null && until == null) {
            throw new IllegalArgumentException("Both since and until are null.");
        }

        if (since != null && until != null) {
            if (since.isAfter(until)) {
                throw new IllegalArgumentException("Invalid predicate: since is after until.");
            }
        }

        this.setSince(since);
        this.setUntil(until);
    }

    @Override
    public FilterPredicateFragment[] serialize(@NonNull final String fieldName) {
        ArrayList<FilterPredicateFragment> result = new ArrayList<>();

        if (this.getSince() != null) {
            result.add(new FilterPredicateFragment(fieldName + ":since", Utils.convertLocalDateToString(this.getSince(), "yyyy-MM-dd")));
        }
        if (this.getUntil() != null) {
            result.add(new FilterPredicateFragment(fieldName + ":until", Utils.convertLocalDateToString(this.getUntil(), "yyyy-MM-dd")));
        }

        return result.toArray(new FilterPredicateFragment[result.size()]);
    }
}