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

import com.verifalia.api.common.models.ListSegment;
import com.verifalia.api.exceptions.VerifaliaException;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IterableHelper {
    public static <TItem, TOptions extends ListingOptions> Iterable<TItem> buildIterator(FirstSegmentFetcher<TItem, TOptions> firstSegmentFetcher,
                                                                                         NextSegmentFetcher<TItem> nextSegmentFetcher,
                                                                                         TOptions options) throws VerifaliaException {

        ListSegment<TItem> firstSegment = firstSegmentFetcher.fetch(options);
        ItemIterator<TItem, TOptions> iterator = new ItemIterator<>(firstSegment, nextSegmentFetcher, options);

        return () -> iterator;
    }

    private static class ItemIterator<TItem, TOptions extends ListingOptions> implements Iterator {
        private final NextSegmentFetcher<TItem> nextSegmentFetcher;
        private final TOptions options;
        private ListSegment<TItem> segment;
        private int consumedInSegment = 0;

        public ItemIterator(@NonNull ListSegment<TItem> segment, @NonNull NextSegmentFetcher<TItem> nextSegmentFetcher, TOptions options) {
            this.segment = segment;
            this.nextSegmentFetcher = nextSegmentFetcher;
            this.options = options;
        }

        @SneakyThrows
        @Override
        public boolean hasNext() {
            fetchNextSegmentIfNeeded();

            return segment != null && (consumedInSegment < segment.getData().size());
        }

        @SneakyThrows
        @Override
        public Object next() {
            fetchNextSegmentIfNeeded();

            if (segment == null) {
                throw new NoSuchElementException();
            }

            return segment.getData().get(consumedInSegment++);
        }

        private void fetchNextSegmentIfNeeded() throws VerifaliaException {
            if (consumedInSegment >= segment.getData().size()) {
                consumedInSegment = 0;

                if (segment.getMeta() != null && segment.getMeta().getIsTruncated()) {
                    ListingCursor cursor = new ListingCursor();

                    cursor.setCursor(segment.getMeta().getCursor());

                    if (options != null) {
                        cursor.setDirection(options.getDirection());
                        cursor.setLimit(options.getLimit());
                    }

                    segment = nextSegmentFetcher.fetch(cursor);
                } else {
                    segment = null;
                }
            }
        }
    }
}
