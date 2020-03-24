package com.verifalia.api.emailvalidations.models;

/**
 * Provides enumerated values that specify the supported statuses of a single email address validation entry.
 */
public enum ValidationEntryStatus {

	/**
     * The at sign symbol (@), used to separate the local part from the domain part of the address, has not been found.
     */
    AtSignNotFound,

    /**
     * A timeout occurred while verifying fake e-mail address rejection for the mail server.
     */
    CatchAllValidationTimeout,

    /**
     * Verification failed because of a socket connection error occurred while querying the DNS server.
     */
    DnsConnectionFailure,

    /**
     * A timeout has occurred while querying the DNS server(s) for records about the email address domain.
     */
    DnsQueryTimeout,

    /**
     * The domain of the email address does not exist.
     */
    DomainDoesNotExist,

    /**
     * The domain of the email address does not have any valid DNS record and couldn't accept messages from another
     * host on the Internet.
     */
    DomainIsMisconfigured,

    /**
     * The domain has a NULL MX (RFC 7505) resource record and can't thus accept email messages
     */
    DomainHasNullMx,

    /**
     * The email address is provided by a well-known disposable email address provider (DEA).
     */
    DomainIsWellKnownDea,

    /**
     * The domain part of the email address is not compliant with the IETF standards.
     */
    DomainPartCompliancyFailure,

    /**
     * An invalid sequence of two adjacent dots has been found.
     */
    DoubleDotSequence,

    /**
     * The email address appears to be duplicated with another entry which appears previously in the same validation job;
     * the duplicateOf property contains the zero-based index of its first occurrence
     */
    Duplicate,

    /**
     * The email address has an invalid total length.
     */
    InvalidAddressLength,

    /**
     * An invalid character has been detected in the provided sequence.
     */
    InvalidCharacterInSequence,

    /**
     * An invalid quoted word with no content has been found.
     */
    InvalidEmptyQuotedWord,

    /**
     * An invalid folding white space (FWS) sequence has been found.
     */
    InvalidFoldingWhiteSpaceSequence,

    /**
     * The local part of the e-mail address has an invalid length.
     */
    InvalidLocalPartLength,

    /**
     * A new word boundary start has been detected at an invalid position.
     */
    InvalidWordBoundaryStart,

    /**
     * The email address is not compliant with the additional syntax rules of the ISP which should eventually manage it.
     */
    IspSpecificSyntaxFailure,

    /**
     * The external mail exchanger rejected the local endpoint, probably because of its own policy rules.
     */
    LocalEndPointRejected,

    /**
     * The external mail exchanger rejected the validation request.
     */
    LocalSenderAddressRejected,

    /**
     * The mailbox for the e-mail address does not exist.
     */
    MailboxDoesNotExist,

    /**
     * The mailbox for the email-address is over quota.
     */
    MailboxHasInsufficientStorage,

    /**
     * The requested mailbox is temporarily unavailable; it could be experiencing technical issues or some other transient problem
     * (could be over quota, for example).
     */
    MailboxTemporarilyUnavailable,

    /**
     * A timeout occurred while verifying the existence of the mailbox.
     */
    MailboxValidationTimeout,

    /**
     * The external mail exchanger hides a honeypot / spam trap.
     */
    MailExchangerIsHoneypot,

    /**
     * The catch-all validation could not be completed correctly, thus the target mail exchanger could be catch-all.
     */
    ServerDoesNotAllowMultipleRecipients,

    /**
     * The external mail exchanger does not support international mailbox names. To support this feature, mail exchangers must comply with
     * <a href="http://www.ietf.org/rfc/rfc5336.txt">RFC 5336</a> and support and announce both the 8BITMIME and the UTF8SMTP protocol extensions.
     */
    ServerDoesNotSupportInternationalMailboxes,

    /**
     * The external mail exchanger accepts fake, non existent, email addresses; therefore the provided email address MAY be inexistent too.
     */
    ServerIsCatchAll,

    /**
     * The mail exchanger is temporarily unavailable.
     */
    ServerTemporaryUnavailable,

    /**
     * A socket connection error occurred while connecting to the mail exchanger which serves the email address domain.
     */
    SmtpConnectionFailure,

    /**
     * A timeout has occurred while connecting to the mail exchanger which serves the email address domain.
     */
    SmtpConnectionTimeout,

    /**
     * The external mail exchanger replied one or more non-standard SMTP lines and caused the SMTP session to be aborted.
     */
    SmtpDialogError,

    /**
     * The email address has been successfully validated.
     */
    Success,

    /**
     * The domain literal mentioned in the domain part is invalid. While Verifalia supports them, domain literals are quite rare nowadays.
     */
    UnacceptableDomainLiteral,

    /**
     * The number of parenthesis used to open comments is not equal to the one used to close them.
     */
    UnbalancedCommentParenthesis,

    /**
     * An unexpected quoted pair sequence has been found within a quoted word.
     */
    UnexpectedQuotedPairSequence,

    /**
     * One or more unhandled exceptions have been thrown during the verification process and something went wrong
     * on the Verifalia side.
     */
    UnhandledException,

    /**
     * A quoted pair within a quoted word is not closed properly.
     */
    UnmatchedQuotedPair
}
