package com.verifalia.api.credits.models;

import com.verifalia.api.common.ListingOptions;
import com.verifalia.api.common.filters.DateFilterPredicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Filter object for making API call to daily usage details.
 */
@Getter
@Setter
@ToString
@SuperBuilder
public class DailyUsageListingOptions extends ListingOptions {
    private DateFilterPredicate dateFilter;
}
