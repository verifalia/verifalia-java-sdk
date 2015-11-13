package com.verifalia.api.emailvalidation.models;

import java.util.Date;

/**
 * Represents a single validated entry within an email validation batch.
 */
public class ValidationEntry {
    /**
     * The input string to validate.
     */
    private String inputData;

    /**
     * The email address, without comments and folding white spaces.
     */
    private String emailAddress;

    /**
     * The date this entry has been completed.
     */
    private Date completedOn;

    /**
     * Gets the domain part of the email address, converted to ASCII if needed and with comments and folding
     * white spaces stripped off.
     * <p>The ASCII encoding is performed using the standard <a href="http://en.wikipedia.org/wiki/Punycode">Punycode algorithm</a>.
     */
    private String asciiEmailAddressDomainPart;

    /**
     * The local part of the email address, without comments and folding white spaces.
     */
    private String emailAddressLocalPart;
    
    /**
     * The domain part of the email address, without comments and folding white spaces.
     */
    private String emailAddressDomainPart;

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
     * A detailed status information for the validation result.
     */
    private ValidationEntryStatus status;

    /**
     * Gets the position of the character in the email address that eventually caused the syntax validation to fail.
     *
     * This property is <b>null</b> when there is not a {@link #isSyntaxFailure}
     */
    private Integer syntaxFailureIndex;


    // #region Shortcut Is* properties

    /**
     * A value indicating whether a problem with the fake address rejection validation occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#CatchAllConnectionFailure CatchAllConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#CatchAllValidationTimeout CatchAllValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#ServerIsCatchAll ServerIsCatchAll}</item>
     * </list>
     */
    private Boolean isCatchAllFailure;

    /**
     * a value indicating whether the email address verification succeeded or failed. In the latter case,
     * detailed failure information is available through {@link #status} property value.
     */
    private Boolean isSuccess;

    /**
     * A value indicating whether a timeout occurred while verifying the email address, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#CatchAllValidationTimeout CatchAllValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#DnsQueryTimeout DnsQueryTimeout}</item>
     *     <item>{@link ValidationEntryStatus#HttpConnectionTimeout HttpConnectionTimeout}</item>
     *     <item>{@link ValidationEntryStatus#MailboxValidationTimeout MailboxValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#SmtpConnectionTimeout SmtpConnectionTimeout}</item>
     * </list>
     */
    private Boolean isTimeoutFailure;


    /**
     * A value indicating whether a failure in the network connection occurred while verifying the email address, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#SmtpConnectionFailure SmtpConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#MailboxConnectionFailure MailboxConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#DnsConnectionFailure DnsConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#CatchAllConnectionFailure CatchAllConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#HttpConnectionFailure HttpConnectionFailure}</item>
     * </list>
     */
    private Boolean isNetworkFailure;

    /**
     * A value indicating whether a syntax error in the email address has been found, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#AtSignNotFound AtSignNotFound}</item>
     *     <item>{@link ValidationEntryStatus#DomainPartCompliancyFailure DomainPartCompliancyFailure}</item>
     *     <item>{@link ValidationEntryStatus#DoubleDotSequence DoubleDotSequence}</item>
     *     <item>{@link ValidationEntryStatus#InvalidAddressLength InvalidAddressLength}</item>
     *     <item>{@link ValidationEntryStatus#InvalidCharacterInSequence InvalidCharacterInSequence}</item>
     *     <item>{@link ValidationEntryStatus#InvalidEmptyQuotedWord InvalidEmptyQuotedWord}</item>
     *     <item>{@link ValidationEntryStatus#InvalidFoldingWhiteSpaceSequence InvalidFoldingWhiteSpaceSequence}</item>
     *     <item>{@link ValidationEntryStatus#InvalidLocalPartLength InvalidLocalPartLength}</item>
     *     <item>{@link ValidationEntryStatus#InvalidWordBoundaryStart InvalidWordBoundaryStart}</item>
     *     <item>{@link ValidationEntryStatus#UnbalancedCommentParenthesis UnbalancedCommentParenthesis}</item>
     *     <item>{@link ValidationEntryStatus#UnexpectedQuotedPairSequence UnexpectedQuotedPairSequence}</item>
     *     <item>{@link ValidationEntryStatus#UnmatchedQuotedPair UnmatchedQuotedPair}</item>
     * </list>
     */
    private Boolean isSyntaxFailure;

