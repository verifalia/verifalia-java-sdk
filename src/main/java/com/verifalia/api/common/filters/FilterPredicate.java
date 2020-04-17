package com.verifalia.api.common.filters;

import lombok.NonNull;

public abstract class FilterPredicate {
    public abstract FilterPredicateSegment[] serialize(@NonNull final String fieldName);
}
