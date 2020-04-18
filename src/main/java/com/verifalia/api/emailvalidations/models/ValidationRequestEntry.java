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
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * A single item of a {@link ValidationRequest} containing an email address to validate, specified by way
 * of the {@link #inputData} property.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationRequestEntry {
    private static final int MAX_CUSTOM_LENGTH = 50;

    /**
     * The input string to validate, which should represent an email address.
     */
    private String inputData;

    /**
     * An optional, custom string which is passed back upon completing the validation job.
     * Setting this value is useful in the event you wish to have a custom reference of this {@link ValidationRequestEntry}
     * with something else (for example, a record in your database). This value accepts a string with a maximum length
     * of 50 characters.
     */
    private String custom;

    /**
     * Initializes a new {@link ValidationRequestEntry}
     * @param inputData The input data string (which should be an email address) to validate.
     */
    public ValidationRequestEntry(@NonNull final String inputData) {
        this.inputData = inputData;
    }

    /**
     * Initializes a new {@link ValidationRequestEntry}
     * @param inputData The input data string (which should be an email address) to validate.
     * @param custom An optional, custom string which is passed back upon completing the validation job.
     */
    public ValidationRequestEntry(@NonNull final String inputData, final String custom) {
        this.inputData = inputData;
        this.custom = custom;
    }

    public void setInputData(@NonNull final String inputData) {
        this.inputData = inputData;
    }

    public void setCustom(final String value) {
        if (value != null) {
            if (value.length() > MAX_CUSTOM_LENGTH) {
                throw new IllegalArgumentException("Custom value '" + value + "' exceeds the maximum allowed length of " + MAX_CUSTOM_LENGTH + " characters.");
            }
        }

        this.custom = value;
    }
}
