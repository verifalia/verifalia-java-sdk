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
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String durationString = jsonParser.getText();

        if (durationString == null) {
            return null;
        }

        return parseDuration(durationString);
    }

    private Duration parseDuration(@NonNull String inputData) {
        inputData = inputData.trim();

        // Parse the eventual days information

        String[] dayFields = inputData.split("\\.");

        if (dayFields.length > 2) {
            throw new IllegalArgumentException();
        }

        int days = 0;
        String timeData = inputData;

        if (dayFields.length == 2) {
            days = Integer.parseInt(dayFields[0]);
            timeData = dayFields[1];
        }

        // Parse the time information

        String[] timeFields = timeData.split(":");

        if (timeFields.length != 3) {
            throw new IllegalArgumentException();
        }

        return Duration.ofSeconds((days * 24 * 60 * 60) +
                (Integer.parseInt(timeFields[0]) * 60 * 60) +
                (Integer.parseInt(timeFields[1]) * 60) +
                (Integer.parseInt(timeFields[2])));
    }
}