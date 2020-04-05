package com.verifalia.api.common;

/**
 * Constants that will be used throughout the SDK
 */
public class Constants {
	// API related constants that will be used making call to Verifalia

	/**
	 * Verifalia client version
	 */
	public static final String CLIENT_VERSION = "2.1";

	/**
	 * Verifalia API base URL scheme
	 */
	public static final String DEFAULT_BASE_URL_SCHEME = "https:";

	/**
	 * Verifalia API base URL host
	 */
	public static final String DEFAULT_BASE_URL_HOST = "api.verifalia.com";

	/**
	 * Verifalia API TLS base URL host
	 */
	public static final String DEFAULT_TLS_BASE_URL_HOST = "api-cca.verifalia.com";

	/**
	 * Verifalia API version
	 */
	public static final String DEFAULT_API_VERSION = "v2.1";

	/**
	 * Verifalia API base URL
	 */
	public static final String DEFAULT_BASE_URL = DEFAULT_BASE_URL_SCHEME + "//" + DEFAULT_BASE_URL_HOST;

	/**
	 * Verifalia API TLS base URL
	 */
	public static final String DEFAULT_TLS_BASE_URL = DEFAULT_BASE_URL_SCHEME + "//" + DEFAULT_TLS_BASE_URL_HOST;

	/**
	 * Verifalia API user agent
	 */
	public static final String USER_AGENT = "verifalia-rest-client/java/" + CLIENT_VERSION;

	/**
	 * Verifalia request content type
	 */
	public static final String REQUEST_CONTENT_TYPE = "application/json";

	/**
	 * Verifalia request charset
	 */
	public static final String REQUEST_CHARSET = "utf-8";

	/**
	 * Verifalia response accept type
	 */
	public static final String RESPONSE_ACCEPT_TYPE = "application/json";

	/**
	 * Verifalia response accept type encoded
	 */
	public static final String RESPONSE_ACCEPT_TYPE_ENCODING = "gzip";

	//Constants related to Verifalia API tool

	/**
	 * Verifalia API tool program name
	 */
	public static final String PROGRAM_NAME = "VerifaliaApiTool";

	/**
	 * Verifalia API tool program version
	 */
	public static final String PROGRAM_VERSION = "0.0.1";

	//API end points for functionalities provided

	/**
	 * Verifalia API endpoint/resource for email validations
	 */
	public static final String EMAIL_VALIDATIONS_RESOURCE = "email-validations";

	/**
	 * Verifalia API endpoint/resource for email validations overview
	 */
	public static final String EMAIL_VALIDATIONS_OVERVIEW_RESOURCE = "overview";

	/**
	 * Verifalia API endpoint/resource for email validations entries
	 */
	public static final String EMAIL_VALIDATIONS_ENTRIES_RESOURCE = "entries";

	/**
	 * Verifalia API endpoint/resource for credits
	 */
	public static final String CREDITS_RESOURCE = "credits";

	/**
	 * Verifalia API endpoint/resource for credits balance
	 */
	public static final String CREDITS_BALANCE_RESOURCE = CREDITS_RESOURCE + "/balance";

	/**
	 * Verifalia API endpoint/resource for credits daily usage
	 */
	public static final String CREDITS_DAILY_USAGE_RESOURCE = CREDITS_RESOURCE + "/daily-usage";

	/**
	 * Verifalia API endpoint to fetch authentication token
	 */
	public static final String AUTH_TOKEN_RESOURCE = "/auth/tokens";

	/**
	 * Verifalia API parameter for cursor to handle pagination
	 */
	public static final String API_PARAM_CURSOR = "cursor";

	// Other constants
	public static final char CR = '\r';
	public static final char LF = '\n';
	public static final String STRING_SEPERATOR_COMMA = ",";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final Integer VALIDATION_INPUT_PRIORITY_MIN_VALUE = 0;
	public static final Integer VALIDATION_INPUT_PRIORITY_MAX_VALUE = 255;
	public static final String TLS_AUTHENTICATION_JKS = "jks";
	public static final String TLS_AUTHENTICATION_VERSION_1_1 = "TLSv1.1";
	public static final String TLS_AUTHENTICATION_VERSION_1_2 = "TLSv1.2";
	public static final String AUTHENTICATION_BASIC = "BASIC";
	public static final String AUTHENTICATION_BEARER = "BEARER";
	public static final String AUTHENTICATION_TLS = "TLS";
}
