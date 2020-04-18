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

import java.util.Objects;

/**
 * The priority (speed) of a validation job, relative to the parent Verifalia account. In the event of an account
 * with many concurrent validation jobs, this value allows to increase the processing speed of a job with respect to the others.
 * The allowed range of values spans from {@link #Lowest} (0 - lowest priority) to {@link #Highest}
 * (255 - highest priority), where the midway value {@link #Normal} (127) means normal priority; if not specified,
 * Verifalia processes all the concurrent validation jobs for an account using the same speed.
 */
@Getter
public class ValidationPriority {
    private byte value;

    /**
     * The lowest possible processing priority (speed) for a validation job.
     */
    public static ValidationPriority Lowest = new ValidationPriority((byte) 0);

    /**
     * Normal processing priority (speed) for a validation job.
     */
    public static ValidationPriority Normal = new ValidationPriority((byte) 127);

    /**
     * The highest possible processing priority (speed) for a validation job.
     */
    public static ValidationPriority Highest = new ValidationPriority((byte) 255);

    /**
     * Initializes a custom processing priority using the given specified value.
     * @param value The allowed range of values spans from {@link #Lowest} (0 - lowest priority) to {@link #Highest}
     * (255 - highest priority), where the midway value {@link #Normal} (127) means normal priority.
     */
    public ValidationPriority(byte value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationPriority that = (ValidationPriority) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (this.value == Lowest.value)
        {
            return new Byte(this.value).toString() + " (lowest)";
        }

        if (this.value == Normal.value)
        {
            return new Byte(this.value).toString() + " (normal)";
        }

        if (this.value == Highest.value)
        {
            return new Byte(this.value).toString() + " (highest)";
        }

        return new Byte(this.value).toString();
    }
}
