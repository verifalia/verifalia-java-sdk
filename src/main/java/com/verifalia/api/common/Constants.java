package com.verifalia.api.common;

/**
 * Constants that will be used throughout the SDK
 */
public class Constants {
	// API related constants that will be used making call to Verifalia

	/**
	 * Verifalia Client Version
	 */
	public static final String CLIENT_VERSION = "2.1";
	/**
	 * Verifalia API base URL
	 */
	public static final String DEFAULT_BASE_URL = "https://api.verifalia.com";
	/**
	 * Verifalia API version
	 */
	public static final String DEFAULT_API_VERSION = "v2.1";
	/**
	 * Verifalia API user agent
	 */
	public static final String USER_AGENT_BASE = "verifalia-rest-client/java/";

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

	// Other constants
	public static final char CR = '\r';
	public static final char LF = '\n';
}
