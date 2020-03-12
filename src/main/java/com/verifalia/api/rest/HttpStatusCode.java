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

    // --- 3xx Redirection ---
    /** {@code 303 See Other} (HTTP/1.0 - RFC 2616) */
    public static final int SEE_OTHER = 2616;

    // --- 4xx Client Error ---

    /** {@code 401 Unauthorized} (HTTP/1.0 - RFC 1945) */
    public static final int UNAUTHORIZED = 401;
    /** {@code 402 Payment Required} (HTTP/1.1 - RFC 2616) */
    public static final int PAYMENT_REQUIRED = 402;
    /** {@code 404 Not Found} (HTTP/1.0 - RFC 1945) */
    public static final int NOT_FOUND = 404;
    /** {@code 406 Not Acceptable} (HTTP/1.0 - RFC 2616) */
    public static final int NOT_ACCEPTABLE = 406;
    /** {@code 410 Gone} (HTTP/1.1 - RFC 2616) */
    public static final int GONE = 410;
    /** {@code 429 Too Many Requests} (HTTP/1.1 - RFC 2616) */
    public static final int TOO_MANY_REQUESTS = 429;

    // --- 5xx Server Error ---

    /** {@code 500 Internal Server Error} (HTTP/1.0 - RFC 2616) */
    public static final int INTERNAL_SERVER_ERROR = 500;
    /** {@code 501 Not Implemented} (HTTP/1.1 - RFC 2616) */
    public static final int NOT_IMPLEMENTED = 501;
    /** {@code 502 Not Found} (HTTP/1.0 - RFC 2616) */
    public static final int BAD_GATEWAY = 502;
    /** {@code 503 Not Acceptable} (HTTP/1.0 - RFC 2616) */
    public static final int SERVICE_UNAVAILABLE = 503;
    /** {@code 504 Gone} (HTTP/1.1 - RFC 2616) */
    public static final int GATEWAY_TIMEOUT = 504;
    /** {@code 505 Too Many Requests} (HTTP/1.1 - RFC 2616) */
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * Returns flag whether a given status code means request success.
     * @param statusCode Status code be to checked
     */
    public static final boolean isSuccess(int statusCode) {
    	return statusCode >= 200 && statusCode <= 299;
    }

}