    /**
     * A value indicating whether a DNS-related issue occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#DnsQueryTimeout DnsQueryTimeout}</item>
     *     <item>{@link ValidationEntryStatus#DomainDoesNotExist DomainDoesNotExist}</item>
     *     <item>{@link ValidationEntryStatus#DnsConnectionFailure DnsConnectionFailure}</item>
     * </list>
     */
    private Boolean isDnsFailure;

    /**
     * Gets a value indicating whether a problem with the SMTP validation of the email address occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#SmtpConnectionFailure SmtpConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#SmtpConnectionTimeout SmtpConnectionTimeout}</item>
     * </list>
     */
    private Boolean isSmtpFailure;

    /**
     * A value indicating whether a problem with the mailbox validation of the email address occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#MailboxConnectionFailure MailboxConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#MailboxDoesNotExist MailboxDoesNotExist}</item>
     *     <item>{@link ValidationEntryStatus#MailboxTemporarilyUnavailable MailboxTemporarilyUnavailable}</item>
     *     <item>{@link ValidationEntryStatus#MailboxValidationTimeout MailboxValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#ServerDoesNotSupportInternationalMailboxes ServerDoesNotSupportInternationalMailboxes}</item>
     * </list>
     */
    private Boolean isMailboxFailure;

    //#endregion
    
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
     * Returns input string to validate.
     */
	public String getInputData() {
		return inputData;
	}

    /**
     * Returns email address, without comments and folding white spaces.
     */
	public String getEmailAddress() {
		return emailAddress;
	}

    /**
     * Returns date this entry has been completed.
     */
	public Date getCompletedOn() {
		return completedOn;
	}

    /**
     * Returns the domain part of the email address, converted to ASCII if needed and with comments and folding
     * white spaces stripped off.
     * <p>The ASCII encoding is performed using the standard <a href="http://en.wikipedia.org/wiki/Punycode">Punycode algorithm</a>.
     */
	public String getAsciiEmailAddressDomainPart() {
		return asciiEmailAddressDomainPart;
	}

    /**
     * Returns the local part of the email address, without comments and folding white spaces.
     */
	public String getEmailAddressLocalPart() {
		return emailAddressLocalPart;
	}

    /**
     * Returns the domain part of the email address, without comments and folding white spaces.
     */
	public String getEmailAddressDomainPart() {
		return emailAddressDomainPart;
	}

    /**
     * Returns a logical value indicating whether the email address has an international mailbox name or not.
     */
	public Boolean getHasInternationalDomainName() {
		return hasInternationalDomainName;
	}

    /**
     * Returns logical value indicating whether the email address has an international mailbox name or not.
     */
	public Boolean getHasInternationalMailboxName() {
		return hasInternationalMailboxName;
	}

    /**
     * Returns logical value indicating whether the email address comes from a disposable email address provider or not.
     */
	public Boolean getIsDisposableEmailAddress() {
		return isDisposableEmailAddress;
	}

    /**
     * Returns logical value indicating whether the local part of the email address is a well-known role account or not.
     */
	public Boolean getIsRoleAccount() {
		return isRoleAccount;
	}

    /**
     * Returns detailed status information for the validation result.
     */
	public ValidationEntryStatus getStatus() {
		return status;
	}

    /**
     * Returns the position of the character in the email address that eventually caused the syntax validation to fail.
     *
     * @return This property is <b>null</b> when there is not a {@link #isSyntaxFailure}
     */
	public Integer getSyntaxFailureIndex() {
		return syntaxFailureIndex;
	}

