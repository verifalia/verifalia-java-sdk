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
import lombok.NonNull;
import lombok.ToString;

import java.util.Objects;

/**
 * A reference to a Verifalia quality level. Quality levels determine how Verifalia validates email addresses, including
 * whether and how the automatic reprocessing logic occurs (for transient statuses) and the verification timeouts
 * settings.
 */
public class QualityLevelName {
    /**
     * The Standard quality level. Suitable for most businesses, provides good results for the vast majority of email
     * addresses; features a single validation pass and 5 second anti-tarpit time; less suitable for validating email
     * addresses with temporary issues (mailbox over quota, greylisting, etc.) and slower mail exchangers.
     */
    public static QualityLevelName Standard = new QualityLevelName("Standard");

    /**
     * The High quality level. Much higher quality, featuring 3 validation passes and 50 seconds of anti-tarpit time,
     * so you can even validate most addresses with temporary issues, or slower mail exchangers.
     */
    public static QualityLevelName High = new QualityLevelName("High");

    /**
     * The Extreme quality level. Unbeatable, top-notch quality for professionals who need the best results the industry
     * can offer: performs email validations at the highest level, with 9 validation passes and 2 minutes of anti-tarpit
     * time.
     */
    public static QualityLevelName Extreme = new QualityLevelName("Extreme");

    private final String nameOrGuid;

    /**
     * Initializes a new instance of <tt>QualityLevelName</tt> from its name.
     * @param nameOrGuid
     */
    public QualityLevelName(@NonNull final String nameOrGuid) {
        this.nameOrGuid = nameOrGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualityLevelName that = (QualityLevelName) o;
        return nameOrGuid.equals(that.nameOrGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOrGuid);
    }

    @Override
    public String toString() {
        return nameOrGuid;
    }
}