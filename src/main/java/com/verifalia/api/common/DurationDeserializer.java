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