    /**
     * Returns a value indicating whether a problem with the fake address rejection validation occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#CatchAllConnectionFailure CatchAllConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#CatchAllValidationTimeout CatchAllValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#ServerIsCatchAll ServerIsCatchAll}</item>
     * </list>
     */
	public Boolean getIsCatchAllFailure() {
		return isCatchAllFailure;
	}

    /**
     * Returns value indicating whether the email address verification succeeded or failed. In the latter case,
     * detailed failure information is available through {@link #status} property value.
     */
	public Boolean getIsSuccess() {
		return isSuccess;
	}

    /**
     * Returns a value indicating whether a timeout occurred while verifying the email address, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#CatchAllValidationTimeout CatchAllValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#DnsQueryTimeout DnsQueryTimeout}</item>
     *     <item>{@link ValidationEntryStatus#HttpConnectionTimeout HttpConnectionTimeout}</item>
     *     <item>{@link ValidationEntryStatus#MailboxValidationTimeout MailboxValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#SmtpConnectionTimeout SmtpConnectionTimeout}</item>
     * </list>
     */
	public Boolean getIsTimeoutFailure() {
		return isTimeoutFailure;
	}

    /**
     * Returns a value indicating whether a failure in the network connection occurred while verifying the email address, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#SmtpConnectionFailure SmtpConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#MailboxConnectionFailure MailboxConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#DnsConnectionFailure DnsConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#CatchAllConnectionFailure CatchAllConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#HttpConnectionFailure HttpConnectionFailure}</item>
     * </list>
     */
	public Boolean getIsNetworkFailure() {
		return isNetworkFailure;
	}

    /**
     * Returns a value indicating whether a syntax error in the email address has been found, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#AtSignNotFound AtSignNotFound}</item>
     *     <item>{@link ValidationEntryStatus#DomainPartCompliancyFailure DomainPartCompliancyFailure}</item>
     *     <item>{@link ValidationEntryStatus#DoubleDotSequence DoubleDotSequence}</item>
     *     <item>{@link ValidationEntryStatus#InvalidAddressLength InvalidAddressLength}</item>
     *     <item>{@link ValidationEntryStatus#InvalidCharacterInSequence InvalidCharacterInSequence}</item>
     *     <item>{@link ValidationEntryStatus#InvalidEmptyQuotedWord InvalidEmptyQuotedWord}</item>
     *     <item>{@link ValidationEntryStatus#InvalidFoldingWhiteSpaceSequence InvalidFoldingWhiteSpaceSequence}</item>
     *     <item>{@link ValidationEntryStatus#InvalidLocalPartLength InvalidLocalPartLength}</item>
     *     <item>{@link ValidationEntryStatus#InvalidWordBoundaryStart InvalidWordBoundaryStart}</item>
     *     <item>{@link ValidationEntryStatus#UnbalancedCommentParenthesis UnbalancedCommentParenthesis}</item>
     *     <item>{@link ValidationEntryStatus#UnexpectedQuotedPairSequence UnexpectedQuotedPairSequence}</item>
     *     <item>{@link ValidationEntryStatus#UnmatchedQuotedPair UnmatchedQuotedPair}</item>
     * </list>
     */
	public Boolean getIsSyntaxFailure() {
		return isSyntaxFailure;
	}

    /**
     * Returns a value indicating whether a DNS-related issue occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#DnsQueryTimeout DnsQueryTimeout}</item>
     *     <item>{@link ValidationEntryStatus#DomainDoesNotExist DomainDoesNotExist}</item>
     *     <item>{@link ValidationEntryStatus#DnsConnectionFailure DnsConnectionFailure}</item>
     * </list>
     */
	public Boolean getIsDnsFailure() {
		return isDnsFailure;
	}

