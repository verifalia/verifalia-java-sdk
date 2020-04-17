package com.verifalia.api.common;

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

    private String toString(Duration value) {
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