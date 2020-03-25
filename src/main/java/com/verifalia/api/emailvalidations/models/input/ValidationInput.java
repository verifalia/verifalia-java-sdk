package com.verifalia.api.emailvalidations.models.input;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the input object for email validations request
 */
@Getter
@Setter
@ToString
public class ValidationInput {
	/**
	 * Entries for emails that needs to be processed
	 */
	private List<ValidationEntryInput> entries;
}
