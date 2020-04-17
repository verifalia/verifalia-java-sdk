package com.verifalia.api.common.filters;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class FilterPredicateSegment {
    private String key;
    private String value;

    public FilterPredicateSegment(@NonNull final String key, @NonNull final String value) {
        this.setKey(key);
        this.setValue(value);
    }

    public void setKey(@NonNull final String key) {
        this.key = key;
    }

    public void setValue(@NonNull final String value) {
        this.value = value;
    }
}