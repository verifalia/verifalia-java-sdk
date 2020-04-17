package com.verifalia.api.common;

import com.verifalia.api.common.models.ListSegment;
import com.verifalia.api.exceptions.VerifaliaException;

public interface FirstSegmentFetcher<TItem, TOptions> {
    ListSegment<TItem> fetch(TOptions options) throws VerifaliaException;
}
