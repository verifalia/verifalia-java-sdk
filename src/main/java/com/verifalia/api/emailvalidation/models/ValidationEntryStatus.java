package com.verifalia.api.emailvalidation.models;

/**
 * Provides enumerated values that specify the supported statuses of a single email address validation entry.
 */
public enum ValidationEntryStatus {
    /**
     * Unknown validation status, due to a value reported by the API which is missing in this SDK.
     */
    Unknown,

    /**
     * The email address has been successfully validated.
     */
    Success,

    /**
     * A quoted pair within a quoted word is not closed properly.
     */
    UnmatchedQuotedPair,

    /**
     * An unexpected quoted pair sequence has been found within a quoted word.
     */
    UnexpectedQuotedPairSequence,

    /**
     * A new word boundary start has been detected at an invalid position.
     */
    InvalidWordBoundaryStart,

    /**
     * An invalid character has been detected in the provided sequence.
     */
    InvalidCharacterInSequence,

    /**
     * The number of parenthesis used to open comments is not equal to the one used to close them.
     */
    UnbalancedCommentParenthesis,

    /**
     * An invalid sequence of two adjacent dots has been found.
     */
    DoubleDotSequence,

    /**
     * The local part of the e-mail address has an invalid length.
     */
    InvalidLocalPartLength,

    /**
     * An invalid folding white space (FWS) sequence has been found.
     */
    InvalidFoldingWhiteSpaceSequence,

    /**
     * The at sign symbol (@), used to separate the local part from the domain part of the address, has not been found.
     */
    AtSignNotFound,

    /**
     * An invalid quoted word with no content has been found.
     */
    InvalidEmptyQuotedWord,

    /**
     * The email address has an invalid total length.
     */
    InvalidAddressLength,

    /**
     * The domain part of the email address is not compliant with the IETF standards.
     */
    DomainPartCompliancyFailure,

    /**
     * The email address is not compliant with the additional syntax rules of the ISP which should eventually manage it.
     */
    IspSpecificSyntaxFailure,

    /**
     * The local part of the email address is a well-known role account.
     */
    LocalPartIsWellKnownRoleAccount,

    // #region DNS failures

    /**
     * A timeout has occurred while querying the DNS server(s) for records about the email address domain.
     */
    DnsQueryTimeout,

    /**
     * Verification failed because of a socket connection error occurred while querying the DNS server.
     */
    DnsConnectionFailure,

    /**
     * The domain of the email address does not exist.
     */
    DomainDoesNotExist,

    /**
     * The domain of the email address does not have any valid DNS record and couldn't accept messages from another
     * host on the Internet.
     */
    DomainIsMisconfigured,

    // #endregion

    // #region Well-known disposable email address failures

    /**
     * The email address is provided by a well-known disposable email address provider (DEA).
     */
    DomainIsWellKnownDea,

    /**
     * The mail exchanger being tested is a well-known disposable email address provider (DEA).
     */
    MailExchangerIsWellKnownDea,

    /**
     * While both the domain and the mail exchanger for the email address being tested are not from a well-known
     * disposable email address provider (DEA), the mailbox is actually disposable.
     */
    MailboxIsDea,

    // #endregion

    // #region SMTP failures

    /**
     * A timeout has occurred while connecting to the mail exchanger which serves the email address domain.
     */
    SmtpConnectionTimeout,

    /**
     * A socket connection error occurred while connecting to the mail exchanger which serves the email address domain.
     */
    SmtpConnectionFailure,

    // #endregion

    // #region Mailbox verification failures

    /**
     * The mailbox for the e-mail address does not exist.
     */
    MailboxDoesNotExist,

    /**
     * A connection error occurred while validating the mailbox for the e-mail address.
     */
    MailboxConnectionFailure,

    /**
     * The external mail exchanger rejected the validation request.
     */
    LocalSenderAddressRejected,

    /**
     * A timeout occurred while verifying the existence of the mailbox.
     */
    MailboxValidationTimeout,

    /**
     * The requested mailbox is temporarily unavailable; it could be experiencing technical issues or some other transient problem
     * (could be over quota, for example).
     */
    MailboxTemporarilyUnavailable,

    /**
     * The external mail exchanger does not support international mailbox names. To support this feature, mail exchangers must comply with
     * <a href="http://www.ietf.org/rfc/rfc5336.txt">RFC 5336</a> and support and announce both the 8BITMIME and the UTF8SMTP protocol extensions.
     */
    ServerDoesNotSupportInternationalMailboxes,

    // #endregion

    // #region Catch-all rejection failures

    /**
     * A timeout occurred while verifying fake e-mail address rejection for the mail server.
     */
    CatchAllValidationTimeout,

    /**
     * The external mail exchanger accepts fake, non existent, email addresses; therefore the provided email address MAY be inexistent too.
     */
    ServerIsCatchAll,

    /**
     * A connection error occurred while verifying the external mail exchanger rejects inexistent email addresses.
     */
    CatchAllConnectionFailure,

    // #endregion

    // #region HTTP failures

    /**
     * A timeout has occured while connecting to the HTTP server (web mail) which serves the e-mail address domain.
     */
    HttpConnectionTimeout,

    /**
     * A socket connection error occured while connecting to the HTTP server (web mail) which serves the e-mail address domain.
     */
    HttpConnectionFailure,

    // #endregion

    /**
     * The mail exchanger is temporarily unavailable.
     */
    ServerTemporaryUnavailable,

    /**
     * The external mail exchanger replied one or more non-standard SMTP lines and caused the SMTP session to be aborted.
     */
    SmtpDialogError,

    /**
     * The external mail exchanger rejected the local endpoint, probably because of its own policy rules.
     */
    LocalEndPointRejected,

    /**
     * One or more unhandled exceptions have been thrown during the verification process and something went wrong
     * on the Verifalia side.
     */
    UnhandledException
}
