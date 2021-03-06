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
 * A classification of the <tt>status</tt> of a {@link ValidationEntry}.
 */
public enum ValidationEntryClassification {
    /**
     * A {@link ValidationEntry} marked as {@link #Deliverable} refers to an email address which is deliverable.
     */
    Deliverable,

    /**
     * A {@link ValidationEntry} marked as {@link #Undeliverable} refers to an email address which is either invalid
     * or no longer deliverable.
     */
    Undeliverable,

    /**
     * A {@link ValidationEntry} marked as {@link #Risky} refers to an email address which could be no longer valid.
     */
    Risky,

    /**
     * A {@link ValidationEntry} marked as {@link #Unknown} contains an email address whose deliverability is unknown.
     */
    Unknown
}
