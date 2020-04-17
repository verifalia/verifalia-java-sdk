package com.verifalia.api.common.filters;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class SetInclusionPredicate<T> extends SetFilterPredicate<T> {
    private T[] values;

    public SetInclusionPredicate(@NonNull final T... values) {
        this.values = values;
    }

    @Override
    public FilterPredicateSegment[] serialize(@NonNull final String fieldName) {
        if (values.length > 0) {
            return new FilterPredicateSegment[]{
                    new FilterPredicateSegment(fieldName, StringUtils.join(values, ","))
            };
        }

        return new FilterPredicateSegment[0];
    }
}