package com.verifalia.api.emailvalidations.models;

import com.verifalia.api.common.ListingOptions;
import com.verifalia.api.common.filters.SetFilterPredicate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Filter object represents the various filters that can be applied when fetching validation entries.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class ValidationEntryListingOptions extends ListingOptions {
    private SetFilterPredicate<ValidationEntryStatus> statuses;
}
