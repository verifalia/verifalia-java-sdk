package com.verifalia.api.emailvalidations.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a single validated entry within an email validation batch.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    public ValidationRequestEntry(String inputData){
    	this.inputData = inputData;
    }
}
