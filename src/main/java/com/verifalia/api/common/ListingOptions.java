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

package com.verifalia.api.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Base class for listing operations against the Verifalia API.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class ListingOptions {
    /**
     * The maximum number of items to return with a listing request. The Verifalia API may choose to override the specified
     * limit if it is either too small or too big. Note: a single listing operation may automatically perform different
     * listing requests to the Verifalia API: this value limits the number of items returned by *each* API request, not
     * the overall total number of returned items.
     */
    private Integer limit;

    /**
     * The direction of the listing.
     */
    @Builder.Default
    private Direction direction = Direction.Forward;

    public void setLimit(final Integer limit) {
        if (limit != null && limit < 0) {
            throw new IllegalArgumentException("Limit must be 0 (meaning no limit will be enforced) or greater.");
        }

        this.limit = limit;
    }

    public void setDirection(@NonNull final Direction direction) {
        this.direction = direction;
    }
}