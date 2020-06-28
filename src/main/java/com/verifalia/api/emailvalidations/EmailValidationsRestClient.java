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

package com.verifalia.api.emailvalidations;

import com.verifalia.api.common.*;
import com.verifalia.api.common.filters.*;
import com.verifalia.api.common.iterables.*;
import com.verifalia.api.common.models.*;
import com.verifalia.api.emailvalidations.models.*;
import com.verifalia.api.exceptions.*;
import com.verifalia.api.rest.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Allows to submit, retrieve, list and delete email validations.
 * The features of this type are exposed by way of the {@link com.verifalia.api.VerifaliaRestClient#emailValidations}
 * property of {@link com.verifalia.api.VerifaliaRestClient}.
 */
public class EmailValidationsRestClient {
    private final RestClient restClient;

    /**
     * Internal method used to initialize the object. Do not use this directly: instead, use the {@link com.verifalia.api.VerifaliaRestClient#emailValidations}
     * property of the {@link com.verifalia.api.VerifaliaRestClient}.
     */
    public EmailValidationsRestClient(@NonNull final RestClient restClient) {
        this.restClient = restClient;
    }

    // region Submission methods

    /**
     * Submits a new email validation for processing. By default, this method does not wait for the completion of the
     * email validation job: pass a {@link WaitingStrategy} to request a different waiting behavior.
     * @param emailAddress The email address to validate.
     * @return A {@link Validation} object representing the submitted email validation job.
     */
    public Validation submit(@NonNull final String emailAddress) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddress));
    }

    /**
     * Submits a new email validation for processing. By default, this method does not wait for the completion of the
     * email validation job: pass a {@link WaitingStrategy} to request a different waiting behavior.
     * @param emailAddresses One or more email addresses to validate.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses));
    }

    /**
     * Submits a new email validation for processing. By default, this method does not wait for the completion of the
     * email validation job: pass a {@link WaitingStrategy} to request a different waiting behavior.
     * @param emailAddresses One or more email addresses to validate.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final Iterable<String> emailAddresses) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses));
    }

    /**
     * Submits a new email validation for processing.
     * @param emailAddresses One or more email addresses to validate.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, final WaitingStrategy waitingStrategy)
            throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses), waitingStrategy);
    }

    /**
     * Submits a new email validation for processing.
     * @param emailAddress The email address to validate.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String emailAddress, final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddress), waitingStrategy);
    }

    /**
     * Submits a new email validation for processing.
     * @param emailAddresses One or more email addresses to validate.
     * @param deduplication The {@link DeduplicationMode deduplication algorithm} to use while determining which email addresses are duplicates.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, @NonNull final DeduplicationMode deduplication,
                             WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses, deduplication), waitingStrategy);
    }

    /**
     * Submits a new email validation for processing.
     * @param emailAddress The email address to validate.
     * @param quality The desired {@link QualityLevelName quality level} for this email validation.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String emailAddress, @NonNull final QualityLevelName quality,
                             final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddress, quality), waitingStrategy);
    }

    /**
     * Submits a new email validation for processing.
     * @param emailAddresses One or more email addresses to validate.
     * @param quality The desired {@link QualityLevelName quality level} for this email validation.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, @NonNull final QualityLevelName quality,
                             final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses, quality), waitingStrategy);
    }

    /**
     * Submits a new email validation for processing.
     * @param emailAddresses One or more email addresses to validate.
     * @param quality The desired {@link QualityLevelName quality level} for this email validation.
     * @param deduplication The {@link DeduplicationMode deduplication algorithm} to use while determining which email addresses are duplicates.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, @NonNull final QualityLevelName quality,
                             @NonNull final DeduplicationMode deduplication,
                             final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses, quality, deduplication), waitingStrategy);
    }

    /**
     * Submits a new email validation for processing. By default, this method does not wait for the completion of the
     * email validation job: pass a {@link WaitingStrategy} to request a different waiting behavior.
     * @param validationRequest A {@link ValidationRequest} to submit for validation.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final AbstractValidationRequest validationRequest) throws VerifaliaException {
        return submit(validationRequest, null);
    }

    /**
     * Submits a new email validation for processing.
     * @param validationRequest A {@link ValidationRequest} to submit for validation.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return A {@link Validation} object representing the submitted email validation job.
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final AbstractValidationRequest validationRequest, final WaitingStrategy waitingStrategy) throws VerifaliaException {
        // Checks the parameters and build the REST request

        RestRequest request;

        if (validationRequest instanceof ValidationRequest) {
            ValidationRequest standardValidationRequest = (ValidationRequest) validationRequest;

            if (standardValidationRequest.getEntries() == null) {
                throw new IllegalArgumentException("emailAddresses cannot be null");
            }
            if (standardValidationRequest.getEntries().size() == 0) {
                throw new IllegalArgumentException("Can't validate an empty batch (emailAddresses)");
            }

            request = new RestRequest(HttpRequestMethod.POST,
                    "email-validations",
                    // Explicitly set the charset as UTF-8 (see https://github.com/verifalia/verifalia-java-sdk/issues/4)
                    new StringEntity(RestRequest.serializeToJson(validationRequest), "UTF-8"));
        }
        else if (validationRequest instanceof FileValidationRequest) {
            // The actual file content will be checked by the Verifalia API

            FileValidationRequest fileValidationRequest = (FileValidationRequest) validationRequest;

            // Build the multi-part entity
            // TODO: Move the entity building part to an ad-hoc class derived from RestRequest

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            // inputFile part

            entityBuilder.addBinaryBody("inputFile",
                    fileValidationRequest.getInputStream(),
                    fileValidationRequest.getContentType(),
                    // HACK: Dummy file name to make the underlying Java multi-part form data serializer happy
                    "file");

            // Settings part

            entityBuilder.addTextBody("settings",
                    RestRequest.serializeToJson(fileValidationRequest),
                    ContentType.parse("application/json"));

            request = new RestRequest(HttpRequestMethod.POST,
                    "email-validations",
                    entityBuilder.build());
        }
        else {
            throw new IllegalArgumentException("Unsupported class for the validationRequest parameter.");
        }

        // Send the request to the Verifalia servers

        RestResponse response = restClient.execute(request);

        // Handle response based on status code

        switch (response.getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_ACCEPTED: {
                ValidationMapper data = response.deserialize(ValidationMapper.class);
                Validation validation = mapValidationMapperToValidation(data);

                if (response.getStatusCode() == HttpStatus.SC_OK) {
                    // The batch has been completed in real time
                    validation.getOverview().setStatus(ValidationStatus.Completed);
                    return validation;
                }

                // The batch has been accepted but is not yet completed

                if (waitingStrategy == null || !waitingStrategy.waitForCompletion) {
                    validation.getOverview().setStatus(ValidationStatus.InProgress);
                    return validation;
                }

                // Poll the service until completion

                return get(validation.getOverview().getId(), waitingStrategy);
            }

            case HttpStatus.SC_PAYMENT_REQUIRED: {
                throw new InsufficientCreditException(response);
            }

            case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE: {
                throw new UnsupportedMediaTypeException(response);
            }

            default: {
                throw new VerifaliaException(response);
            }
        }
    }

    // endregion

    // region Retrieval methods

    /**
     * Returns an email validation job previously submitted for processing. In the event retrieving the whole validation
     * job data is not needed and getting just the {@link ValidationOverview} would be enough, use the {@link #getOverview(String)}
     * method instead.
     * By default, this method does not wait for the completion of the email validation job: pass a {@link WaitingStrategy}
     * to request a different waiting behavior.
     * @param id The {@link ValidationOverview#id} of the email validation job to retrieve.
     * @return The {@link Validation} object representing the email validation job.
     * @throws VerifaliaException
     */
    public Validation get(@NonNull final String id) throws VerifaliaException {
        return get(id, null);
    }

    /**
     * Returns an email validation job previously submitted for processing. In the event retrieving the whole validation
     * job data is not needed and getting just the {@link ValidationOverview} would be enough, use the {@link #getOverview(String)}
     * method instead.
     * @param id The {@link ValidationOverview#id} of the email validation job to retrieve.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return The {@link Validation} object representing the email validation job.
     * @throws VerifaliaException
     */
    public Validation get(@NonNull final String id, final WaitingStrategy waitingStrategy) throws VerifaliaException {

        // Handle the case when the client wishes to avoid waiting for completion

        Validation result = getOnce(id);

        if (waitingStrategy != null && waitingStrategy.waitForCompletion) {
            EmailValidationsRestClient parent = this;
            PollingTask<Validation> pollingTask = new PollingTask<>(id,
                    result,
                    new PollingCallback<Validation>() {
                        @Override
                        public ValidationOverview getOverview(Validation result) throws VerifaliaException {
                            return result.getOverview();
                        }

                        @Override
                        public Validation refresh(String id) throws VerifaliaException {
                            return parent.getOnce(id);
                        }
                    },
                    waitingStrategy);

            // Waits for the request completion or for the timeout to expire

            pollingTask.run();
            result = pollingTask.getResult();
        }

        return result;
    }

    private Validation getOnce(@NonNull final String id) throws VerifaliaException {
        // Build request
        RestRequest request = new RestRequest(HttpRequestMethod.GET, "email-validations/" + id);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        // Handle response based on status code
        switch (response.getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_ACCEPTED:
            case HttpStatus.SC_GONE: {
                ValidationMapper data = response.deserialize(ValidationMapper.class);
                Validation validation = mapValidationMapperToValidation(data);

                switch (response.getStatusCode()) {
                    case HttpStatus.SC_OK: {
                        validation.getOverview().setStatus(ValidationStatus.Completed);
                        break;
                    }
                    case HttpStatus.SC_ACCEPTED: {
                        validation.getOverview().setStatus(ValidationStatus.InProgress);
                        break;
                    }
                    case HttpStatus.SC_GONE: {
                        validation.getOverview().setStatus(ValidationStatus.Expired);
                        break;
                    }

                    default:
                        throw new IllegalStateException("Unexpected value: " + response.getStatusCode());
                }

                return validation;
            }

            case HttpStatus.SC_NOT_FOUND: {
                return null;
            }

            default: {
                throw new VerifaliaException(response);
            }
        }
    }

    /**
     * Returns a lightweight {@link ValidationOverview} of an email validation job previously submitted for processing.
     * To retrieve the whole job data, including its results, use the {@link #get(String)} method instead.
     * By default, this method does not wait for the completion of the email validation job: pass a {@link WaitingStrategy}
     * to request a different waiting behavior.
     * @param id The {@link ValidationOverview#id} of the email validation job to retrieve.
     * @return
     * @throws VerifaliaException
     */
    public ValidationOverview getOverview(@NonNull final String id) throws VerifaliaException {
        return getOverview(id, null);
    }

    /**
     * Returns a lightweight {@link ValidationOverview} of an email validation job previously submitted for processing.
     * To retrieve the whole job data, including its results, use the {@link #get(String)} method instead.
     * @param id The {@link ValidationOverview#id} of the email validation job to retrieve.
     * @param waitingStrategy The {@link WaitingStrategy strategy} which rules out how to wait for the completion of the email validation.
     *                        Passing <tt>null</tt> makes the method avoid waiting for the job completion.
     * @return
     * @throws VerifaliaException
     */
    public ValidationOverview getOverview(@NonNull final String id, final WaitingStrategy waitingStrategy) throws VerifaliaException {
        ValidationOverview result = getOverviewOnce(id);

        if (waitingStrategy != null && waitingStrategy.waitForCompletion) {
            PollingTask<ValidationOverview> pollingTask = new PollingTask<>(id,
                    result,
                    new PollingCallback<ValidationOverview>() {
                        @Override
                        public ValidationOverview getOverview(ValidationOverview result) throws VerifaliaException {
                            return result;
                        }

                        @Override
                        public ValidationOverview refresh(String id) throws VerifaliaException {
                            return getOverviewOnce(id);
                        }
                    },
                    waitingStrategy);

            // Waits for the request completion or for the timeout to expire

            pollingTask.run();
            result = pollingTask.getResult();
        }

        return result;
    }

    private ValidationOverview getOverviewOnce(@NonNull final String id) throws VerifaliaException {
        // Build URL
        StringBuilder requestUrlBuilder = new StringBuilder();
        requestUrlBuilder.append("email-validations/");
        requestUrlBuilder.append(id);
        requestUrlBuilder.append("/overview");

        // Construct request object
        RestRequest request = new RestRequest(HttpRequestMethod.GET, requestUrlBuilder.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        // Handle response based on status code
        switch (response.getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_ACCEPTED:
            case HttpStatus.SC_GONE: {
                ValidationOverview data = response.deserialize(ValidationOverview.class);

                switch (response.getStatusCode()) {
                    case HttpStatus.SC_OK: {
                        data.setStatus(ValidationStatus.Completed);
                        break;
                    }
                    case HttpStatus.SC_ACCEPTED: {
                        data.setStatus(ValidationStatus.InProgress);
                        break;
                    }
                    case HttpStatus.SC_GONE: {
                        data.setStatus(ValidationStatus.Expired);
                        break;
                    }

                    default:
                        throw new IllegalStateException("Unexpected value: " + response.getStatusCode());
                }

                return data;
            }

            case HttpStatus.SC_NOT_FOUND: {
                return null;
            }

            default: {
                throw new VerifaliaException(response);
            }
        }
    }

    /**
     * Lists the validated entries for a given validation.
     * @param id The {@link ValidationOverview#id} of the email validation job to list the entries for.
     * @return An iterable collection of {@link ValidationEntry} items.
     * @throws VerifaliaException
     */
    public Iterable<ValidationEntry> listEntries(@NonNull final String id) throws VerifaliaException {
        return listEntries(id, null);
    }

    /**
     * Lists the validated entries for a given validation.
     * @param id The {@link ValidationOverview#id} of the email validation job to list the entries for.
     * @param options A {@link ValidationEntryListingOptions} representing the options for the listing operation.
     * @return An iterable collection of {@link ValidationEntry} items.
     * @throws VerifaliaException
     */
    public Iterable<ValidationEntry> listEntries(@NonNull final String id, final ValidationEntryListingOptions options) throws VerifaliaException {
        return IterableHelper.buildIterator(theOptions -> this.listEntriesSegmented(id, theOptions),
                cursor -> this.listEntriesSegmented(id, cursor),
                options);
    }

    private ListSegment<ValidationEntry> listEntriesSegmented(@NonNull final String id, final ValidationEntryListingOptions options) throws VerifaliaException {
        Map<String, String> paramMap = new HashMap<>();

        if (nonNull(options)) {
            if (options.getLimit() != null && options.getLimit() > 0) {
                paramMap.put("limit", (options.getLimit().toString()));
            }

            // Predicates

            if (options.getStatuses() != null) {
                for (FilterPredicateFragment fragment : options.getStatuses().serialize("status")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }
        }

        // Build request URI with the param map
        URI requestUri = Utils.getHttpUri(paramMap);

        // Build query entries resource string
        StringBuilder queryEntriesResource = new StringBuilder("email-validations/");
        queryEntriesResource.append(id);
        queryEntriesResource.append("/entries");

        if (nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())) {
            queryEntriesResource.append(requestUri.toString());
        }

        // Make request object for the rest call
        RestRequest request = new RestRequest(HttpRequestMethod.GET, queryEntriesResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        if (response.getStatusCode() != HttpStatus.SC_OK) {
            throw new VerifaliaException(response);
        }

        // Handle pagination with meta details
        return response.deserialize(ValidationEntryListSegment.class);
    }

    private ListSegment<ValidationEntry> listEntriesSegmented(@NonNull final String id, @NonNull final ListingCursor cursor) throws VerifaliaException {
        Map<String, String> paramMap = new HashMap<>();

        if (cursor.getDirection() == Direction.Forward) {
            paramMap.put("cursor", cursor.getCursor());
        } else {
            paramMap.put("cursor:prev", cursor.getCursor());
        }

        if (cursor.getLimit() != null && cursor.getLimit() > 0) {
            paramMap.put("limit", (cursor.getLimit().toString()));
        }

        // Build request URI with the param map
        URI requestUri = Utils.getHttpUri(paramMap);

        // Build query entries resource string
        StringBuilder queryEntriesResource = new StringBuilder("email-validations/");
        queryEntriesResource.append(id);
        queryEntriesResource.append("/entries");

        if (nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())) {
            queryEntriesResource.append(requestUri.toString());
        }

        // Make request object for the rest call
        RestRequest request = new RestRequest(HttpRequestMethod.GET, queryEntriesResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        if (response.getStatusCode() != HttpStatus.SC_OK) {
            throw new VerifaliaException(response);
        }

        // Handle pagination with meta details
        return response.deserialize(ValidationEntryListSegment.class);
    }

    // endregion

    // region Listing methods

    /**
     * Lists all the email validation jobs, from the oldest to the newest. Pass a {@link ValidationOverviewListingOptions}
     * to specify filters and a different sorting.
     * @return An iterable collection of {@link ValidationOverview} elements.
     * @throws VerifaliaException
     */
    public Iterable<ValidationOverview> list() throws VerifaliaException {
        return list(null);
    }

    /**
     * Lists all the email validation jobs, according to the listing specified options.
     * @param options A {@link ValidationOverviewListingOptions} representing the options for the listing operation.
     * @return An iterable collection of {@link ValidationOverview} elements.
     * @throws VerifaliaException
     */
    public Iterable<ValidationOverview> list(final ValidationOverviewListingOptions options) throws VerifaliaException {
        return IterableHelper.buildIterator(
                this::listSegmented,
                this::listSegmented,
                options);
    }

    private ListSegment<ValidationOverview> listSegmented(final ValidationOverviewListingOptions options) throws VerifaliaException {
        // Build query string param map
        Map<String, String> paramMap = new HashMap<>();

        if (nonNull(options)) {
            if (options.getLimit() != null && options.getLimit() > 0) {
                paramMap.put("limit", (options.getLimit().toString()));
            }

            // Predicates

            if (options.getCreatedOn() != null) {
                for (FilterPredicateFragment fragment : options.getCreatedOn().serialize("createdOn")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }
            if (options.getStatuses() != null) {
                for (FilterPredicateFragment fragment : options.getStatuses().serialize("status")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }
            if (options.getOwner() != null) {
                for (FilterPredicateFragment fragment : options.getOwner().serialize("owner")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }

            // Sort

            if (nonNull(options.getOrderBy())) {
                switch (options.getOrderBy()) {
                    case CreatedOn:
                        paramMap.put("sort", (options.getDirection() == Direction.Backward ? "-" : "") + "createdOn");
                        break;

                    default:
                        throw new NotImplementedException();
                }
            }
        }

        // Build request URI with the param map
        URI requestUri = Utils.getHttpUri(paramMap);

        // Build query entries resource string
        StringBuilder listJobsResource = new StringBuilder("email-validations");
        if (nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())) {
            listJobsResource.append(requestUri.toString());
        }

        // Make request object for the rest call
        RestRequest request = new RestRequest(HttpRequestMethod.GET, listJobsResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        // Handle pagination with meta details
        return response.deserialize(ValidationOverviewListSegment.class);
    }

    private ListSegment<ValidationOverview> listSegmented(@NonNull final ListingCursor cursor) throws VerifaliaException {
        // Build query string param map
        Map<String, String> paramMap = new HashMap<>();

        if (cursor.getDirection() == Direction.Forward) {
            paramMap.put("cursor", cursor.getCursor());
        } else {
            paramMap.put("cursor:prev", cursor.getCursor());
        }

        if (cursor.getLimit() != null && cursor.getLimit() > 0) {
            paramMap.put("limit", (cursor.getLimit().toString()));
        }

        // Build request URI with the param map
        URI requestUri = Utils.getHttpUri(paramMap);

        // Build query entries resource string
        StringBuilder listJobsResource = new StringBuilder("email-validations");
        if (nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())) {
            listJobsResource.append(requestUri.toString());
        }

        // Make request object for the rest call
        RestRequest request = new RestRequest(HttpRequestMethod.GET, listJobsResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request);

        // Handle pagination with meta details
        return response.deserialize(ValidationOverviewListSegment.class);
    }

    private Validation mapValidationMapperToValidation(ValidationMapper validationMapper) {
        Validation validation = null;
        if (nonNull(validationMapper)) {
            validation = new Validation();
            if (nonNull(validationMapper.getOverview())) {
                validation.setOverview(validationMapper.getOverview());
            }
            if (nonNull(validationMapper.getEntries())) {
                validation.setEntries(validationMapper.getEntries().getData());
            }
        }
        return validation;
    }

    private abstract static class PollingCallback<T> {
        public abstract ValidationOverview getOverview(T result) throws VerifaliaException;

        public abstract T refresh(String id) throws VerifaliaException;
    }

    private static class PollingTask<T> implements Runnable {
        private final String id;
        private final WaitingStrategy waitingStrategy;
        private final PollingCallback<T> callback;
        private T result;
        private VerifaliaException exception;

        public PollingTask(String id, final T initialResult, PollingCallback<T> callback, WaitingStrategy waitingStrategy) {
            this.id = id;
            this.result = initialResult;
            this.callback = callback;
            this.waitingStrategy = waitingStrategy;
        }

        public void run() {
            try {
                do {
                    // A null result means the validation has not been found
                    if (result == null)
                        return;

                    // Returns immediately if the validation has been completed

                    ValidationOverview overview = this.callback.getOverview(result);

                    if (overview.getStatus() != ValidationStatus.InProgress)
                        return;

                    // Provides progress updates to the eventual subscriber

                    if (waitingStrategy.progressProvider != null) {
                        waitingStrategy.progressProvider.report(overview);
                    }

                    // Wait for the polling interval

                    waitingStrategy.waitForNextPoll(overview);

                    result = this.callback.refresh(id);
                } while (true);
            } catch (VerifaliaException exception) {
                this.exception = exception;
            } catch (Exception exception) {
                // Special handling for unhandled exceptions - for example, those thrown by a user-provided waiting strategy

                this.exception = new WaitingInterruptedException("An unhandled exception was thrown while waiting for a job completion.", exception);
            }
        }

        public T getResult() throws VerifaliaException {
            // Handles any eventual exception

            if (exception != null) {
                throw exception;
            }

            return result;
        }
    }

    // endregion

    // region Deletion methods

    /**
     * Deletes an email validation job previously submitted for processing.
     * @param id The {@link ValidationOverview#id} of the email validation job to delete.
     * @throws VerifaliaException
     */
    public void delete(@NonNull final String id) throws VerifaliaException {
        // Make request
        RestRequest request = new RestRequest(HttpRequestMethod.DELETE, "email-validations/" + id);

        // Sends the request to the Verifalia servers
        restClient.execute(request);
    }

    // endregion

    private static class ValidationEntryListSegment extends ListSegment<ValidationEntry> {
    }

    public static class ValidationOverviewListSegment extends ListSegment<ValidationOverview> {
    }

    @Getter
    @Setter
    @ToString
    private static class ValidationEntries {

        /**
         * Meta information for the validation entries
         */
        private ListSegmentMeta meta;

        /**
         * List of all the validation entry data object submitted with the request
         */
        private List<ValidationEntry> data;
    }

    @Getter
    @Setter
    @ToString
    private static class ValidationMapper {

        /**
         * The overview of the email validation batch.
         */
        private ValidationOverview overview;

        /**
         * Validation entries submiited for the job
         */
        private ValidationEntries entries;
    }
}
