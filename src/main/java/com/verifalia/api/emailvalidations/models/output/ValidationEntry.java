package com.verifalia.api.emailvalidations.models.output;

import static java.util.Objects.nonNull;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.verifalia.api.emailvalidations.models.ValidationEntryClassification;
import com.verifalia.api.emailvalidations.models.ValidationEntryStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a single validated entry within an email validation batch.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationEntry {
	 /**
     * A number with the zero-based index of the entry, with respect to the whole job; this value is mostly useful in the event the API consumer requests a filtered entries set.
     */
    private Integer index;

    /**
     * A string with the original input data submitted for validation.
     */
    private String inputData;

    /**
     * Represents the name of the classification for the email validation, which groups related email validation status codes.
     */
    private ValidationEntryClassification classification;

    /**
     * A detailed status information for the validation result.
     */
    private ValidationEntryStatus status;

    /**
     * A string with the eventually recognized email address, without any comment or FWS (folding white space) symbol.
     */
    private String emailAddress;

    /**
     * The local part of the email address, without comments and folding white spaces.
     */
    private String emailAddressLocalPart;

    /**
     * The domain part of the email address, without comments and folding white spaces.
     */
    private String emailAddressDomainPart;

    /**
     * Gets the domain part of the email address, converted to ASCII if needed and with comments and folding
     * white spaces stripped off.
     * <p>The ASCII encoding is performed using the standard <a href="http://en.wikipedia.org/wiki/Punycode">Punycode algorithm</a>.
     */
    private String asciiEmailAddressDomainPart;

    /**
     * A logical value indicating whether the email address has an international domain name or not.
     */
    private Boolean hasInternationalDomainName;

    /**
     * A logical value indicating whether the email address has an international mailbox name or not.
     */
    private Boolean hasInternationalMailboxName;

    /**
     * A logical value indicating whether the email address comes from a disposable email address provider or not.
     */
    private Boolean isDisposableEmailAddress;

    /**
     * A logical value indicating whether the local part of the email address is a well-known role account or not.
     */
    private Boolean isRoleAccount;

    /**
     * A logical value indicating whether the local part of the email address is a free email address or not.
     */
    private Boolean isFreeEmailAddress;

    /**
     * Gets the position of the character in the email address that eventually caused the syntax validation to fail.
     *
     * This property is <b>null</b> when there is not a {@link #isSyntaxFailure}
     */
    private Integer syntaxFailureIndex;

    /**
     * A string with the eventual custom data included with the entry at the job submission time.
     */
    private String custom;

    /**
     * A number with the eventual zero-based index of the entry which appears to be duplicated by this item. Only present when the status property equals to Duplicate.
     */
    private Integer duplicateOf;

    /**
     * The date this entry has been completed.
     */
    private Date completedOn;

    /**
     * Constructs new object.
     */
	protected ValidationEntry() {}

    /**
     * Constructs new object.
     * @param inputData Input data to be validated (typically, email address)
     */
    public ValidationEntry(String inputData) {
    	this.inputData = inputData;
    }

    /**
     * Overrides the default set method for status with custom logic to handle status as Unknown if no valid status is returned from server
     * @param status Validation entry status mapped which needs to be checked
     */
    public void setStatus(ValidationEntryStatus status){
    	if(!nonNull(status)){
    		this.status = ValidationEntryStatus.Unknown;
    	} else {
    		this.status = status;
    	}
    }
}
