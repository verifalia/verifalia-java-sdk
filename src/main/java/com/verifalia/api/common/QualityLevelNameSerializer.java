package com.verifalia.api.common;

import com.verifalia.api.emailvalidations.models.QualityLevelName;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class QualityLevelNameSerializer extends JsonSerializer<QualityLevelName> {
    @Override
    public void serialize(QualityLevelName value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            jgen.writeString(value.getNameOrGuid());
        }
    }
}