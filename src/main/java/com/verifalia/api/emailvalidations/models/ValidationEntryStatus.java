/*
 * Verifalia - Email list cleaning and real-time email verification service
 * https://verifalia.com/
 * support@verifalia.com
 *
 * Copyright (c) 2005-2020 Cobisi Research
 *
 * Cobisi Research
 * Via Prima Strada, 35
 * 35129, Padova
 * Italy - European Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.verifalia.api.emailvalidations.models;

/**
 * Provides enumerated values for the supported validation statuses for a {@link ValidationEntry}.
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
     * The item is a duplicate of another email address in the list. To find out the entry this item is a duplicate of,
     * check the {@link ValidationEntry#getDuplicateOf()} property of the {@link ValidationEntry} instance which exposes this
     * status code.
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
     * The mailbox is disposable (DEA).
     */
    MailboxIsDea,

    /**
     * The requested mailbox is temporarily unavailable; it could be experiencing technical issues or some other transient problem.
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
     * The external mail exchanger is a well-know disposable email address provider (DEA).
     */
    MailExchangerIsWellKnownDea,

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
     * The external mail exchanger accepts fake, non existent, email addresses; therefore the provided email address MAY be nonexistent too.
     */
    ServerIsCatchAll,

    /**
     * The mail exchanger responsible for the email address under test is temporarily unavailable.
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
     * The mail exchanger responsible for the email address under test replied one or more non-standard SMTP replies which
     * caused the SMTP session to be aborted.
     */
    SmtpDialogError,

    /**
     * The email address has been successfully validated.
     */
    Success,

    /**
     * The domain literal of the email address couldn't accept messages from the Internet; while Verifalia supports them,
     * domain literals are quite rare nowadays.
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
    UnmatchedQuotedPair,

    /**
     * Unknown validation status, due to a value reported by the API which is missing in this SDK.
     */
    Unknown
}