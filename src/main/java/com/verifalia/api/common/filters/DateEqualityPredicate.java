package com.verifalia.api.common.filters;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
public class DateEqualityPredicate extends DateFilterPredicate {
    private LocalDate date;

    public DateEqualityPredicate(@NonNull final LocalDate date) {
        this.setLocalDate(date);
    }

    @Override
    public FilterPredicateSegment[] serialize(@NonNull final String fieldName) {
        return new FilterPredicateSegment[]{
                new FilterPredicateSegment(fieldName, Utils.convertLocalDateToString(this.getDate(), Constants.DATE_FORMAT))
        };
    }

    public void setLocalDate(@NonNull final LocalDate date) {
        this.date = date;
    }
}
