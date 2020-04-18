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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

import static java.util.Objects.nonNull;

/**
 * Represents a single validated entry within a <tt>Validation</tt>.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationEntry {
    /**
     * The index of this entry within its <tt>Validation</tt> container. This property is mostly useful in the event
     * the API returns a filtered view of the items.
     */
    private Integer index;

    /**
     * The input string being validated.
     */
    private String inputData;

    /**
     * The <tt>ValidationEntryClassification</tt> for the status of this email address.
     */
    private ValidationEntryClassification classification;

    /**
     * The validation status for this entry.
     */
    private ValidationEntryStatus status;

    /**
     * Gets the email address, without any eventual comment or folding white space.
     */
    private String emailAddress;

    /**
     * Gets the local part of the email address, without comments and folding white spaces.
     */
    private String emailAddressLocalPart;

    /**
     * Gets the local part of the email address, without comments and folding white spaces. If the ASCII-only (punycode)
     * version of the domain part is needed, use <tt>AsciiEmailAddressDomainPart</tt>.
     */
    private String emailAddressDomainPart;

    /**
     * Gets the domain part of the email address, converted to ASCII if needed and with comments and folding white
     * spaces stripped off.
     * <p>The ASCII encoding is performed using the standard <a href="http://en.wikipedia.org/wiki/Punycode">Punycode algorithm</a>.
     * <p>To get the domain part without any ASCII encoding, use <tt>EmailAddressDomainPart</tt>.</p>
     */
    private String asciiEmailAddressDomainPart;

    /**
     * If true, the email address has an international domain name.
     */
    private Boolean hasInternationalDomainName;

    /**
     * If true, the email address has an international mailbox name.
     */
    private Boolean hasInternationalMailboxName;

    /**
     * If true, the email address comes from a disposable email address (DEA) provider.
     * <p>See <see cref="https://verifalia.com/help/email-validations/what-is-a-disposable-email-address-dea"/> for
     * additional information about disposable email addresses.
     */
    private Boolean isDisposableEmailAddress;

    /**
     * If true, the local part of the email address is a well-known role account.
     */
    private Boolean isRoleAccount;

    /**
     * If true, the email address comes from a free email address provider (e.g. gmail, yahoo, outlook / hotmail, ...).
     */
    private Boolean isFreeEmailAddress;

    /**
     * The position of the character in the email address that eventually caused the syntax validation to fail.
     * Returns <tt>null</tt> if there isn't any syntax failure.
     */
    private Integer syntaxFailureIndex;

    /**
     * A custom, optional string which is passed back upon completing the validation. To pass back and forth a custom
     * value, use the <tt>ValidationRequestEntry.Custom</tt> property of <tt>ValidationRequestEntry</tt>.
     */
    private String custom;

    /**
     * The zero-based index of the first occurrence of this email address in the parent <tt>Validation</tt>, in the
     * event the <tt>Status</tt> for this entry is <tt>Duplicate</tt>; duplicated items do not expose any result detail
     * apart from this and the eventual <tt>Custom</tt> values.
     */
    private Integer duplicateOf;

    /**
     * The date this entry has been completed, if available.
     */
    private Date completedOn;

    /**
     * Constructs new object.
     */
    protected ValidationEntry() {
    }

    /**
     * Overrides the default set method for status with custom logic to handle status as Unknown if no valid status is
     * returned from the Verifalia API.
     *
     * @param status Validation entry status mapped which needs to be checked
     */
    public void setStatus(ValidationEntryStatus status) {
        if (status == null) {
            this.status = ValidationEntryStatus.Unknown;
        } else {
            this.status = status;
        }
    }
}