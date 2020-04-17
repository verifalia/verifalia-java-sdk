package com.verifalia.api.common.filters;

public abstract class FilterPredicate {
    public abstract FilterPredicateSegment[] serialize(String fieldName);
}
