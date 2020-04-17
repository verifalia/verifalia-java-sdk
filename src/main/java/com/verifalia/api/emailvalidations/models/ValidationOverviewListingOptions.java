package com.verifalia.api.emailvalidations.models;

import com.verifalia.api.common.ListingOptions;
import com.verifalia.api.common.filters.DateFilterPredicate;
import com.verifalia.api.common.filters.SetFilterPredicate;
import com.verifalia.api.common.filters.StringEqualityPredicate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Filter and sort object represents the various filters that can be applied when fetching validation jobs.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class ValidationOverviewListingOptions extends ListingOptions {
    private DateFilterPredicate createdOn;

    /**
     * A string representing the Id of the user which you wish to filter the results for; if present, the API will return only the jobs submitted by the specified user.
     */
    private StringEqualityPredicate owner;

    private SetFilterPredicate<ValidationStatus> statuses;

    /**
     * A object representing the required sorting to apply to the listing operation. Can be one of the following values: createdOn, -createdOn
     */
    private ValidationOverviewListingField orderBy;
}
