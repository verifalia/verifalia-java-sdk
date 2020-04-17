package com.verifalia.api.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ListingOptions {
    private Integer limit;

    @Builder.Default
    private Direction direction = Direction.Forward;

    public void setLimit(Integer limit) {
        if (limit != null && limit < 0) {
            throw new IllegalArgumentException("Limit must be 0 (meaning no limit will be enforced) or greater.");
        }

        this.limit = limit;
    }

    public void setDirection(@NonNull Direction direction) {
        this.direction = direction;
    }
}