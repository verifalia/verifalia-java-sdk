package com.verifalia.api.emailvalidations.models;

/**
 * An optional string with the name of the desired results quality.
 * If not specified, Verifalia will use the default results quality for your account.
 */
public enum ValidationQuality {
    /**
     * The email validation batch is being processed with standard quality.
     */
	Standard,

    /**
     * The email validation batch is being processed with high quality.
     */
    High,

    /**
     * The email validation batch is being processed with extreme quality.
     */
    Extreme
}
