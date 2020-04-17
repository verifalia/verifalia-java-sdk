package com.verifalia.api.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListingCursor extends ListingOptions {
    private String cursor;
}