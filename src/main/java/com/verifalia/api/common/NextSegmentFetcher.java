package com.verifalia.api.common;

import com.verifalia.api.common.models.ListSegment;
import com.verifalia.api.exceptions.VerifaliaException;

public interface NextSegmentFetcher<TItem> {
    ListSegment<TItem> fetch(ListingCursor cursor) throws VerifaliaException;
}
