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
        private ListSegment<TItem> segment;
        private final NextSegmentFetcher<TItem> nextSegmentFetcher;
        private final TOptions options;
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
