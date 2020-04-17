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

package com.verifalia.api.common;

import lombok.NonNull;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class DurationSerializer extends JsonSerializer<Duration> {
    private final long SECONDS_IN_MINUTE = 60;
    private final long SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60;
    private final long SECONDS_IN_DAY = SECONDS_IN_HOUR * 24;

    @Override
    public void serialize(Duration value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            jgen.writeString(toString(value));
        }
    }

    private String toString(@NonNull Duration value) {
        StringBuffer sb = new StringBuffer();
        long remainingSeconds = value.toMillis() / 1000;

        long days = remainingSeconds / SECONDS_IN_DAY;

        if (days != 0) {
            sb.append(days);
            sb.append(".");
            remainingSeconds = remainingSeconds % SECONDS_IN_DAY;
        }

        sb.append(remainingSeconds / SECONDS_IN_HOUR);
        sb.append(":");
        remainingSeconds = remainingSeconds % SECONDS_IN_HOUR;

        sb.append(remainingSeconds / SECONDS_IN_MINUTE);
        sb.append(":");
        remainingSeconds = remainingSeconds % SECONDS_IN_MINUTE;

        sb.append(remainingSeconds);

        return sb.toString();
    }
}