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

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
public class DateBetweenPredicate extends DateFilterPredicate {
    private LocalDate since;
    private LocalDate until;

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
    public FilterPredicateSegment[] serialize(@NonNull final String fieldName) {
        ArrayList<FilterPredicateSegment> result = new ArrayList<>();

        if (this.getSince() != null) {
            result.add(new FilterPredicateSegment(fieldName + ":since", Utils.convertLocalDateToString(this.getSince(), Constants.DATE_FORMAT)));
        }
        if (this.getUntil() != null) {
            result.add(new FilterPredicateSegment(fieldName + ":until", Utils.convertLocalDateToString(this.getUntil(), Constants.DATE_FORMAT)));
        }

        return result.toArray(new FilterPredicateSegment[result.size()]);
    }
}