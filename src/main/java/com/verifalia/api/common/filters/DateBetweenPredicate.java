package com.verifalia.api.common.filters;

import com.verifalia.api.common.Constants;
import com.verifalia.api.common.Utils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
public class DateBetweenPredicate extends DateFilterPredicate {
    private LocalDate since;
    private LocalDate until;

    public DateBetweenPredicate(final LocalDate since, final LocalDate until) {
        if (since == null && until == null) {
            throw new IllegalArgumentException("Both since and until are null.");
        }

        if (since != null && until != null) {
            if (since.isAfter(until)) {
                throw new IllegalArgumentException("Invalid predicate: since is after until.");
            }
        }

        this.setSince(since);
        this.setUntil(until);
    }

    @Override
    public FilterPredicateSegment[] serialize(@NonNull final String fieldName) {
        ArrayList<FilterPredicateSegment> result = new ArrayList<>();

        if (this.getSince() != null) {
            result.add(new FilterPredicateSegment(fieldName + ":since", Utils.convertLocalDateToString(this.getSince(), Constants.DATE_FORMAT)));
        }
        if (this.getUntil() != null) {
            result.add(new FilterPredicateSegment(fieldName + ":until", Utils.convertLocalDateToString(this.getUntil(), Constants.DATE_FORMAT)));
        }

        return result.toArray(new FilterPredicateSegment[result.size()]);
    }
}