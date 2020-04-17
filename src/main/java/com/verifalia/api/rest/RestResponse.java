package com.verifalia.api.rest;

import com.verifalia.api.exceptions.VerifaliaException;
import lombok.Getter;
import lombok.NonNull;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Represents REST service response
 */
//@Getter
public class RestResponse {
    private final ContentType contentType;
    /**
     * HTTP response code
     */
    @Getter
    private final int statusCode;

    /**
     * Response data
     */
    private final byte[] data;

    /**
     * Creates new object
     *
     * @param statusCode        Status code
     * @param result            Result string
     * @param responseDataClass Class in which the response data needs to be mapped
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public RestResponse(final int statusCode, final HttpEntity entity)
            throws VerifaliaException {

        this.statusCode = statusCode;

        if (entity == null) {
            this.contentType = null;
            this.data = null;

            return;
        }

        try {
            this.contentType = ContentType.get(entity);
            this.data = EntityUtils.toByteArray(entity);
        } catch (IOException exception) {
            throw new VerifaliaException("Cannot read the input stream.", exception);
        }

    }

    public <T> T deserialize(@NonNull final Class<T> dataClass) throws VerifaliaException {
        if (data == null) {
            return null;
        }

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(this.data, dataClass);
        } catch (IOException exception) {
            throw new VerifaliaException("Cannot read the input JSON response.", exception);
        }
    }

    public String readAsString() {
        if (data == null) {
            return null;
        }

        Charset charset = null;
        if (contentType != null) {
            charset = contentType.getCharset();
            if (charset == null) {
                final ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
                charset = defaultContentType != null ? defaultContentType.getCharset() : null;
            }
        }
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }

        return new String(this.data, charset);
    }
}