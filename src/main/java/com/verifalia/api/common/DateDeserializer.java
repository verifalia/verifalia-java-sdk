package com.verifalia.api.common;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateDeserializer extends JsonDeserializer<LocalDate> {
    private static LocalDate convertStringToLocalDate(@NonNull final String dateStr) {
        if (!StringUtils.isBlank(dateStr)) {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
        }
        return null;
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateString = jsonParser.getText();

        if (dateString == null) {
            return null;
        }

        return convertStringToLocalDate(dateString);
    }
}