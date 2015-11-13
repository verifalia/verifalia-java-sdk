package com.verifalia.api.rest;

public class Utils {
	public static final char CR = '\r';
	public static final char LF = '\n';
	public static final String EMPTY_STRING = "";
	
    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>Removes one newline from end of a String if it's there,
     * otherwise leave it alone.  A newline is &quot;{@code \n}&quot;,
     * &quot;{@code \r}&quot;, or &quot;{@code \r\n}&quot;.</p>
     * @param str  the String to chomp a newline from, may be null
     * @return String without newline, {@code null} if null String input
     */
    public static String chomp(final String str) {
    	    	
        if (isEmpty(str)) {
            return str;
        }

        if (str.length() == 1) {
            final char ch = str.charAt(0);
            if (ch == CR || ch == LF) {
                return "";
            }
            return str;
        }

        int lastIdx = str.length() - 1;
        final char last = str.charAt(lastIdx);

        if (last == LF) {
            if (str.charAt(lastIdx - 1) == CR) {
                lastIdx--;
            }
        } else if (last != CR) {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }
}
