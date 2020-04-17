package com.verifalia.api.emailvalidations.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Represents a single validated entry within an email validation batch.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationRequestEntry {

    /**
     * A string with the original input data submitted for validation.
     */
    private String inputData;

    /**
     * A string with the eventual custom data included with the entry at the job submission time.
     */
    private String custom;

    public ValidationRequestEntry(@NonNull final String inputData) {
        this.inputData = inputData;
    }

    public ValidationRequestEntry(@NonNull final String inputData, final String custom) {
        this.inputData = inputData;
        this.custom = custom;
    }

    public void setInputData(@NonNull final String inputData) {
        this.inputData = inputData;
    }
}
