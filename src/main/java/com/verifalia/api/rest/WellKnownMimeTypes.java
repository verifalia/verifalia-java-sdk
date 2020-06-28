package com.verifalia.api.rest;

/**
 * Contains the MIME content types supported by the Verifalia API.
 */
public final class WellKnownMimeTypes {
    /**
     * Plain-text files (.txt), with one email address per line.
     */
    public static final String TEXT_PLAIN = "text/plain";

    /**
     * Comma-separated values (.csv).
     */
    public static final String TEXT_CSV = "text/csv";

    /**
     * Tab-separated values (usually coming with the .tsv extension).
     */
    public static final String TEXT_TSV = "text/tab-separated-values";

    /**
     * Microsoft Excel 97-2003 Worksheet (.xls).
     */
    public static final String EXCEL_XLS = "application/vnd.ms-excel";

    /**
     * Microsoft Excel workbook (.xslx).
     */
    public static final String EXCEL_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
}
