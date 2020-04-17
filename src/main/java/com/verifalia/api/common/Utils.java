package com.verifalia.api.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static java.util.Objects.nonNull;

/**
 * Utility methods/functions used throughout the SDK
 */
public class Utils {
    /**
     * Generates URI for HTTP request.
     *
     * @param scheme   Scheme (http/https) for the URI.
     * @param host     URI host.
     * @param path     URI resource.
     * @param paramMap Map with variable names and values to be passed when making URI for http request.
     * @return URI URI for HTTP request based on the input parameters.
     */
    public static URI getHttpUri(String scheme, String host, String path, Map<String, String> paramMap) {
        URIBuilder builder = new URIBuilder();
        try {
            if (!StringUtils.isBlank(scheme)) {
                builder.setScheme(scheme);
            }
            if (!StringUtils.isBlank(host)) {
                builder.setHost(host);
            }
            if (!StringUtils.isBlank(path)) {
                builder.setPath(path);
            }
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

    /**
     * Converts string to local date based on the input format.
     *
     * @param dateStr    Date string which needs to be converted to local date.
     * @param dateFormat Date format in which the local date object needs to be formatted.
     * @return LocalDate Converted date string to local date as per the input format.
     */
    public static LocalDate convertStringToLocalDate(String dateStr, String dateFormat) {
        if (!StringUtils.isBlank(dateStr)) {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
        }
        return null;
    }

    /**
     * Generates random number in the range from 0 to the input passed in the request
     *
     * @param rangeTo Identifies to range for generating random number
     * @return int Random between in the range from 0 to the input passed in the request
     */
    public static int getRandomNumberInRange(int rangeTo) {
        return new Random().nextInt(rangeTo);
    }
}