    /**
     * Returns a value indicating whether a problem with the SMTP validation of the email address occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#SmtpConnectionFailure SmtpConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#SmtpConnectionTimeout SmtpConnectionTimeout}</item>
     * </list>
     */
	public Boolean getIsSmtpFailure() {
		return isSmtpFailure;
	}

    /**
     * Returns a value indicating whether a problem with the mailbox validation of the email address occurred, including:
     * <list type="bullet">
     *     <item>{@link ValidationEntryStatus#MailboxConnectionFailure MailboxConnectionFailure}</item>
     *     <item>{@link ValidationEntryStatus#MailboxDoesNotExist MailboxDoesNotExist}</item>
     *     <item>{@link ValidationEntryStatus#MailboxTemporarilyUnavailable MailboxTemporarilyUnavailable}</item>
     *     <item>{@link ValidationEntryStatus#MailboxValidationTimeout MailboxValidationTimeout}</item>
     *     <item>{@link ValidationEntryStatus#ServerDoesNotSupportInternationalMailboxes ServerDoesNotSupportInternationalMailboxes}</item>
     * </list>
     */
	public Boolean getIsMailboxFailure() {
		return isMailboxFailure;
	}

	/** 
	 * Converts this object into human-readable string representation.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{\n");
		
		if(inputData != null)
			sb.append("\tinputData: ").append(inputData).append('\n');
		
		if(emailAddress != null)
			sb.append("\temailAddress: ").append(emailAddress).append('\n');

		if(completedOn != null)
			sb.append("\tcompletedOn: ").append(completedOn).append('\n');

		if(asciiEmailAddressDomainPart != null)
			sb.append("\tasciiEmailAddressDomainPart: ").append(asciiEmailAddressDomainPart).append('\n');

		if(emailAddressLocalPart != null)
			sb.append("\temailAddressLocalPart: ").append(emailAddressLocalPart).append('\n');

		if(emailAddressDomainPart != null)
			sb.append("\temailAddressDomainPart: ").append(emailAddressDomainPart).append('\n');

		if(hasInternationalDomainName != null)
			sb.append("\thasInternationalDomainName: ").append(hasInternationalDomainName).append('\n');

		if(hasInternationalDomainName != null)
			sb.append("\thasInternationalDomainName: ").append(hasInternationalDomainName).append('\n');

		if(hasInternationalMailboxName != null)
			sb.append("\thasInternationalMailboxName: ").append(hasInternationalMailboxName).append('\n');

		if(isDisposableEmailAddress != null)
			sb.append("\tisDisposableEmailAddress: ").append(isDisposableEmailAddress).append('\n');

		if(isRoleAccount != null)
			sb.append("\tisRoleAccount: ").append(isRoleAccount).append('\n');

		if(status != null)
			sb.append("\tstatus: ").append(status.name()).append('\n');

		if(syntaxFailureIndex != null)
			sb.append("\tsyntaxFailureIndex: ").append(syntaxFailureIndex).append('\n');

		if(isCatchAllFailure != null)
			sb.append("\tisCatchAllFailure: ").append(isCatchAllFailure).append('\n');

		if(isSuccess != null)
			sb.append("\tisSuccess: ").append(isSuccess).append('\n');

		if(isTimeoutFailure != null)
			sb.append("\tisTimeoutFailure: ").append(isTimeoutFailure).append('\n');

		if(isNetworkFailure != null)
			sb.append("\tisNetworkFailure: ").append(isNetworkFailure).append('\n');

		if(isSyntaxFailure != null)
			sb.append("\tisSyntaxFailure:").append(isSyntaxFailure).append('\n');

		if(isDnsFailure != null)
			sb.append("\tisDnsFailure: ").append(isDnsFailure).append('\n');

		if(isSmtpFailure != null)
			sb.append("\tisSmtpFailure: ").append(isSmtpFailure).append('\n');

		if(isMailboxFailure != null)
			sb.append("\tisMailboxFailure: ").append(isMailboxFailure).append('\n');

		sb.append("}\n");
		
		return sb.toString();
	}
	
}
