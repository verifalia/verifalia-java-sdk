package com.verifalia.api.emailvalidations.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Represents a snapshot of an email validation batch.
 */
@Getter
@Setter
@ToString
public class Validation {

    /**
     * The overview of the email validation batch.
     */
    private ValidationOverview overview;

    /**
     * List of all the validation entry data object submitted with the request
     */
    private List<ValidationEntry> entries;
}
