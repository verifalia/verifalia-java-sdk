package com.verifalia.api.common;

/**
 * Constants that will be used throughout the SDK
 */
public class Constants {
	/**
	 * Verifalia API version
	 */
	public static final String DEFAULT_API_VERSION = "v2.1";

	//Constants related to Verifalia API tool

	/**
	 * Verifalia API tool program name
	 */
	public static final String PROGRAM_NAME = "VerifaliaApiTool";

	/**
	 * Verifalia API tool program version
	 */
	public static final String PROGRAM_VERSION = "0.0.1";

	/**
	 * Verifalia API parameter for cursor to handle pagination
	 */
	public static final String API_PARAM_CURSOR = "cursor";

	// Other constants
	public static final String STRING_SEPERATOR_COMMA = ",";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final Integer VALIDATION_INPUT_PRIORITY_MIN_VALUE = 0;
	public static final Integer VALIDATION_INPUT_PRIORITY_MAX_VALUE = 255;
	public static final String TLS_AUTHENTICATION_JKS = "jks";
	public static final String UNKNOWN_ERROR_MSG = "Unknown error found while processing your request";
}
