package com.verifalia.api.emailvalidations.models;

import java.util.List;

import com.verifalia.api.common.models.ResponseMeta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a snapshot of an email validation batch.
 */
@Getter
@Setter
@ToString
public class ValidationEntries {

	/**
     * Meta information for the validation entries
     */
	private ResponseMeta meta;

	/**
     * List of all the validation entry data object submitted with the request
     */
	private List<ValidationEntryData> data;
}
