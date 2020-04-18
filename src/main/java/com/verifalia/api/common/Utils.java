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
 * Internal utility methods/functions used throughout the SDK.
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