package com.verifalia.api.common.filters;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class StringEqualityPredicate extends StringFilterPredicate {
    private String string;

    public StringEqualityPredicate(@NonNull final String string) {
        this.setString(string);
    }

    @Override
    public FilterPredicateSegment[] serialize(@NonNull final String fieldName) {
        return new FilterPredicateSegment[]{
                new FilterPredicateSegment(fieldName, this.getString())
        };
    }

    public void setString(@NonNull final String string) {
        this.string = string;
    }
}
