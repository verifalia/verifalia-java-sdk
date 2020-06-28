package com.verifalia.api.emailvalidations.models;

/**
 * The line-ending mode for an input text file provided to the Verifalia API for verification.
 */
public enum LineEndingMode {
    /**
     * Automatic line-ending detection, attempts to guess the correct line ending from the first chunk of data.
     */
    Auto,

    /**
     * CR + LF sequence (\r\n), commonly used in files generated on Windows.
     */
    CrLf,

    /**
     * CR sequence (\r), commonly used in files generated on classic MacOS.
     */
    Cr,

    /**
     * LF (\n), commonly used in files generated on Unix and Unix-like systems (including Linux and MacOS).
     */
    Lf
}
