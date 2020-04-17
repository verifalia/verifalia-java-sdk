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

import com.verifalia.api.common.Direction;
import com.verifalia.api.common.IterableHelper;
import com.verifalia.api.common.ListingCursor;
import com.verifalia.api.common.Utils;
import com.verifalia.api.common.filters.FilterPredicateSegment;
import com.verifalia.api.common.models.ListSegment;
import com.verifalia.api.common.models.ListSegmentMeta;
import com.verifalia.api.emailvalidations.models.*;
import com.verifalia.api.exceptions.InsufficientCreditException;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.exceptions.WaitingInterruptedException;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Allows to submit and manage email validations using the Verifalia service.
 * <p>The functionalities of this type are exposed by way of the {@link com.verifalia.api.VerifaliaRestClient#getEmailValidations getmailValidations()}
 * of {@link com.verifalia.api.VerifaliaRestClient VerifaliaRestClient}.
 */
public class EmailValidationsRestClient {
    private final RestClient restClient;

    public EmailValidationsRestClient(@NonNull final RestClient restClient) {
        this.restClient = restClient;
    }

    // region Submission methods

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property
     * is {@link ValidationStatus#Completed Completed}. Use the {@link EmailValidationsRestClient#submit(String, WaitForCompletionOptions)}
     * to wait for the completion of the batch without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String) query}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddress Email address to validate.
     * @return An object representing the email validation batch.
     * @throws VerifaliaException
     * @throws IOException
     */
    @SuppressWarnings("serial")
    public Validation submit(@NonNull final String emailAddress) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddress));
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property
     * is {@link ValidationStatus#Completed Completed}. Use the {@link EmailValidationsRestClient#submit(String[], WaitForCompletionOptions)}
     * to wait for the completion of the batch without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String) query}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @return An object representing the email validation batch.
     * @throws VerifaliaException
     * @throws IOException
     */
    public Validation submit(@NonNull final String[] emailAddresses) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses));
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property
     * is {@link ValidationStatus#Completed Completed}. Use the {@link EmailValidationsRestClient#submit(com.verifalia.api.emailvalidations.models.ValidationRequest, WaitForCompletionOptions)}
     * to wait for the completion of the batch without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String) query}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @return An object representing the email validation batch
     * @throws VerifaliaException
     * @throws IOException
     */
    public Validation submit(@NonNull final Iterable<String> emailAddresses) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses));
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses           A collection of email addresses to validate
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, @NonNull final WaitingStrategy waitingStrategy)
            throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses), waitingStrategy);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddress             Email addresses to validate
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    @SuppressWarnings("serial")
    public Validation submit(@NonNull final String emailAddress, @NonNull final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddress), waitingStrategy);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses           A collection of email addresses to validate
     * @param deduplication            Validation deduplication based on which request needs to be proceessed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, @NonNull final DeduplicationMode deduplication,
                             @NonNull WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses, deduplication), waitingStrategy);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddress             Email addresses to validate
     * @param quality                  Validation quality based on which request needs to be processed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    @SuppressWarnings("serial")
    public Validation submit(@NonNull final String emailAddress, @NonNull final QualityLevelName quality,
                             @NonNull final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddress, quality), waitingStrategy);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses           A collection of email addresses to validate
     * @param quality                  Validation quality based on which request needs to be processed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, @NonNull final QualityLevelName quality,
                             @NonNull final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses, quality), waitingStrategy);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses           A collection of email addresses to validate
     * @param quality                  Validation quality based on which request needs to be processed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final String[] emailAddresses, @NonNull final QualityLevelName quality,
                             @NonNull final DeduplicationMode deduplication,
                             @NonNull final WaitingStrategy waitingStrategy) throws VerifaliaException {
        return submit(new ValidationRequest(emailAddresses, quality, deduplication), waitingStrategy);
    }

    public Validation submit(@NonNull final ValidationRequest validationRequest) throws VerifaliaException {
        return submit(validationRequest, new WaitingStrategy(true));
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the {@code waitForCompletionOptions} parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#get(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param validationRequest        An object representing the input for email validation requests
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return Validation An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(@NonNull final ValidationRequest validationRequest, @NonNull final WaitingStrategy waitingStrategy) throws VerifaliaException {
        // Checks the parameters

        if (validationRequest.getEntries() == null) {
            throw new IllegalArgumentException("emailAddresses cannot be null");
        }
        if (validationRequest.getEntries().size() == 0) {
            throw new IllegalArgumentException("Can't validate an empty batch (emailAddresses)");
        }

        // Build the REST request
        RestRequest request = new RestRequest(HttpRequestMethod.POST, "email-validations", validationRequest);

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

                if (!waitingStrategy.waitForCompletion) {
                    validation.getOverview().setStatus(ValidationStatus.InProgress);
                    return validation;
                }

                // Poll the service until completion

                return get(validation.getOverview().getId(), waitingStrategy);
            }

            case HttpStatus.SC_PAYMENT_REQUIRED: {
                throw new InsufficientCreditException(response);
            }

            default: {
                throw new VerifaliaException(response);
            }
        }
    }


    // endregion

    // region Retrieval methods

    /**
     * Returns an object representing an email validation batch, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @param id The identifier for an email validation batch to be retrieved.
     * @return Validation An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation get(@NonNull final String id) throws VerifaliaException {
        return get(id, new WaitingStrategy(true));
    }

    /**
     * Returns an object representing an email validation batch, waiting for its completion and issuing multiple retries if needed.
     * Makes a GET request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @param id                       The identifier for an email validation batch to be retrieved.
     * @param waitOptions              The options about waiting for the validation completion.
     * @param pollingLoopEventListener Polling loop event listener, may be <b>null</b>.
     * @return An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation get(@NonNull final String id, @NonNull final WaitingStrategy waitingStrategy) throws VerifaliaException {

        // Handle the case when the client wishes to avoid waiting for completion

        Validation result = getOnce(id);

        if (waitingStrategy.waitForCompletion) {
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

    /**
     * Returns an object representing an email validation batch, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @param id The identifier for an email validation batch to be retrieved.
     * @return An object representing the current status of the requested email validation batch.
     */
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

    public ValidationOverview getOverview(@NonNull final String id) throws VerifaliaException {
        return getOverview(id, new WaitingStrategy(true));
    }

    public ValidationOverview getOverview(@NonNull final String id, @NonNull final WaitingStrategy waitingStrategy) throws VerifaliaException {
        ValidationOverview result = getOverviewOnce(id);

        if (waitingStrategy.waitForCompletion) {
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

    /**
     * Returns an object representing an email validation batch overview, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}/overview"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @param id The identifier for an email validation batch to be retrieved.
     * @return ValidationOverview An object representing the overview of the requested email validation batch.
     */
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
     * Returns an object representing an email validation batch entries, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}/entries"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @param id The identifier for an email validation batch to be retrieved.
     * @return List<ValidationEntry> An object list representing the entries of the requested email validation batch.
     */
    public Iterable<ValidationEntry> listEntries(@NonNull final String id) throws VerifaliaException {
        return listEntries(id, null);
    }

    /**
     * Returns an object representing an email validation batch entries, identified by the specified unique identifier and with specified statuses.
     * Makes a GET request to the <b>"/email-validations/{id}/entries"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @param id      The identifier for an email validation batch to be retrieved.
     * @param options An object with the various filters mentioned when retrieving entries.
     * @return List<ValidationEntry> An object list representing the entries of the requested email validation batch.
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
                for (FilterPredicateSegment fragment : options.getStatuses().serialize("status")) {
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

    /**
     * Returns an object representing the various email validations jobs initiated.
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public Iterable<ValidationOverview> list() throws VerifaliaException {
        return list(null);
    }

    /**
     * Returns an object representing the various email validations jobs initiated based on filter and sort options passed.
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     *
     * @param validationJobFilter Object with options for filters and sort options supported.
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public Iterable<ValidationOverview> list(final ValidationOverviewListingOptions options) throws VerifaliaException {
        return IterableHelper.buildIterator(
                this::listSegmented,
                this::listSegmented,
                options);
    }

    // endregion

    // region Listing methods

    private ListSegment<ValidationOverview> listSegmented(final ValidationOverviewListingOptions options) throws VerifaliaException {
        // Build query string param map
        Map<String, String> paramMap = new HashMap<>();

        if (nonNull(options)) {
            if (options.getLimit() != null && options.getLimit() > 0) {
                paramMap.put("limit", (options.getLimit().toString()));
            }

            // Predicates

            if (options.getCreatedOn() != null) {
                for (FilterPredicateSegment fragment : options.getCreatedOn().serialize("createdOn")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }
            if (options.getStatuses() != null) {
                for (FilterPredicateSegment fragment : options.getStatuses().serialize("status")) {
                    paramMap.put(fragment.getKey(), fragment.getValue());
                }
            }
            if (options.getOwner() != null) {
                for (FilterPredicateSegment fragment : options.getOwner().serialize("owner")) {
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

    /**
     * Deletes an email validation batch, identified by the specified unique identifier.
     * Makes a DELETE request to the <b>"/email-validations/{id}"</b> resource.
     *
     * @param id The identifier for an email validation batch to be deleted.
     * @throws VerifaliaException
     */
    public void delete(@NonNull final String id) throws VerifaliaException {
        // Make request
        RestRequest request = new RestRequest(HttpRequestMethod.DELETE, "email-validations/" + id);

        // Sends the request to the Verifalia servers
        restClient.execute(request);
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

    /**
     * Polling thread task
     */
    private static class PollingTask<T> implements Runnable {
        private final String id;
        private final WaitingStrategy waitingStrategy;
        /**
         * Query result
         */
        private T result;
        /*
         * Thread exception
         */
        private VerifaliaException exception;
        private final PollingCallback<T> callback;

        public PollingTask(String id, final T initialResult, PollingCallback<T> callback, WaitingStrategy waitingStrategy) {
            this.id = id;
            this.result = initialResult;
            this.callback = callback;
            this.waitingStrategy = waitingStrategy;
        }

        /**
         * Main thread method
         */
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

    private static class ValidationEntryListSegment extends ListSegment<ValidationEntry> {
    }

    // endregion

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
