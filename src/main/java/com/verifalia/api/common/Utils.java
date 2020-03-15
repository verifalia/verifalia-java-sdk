package com.verifalia.api.common;

import org.apache.commons.lang3.StringUtils;

public class Utils {
    /**
     * <p>Removes one newline from end of a String if it's there,
     * otherwise leave it alone.  A newline is &quot;{@code \n}&quot;,
     * &quot;{@code \r}&quot;, or &quot;{@code \r\n}&quot;.</p>
     * @param str  the String to chomp a newline from, may be null
     * @return String without newline, {@code null} if null String input
     */
    public static String chomp(final String str) {

        if (StringUtils.isEmpty(str)) {
            return str;
        }

        if (str.length() == 1) {
            final char ch = str.charAt(0);
            if (ch == Constants.CR || ch == Constants.LF) {
                return "";
            }
            return str;
        }

        int lastIdx = str.length() - 1;
        final char last = str.charAt(lastIdx);

        if (last == Constants.LF) {
            if (str.charAt(lastIdx - 1) == Constants.CR) {
                lastIdx--;
            }
        } else if (last != Constants.CR) {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }
}
