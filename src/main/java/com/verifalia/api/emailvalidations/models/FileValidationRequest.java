/*
 * Verifalia - Email list cleaning and real-time email verification service
 * https://verifalia.com/
 * support@verifalia.com
 *
 * Copyright (c) 2005-2020 Cobisi Research
 *
 * Cobisi Research
 * Via Prima Strada, 35
 * 35129, Padova
 * Italy - European Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.verifalia.api.emailvalidations.models;

import com.verifalia.api.emailvalidations.serialization.LineEndingModeSerializer;
import lombok.*;
import org.apache.http.entity.ContentType;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.*;

import static java.util.Objects.nonNull;

/**
 * Represents an email validation request through a file import, to be submitted against the Verifalia API.
 * Verifalia offers support for the following file types:
 * - plain text files (.txt), with one email address per line (MIME type: text/plain)
 * - comma-separated values (.csv), tab-separated values (.tsv) and other delimiter-separated values files (MIME
 * types: text/csv and text/tab-separated-values)
 * - Microsoft Excel spreadsheets - .xls and .xlsx - (MIME types: application/vnd.ms-excel and
 * application/vnd.openxmlformats-officedocument.spreadsheetml.sheet).
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class FileValidationRequest extends AbstractValidationRequest {
    /**
     * An {@link InputStream} (a {@link java.io.FileInputStream}, for example) containing the email addresses to validate.
     */
    @NonNull
    @JsonIgnore
    private InputStream inputStream;

    /**
     * The {@link ContentType} of the provided input file.
     */
    @NonNull
    @JsonIgnore
    private ContentType contentType;

    /**
     * An optional {@link Integer} with the zero-based index of the first row to import and process. If not specified, Verifalia
     * will start processing files from the first (0) row.
     */
    private Integer startingRow;

    /**
     * An optional {@link Integer} with the zero-based index of the last row to import and process. If not specified, Verifalia
     * will process rows until the end of the file.
     */
    private Integer endingRow;

    /**
     * An optional {@link Integer} with the zero-based index of the column to import; applies to comma-separated (.csv),
     * tab-separated (.tsv) and other delimiter-separated values files, and Excel files. If not specified, Verifalia will
     * use the first (0) column.
     */
    private Integer column;

    /**
     * An optional {@link Integer} with the zero-based index of the worksheet to import; applies to Excel files only.
     * If not specified, Verifalia will use the first (0) worksheet.
     */
    private Integer sheet;

    /**
     * Allows to specify the line ending sequence of the provided file; applies to plain-text files, comma-separated (.csv),
     * tab-separated (.tsv) and other delimiter-separated values files.
     */
    @JsonSerialize(using = LineEndingModeSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private LineEndingMode lineEnding;

    /**
     * An optional string with the column delimiter sequence of the file; applies to comma-separated (.csv), tab-separated
     * (.tsv) and other delimiter-separated values files. If not specified, Verifalia will use the , (comma) symbol for
     * CSV files and the \t (tab) symbol for TSV files.
     */
    private String delimiter;

    // region Constructor overloads accepting a File instance

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param file The file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     */
    public FileValidationRequest(@NonNull final File file, @NonNull final ContentType contentType) throws FileNotFoundException {
        this(file, contentType, null, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param file The file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public FileValidationRequest(@NonNull final File file, @NonNull final ContentType contentType, final QualityLevelName quality) throws FileNotFoundException {
        this(file, contentType, quality, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param file The file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final File file, @NonNull final ContentType contentType, final DeduplicationMode deduplication) throws FileNotFoundException {
        this(file, contentType, null, deduplication);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param file The file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final File file, @NonNull final ContentType contentType, final QualityLevelName quality, final DeduplicationMode deduplication) throws FileNotFoundException {
        this(new FileInputStream(file), contentType, quality, deduplication);
    }

    // endregion

    // region Constructor overloads accepting a file name

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull final ContentType contentType) throws FileNotFoundException {
        this(fileName, contentType, null, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull final ContentType contentType, final QualityLevelName quality) throws FileNotFoundException {
        this(fileName, contentType, quality, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull final ContentType contentType, final DeduplicationMode deduplication) throws FileNotFoundException {
        this(fileName, contentType, null, deduplication);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull final ContentType contentType, final QualityLevelName quality, final DeduplicationMode deduplication) throws FileNotFoundException {
        this(new File(fileName), contentType, quality, deduplication);
    }

    // endregion

    // region Constructor overloads accepting a file name and a MIME type

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param mimeType The MIME content type of the file to be submitted for validation.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull final String mimeType) throws FileNotFoundException {
        this(fileName, mimeType, null, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param mimeType The MIME content type of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull final String mimeType, final QualityLevelName quality) throws FileNotFoundException {
        this(fileName, mimeType, quality, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param mimeType The MIME content type of the file to be submitted for validation.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull final String mimeType, final DeduplicationMode deduplication) throws FileNotFoundException {
        this(fileName, mimeType, null, deduplication);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param fileName The name of the file to be submitted for validation.
     * @param mimeType The MIME content type of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final String fileName, @NonNull String mimeType, final QualityLevelName quality, final DeduplicationMode deduplication) throws FileNotFoundException {
        this(new File(fileName), ContentType.create(mimeType), quality, deduplication);
    }

    // endregion

    // region Constructor overloads accepting a byte array

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param data The file byte array to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     */
    public FileValidationRequest(@NonNull final byte[] data, @NonNull final ContentType contentType) {
        this(data, contentType, null, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param data The file byte array to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public FileValidationRequest(@NonNull final byte[] data, @NonNull final ContentType contentType, final QualityLevelName quality) {
        this(data, contentType, quality, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param data The file byte array to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final byte[] data, @NonNull final ContentType contentType, final DeduplicationMode deduplication) {
        this(data, contentType, null, deduplication);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param data The file byte array to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final byte[] data, @NonNull final ContentType contentType, final QualityLevelName quality, final DeduplicationMode deduplication) {
        this(new ByteArrayInputStream(data), contentType, deduplication);
    }

    // endregion

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param stream The input stream to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     */
    public FileValidationRequest(@NonNull final InputStream stream, @NonNull final ContentType contentType) {
        this(stream, contentType, null, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param stream The input stream to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     */
    public FileValidationRequest(@NonNull final InputStream stream, @NonNull final ContentType contentType, final QualityLevelName quality) {
        this(stream, contentType, quality, null);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param stream The input stream to be submitted for validation.
     * @param contentType The {@link ContentType} of the file to be submitted for validation.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final InputStream stream, @NonNull final ContentType contentType, final DeduplicationMode deduplication) {
        this(stream, contentType, null, deduplication);
    }

    /**
     * Initializes a {@link FileValidationRequest} to be submitted to the Verifalia email validation engine.
     * @param stream The input stream to be submitted for validation.
     * @param quality An optional {@link QualityLevelName} referring to the expected results quality for the request.
     * @param deduplication An optional {@link DeduplicationMode} to use while determining which email addresses are duplicates.
     */
    public FileValidationRequest(@NonNull final InputStream stream, @NonNull final ContentType contentType, final QualityLevelName quality, final DeduplicationMode deduplication) {
        setInputStream(stream);
        setContentType(contentType);

        if (nonNull(quality)) {
            setQuality(quality);
        }
        if (nonNull(deduplication)) {
            setDeduplication(deduplication);
        }
    }
}