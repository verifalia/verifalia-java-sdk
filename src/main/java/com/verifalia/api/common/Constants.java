package com.verifalia.api.common;

/**
 * Constants that will be used throughout the SDK
 */
public class Constants {
	/*
	 * API related constants that will be used making call to Verifalia
	 */
	public static final String CLIENT_VERSION = "2.1";
	public static final String DEFAULT_BASE_URL = "https://api.verifalia.com";
	public static final String DEFAULT_API_VERSION = "v2.1";
	public static final String USER_AGENT_BASE = "verifalia-rest-client/java/";

	/*
	 * Constants related to Verifalia API tool
	 */
	public static final String PROGRAM_NAME = "VerifaliaApiTool";
	public static final String PROGRAM_VERSION = "0.0.1";

	/*
	 * API end points for functionalities provided
	 */
	public static final String EMAIL_VALIDATIONS_RESOURCE = "email-validations";
	public static final String EMAIL_VALIDATIONS_OVERVIEW_RESOURCE = "overview";
	public static final String EMAIL_VALIDATIONS_ENTRIES_RESOURCE = "entries";
	public static final String CREDITS_RESOURCE = "credits";
	public static final String CREDITS_BALANCE_RESOURCE = CREDITS_RESOURCE + "/balance";
	public static final String CREDITS_DAILY_USAGE_RESOURCE = CREDITS_RESOURCE + "/daily-usage";

	public static final String CREDITS_DAILY_UAGSE_PARAM_DATE = "date";
	public static final String CREDITS_DAILY_UAGSE_PARAM_DATE_SINCE = "date:since";
	public static final String CREDITS_DAILY_UAGSE_PARAM_DATE_UNTIL= "date:until";

	/*
	 * Other constants
	 */
	public static final char CR = '\r';
	public static final char LF = '\n';
}
