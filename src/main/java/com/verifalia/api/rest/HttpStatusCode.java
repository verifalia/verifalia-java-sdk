package com.verifalia.api.rest;

/**
 * Provides constants for some HTTP status codes used by the Verifalia SDK.
 */
public final class HttpStatusCode {

    // --- 2xx Success ---

    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final int OK = 200;
    /** {@code 202 Accepted} (HTTP/1.0 - RFC 1945) */
    public static final int ACCEPTED = 202;

    // --- 4xx Client Error ---

    /** {@code 401 Unauthorized} (HTTP/1.0 - RFC 1945) */
    public static final int UNAUTHORIZED = 401;
    /** {@code 402 Payment Required} (HTTP/1.1 - RFC 2616) */
    public static final int PAYMENT_REQUIRED = 402;
    /** {@code 404 Not Found} (HTTP/1.0 - RFC 1945) */
    public static final int NOT_FOUND = 404;
    /** {@code 410 Gone} (HTTP/1.1 - RFC 2616) */
    public static final int GONE = 410;

    /**
     * Returns flag whether a given status code means request success.
     * @param statusCode Status code be to checked
     */
    public static final boolean isSuccess(int statusCode) {
    	return statusCode >= 200 && statusCode <= 299;
    }

}
