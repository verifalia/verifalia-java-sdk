package com.verifalia.api.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Utility methods/functions used throughout the SDK
 */
public class Utils {
    /**
     * Generates URI for HTTP request.
     *
     * @param paramMap Map with variable names and values to be passed when making URI for http request.
     * @return URI URI for HTTP request based on the input parameters.
     */
    public static URI getHttpUri(Map<String, String> paramMap) {
        URIBuilder builder = new URIBuilder();
        try {
            if (nonNull(paramMap) && paramMap.size() > 0) {
                Iterator<String> paramMapIter = paramMap.keySet().iterator();
                while (paramMapIter.hasNext()) {
                    String paramKey = paramMapIter.next();
                    String paramValue = paramMap.get(paramKey);
                    if (!StringUtils.isEmpty(paramValue)) {
                        builder.setParameter(paramKey, paramValue);
                    }
                }
            }

            return builder.build();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Converts local date to string based on the input format.
     *
     * @param localDate  LocalDate object which needs to be formatted.
     * @param dateFormat Date format in which the local date object needs to be formatted.
     * @return String Converted local date to string as per the input format.
     */
    public static String convertLocalDateToString(LocalDate localDate, String dateFormat) {
        if (nonNull(localDate)) {
            return localDate.format(DateTimeFormatter.ofPattern(dateFormat));
        }
        return StringUtils.EMPTY;
    }
}
