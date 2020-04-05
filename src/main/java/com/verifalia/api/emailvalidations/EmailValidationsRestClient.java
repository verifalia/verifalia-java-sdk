package com.verifalia.api.emailvalidations;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.Constants;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.common.ServerPollingLoopEventListener.ServerPollingLoopEvent;
import com.verifalia.api.common.Utils;
import com.verifalia.api.common.models.ResponseMeta;
import com.verifalia.api.emailvalidations.models.ValidationDeDuplication;
import com.verifalia.api.emailvalidations.models.ValidationEntriesFilter;
import com.verifalia.api.emailvalidations.models.ValidationEntryStatus;
import com.verifalia.api.emailvalidations.models.ValidationJobsFilter;
import com.verifalia.api.emailvalidations.models.ValidationJobsSort;
import com.verifalia.api.emailvalidations.models.ValidationStatus;
import com.verifalia.api.emailvalidations.models.input.ValidationEntryInput;
import com.verifalia.api.emailvalidations.models.input.ValidationInput;
import com.verifalia.api.emailvalidations.models.output.Validation;
import com.verifalia.api.emailvalidations.models.output.ValidationEntries;
import com.verifalia.api.emailvalidations.models.output.ValidationEntry;
import com.verifalia.api.emailvalidations.models.output.ValidationJobs;
import com.verifalia.api.emailvalidations.models.output.ValidationOverview;
import com.verifalia.api.exceptions.AuthorizationException;
import com.verifalia.api.exceptions.InsufficientCreditException;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.HttpRequestMethod;
import com.verifalia.api.rest.HttpStatusCode;
import com.verifalia.api.rest.RestClient;
import com.verifalia.api.rest.RestRequest;
import com.verifalia.api.rest.RestResponse;

/**
 * Allows to submit and manage email validations using the Verifalia service.
 * <p>The functionalities of this type are exposed by way of the {@link com.verifalia.api.VerifaliaRestClient#getEmailValidations getmailValidations()}
 * of {@link com.verifalia.api.VerifaliaRestClient VerifaliaRestClient}.
 */
public class EmailValidationsRestClient {
    private RestClient restClient;

    public EmailValidationsRestClient(RestClient restClient) {
        if (restClient == null)
            throw new IllegalArgumentException("restClient is null.");

        this.restClient = restClient;
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property
     * is {@link ValidationStatus#Completed Completed}. Use the {@link EmailValidationsRestClient#submit(String[], WaitForCompletionOptions)}
     * to wait for the completion of the batch without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String) query}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @return An object representing the email validation batch.
     * @throws VerifaliaException
     * @throws IOException
     */
    public Validation submit(String[] emailAddresses) throws IOException, VerifaliaException {
    	ValidationInput validationInput = getValidationInput(Arrays.asList(emailAddresses));
        return submit(validationInput, WaitForCompletionOptions.DontWait);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property
     * is {@link ValidationStatus#Completed Completed}. Use the {@link EmailValidationsRestClient#submit(com.verifalia.api.emailvalidations.models.input.ValidationInput, WaitForCompletionOptions)}
     * to wait for the completion of the batch without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String) query}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @return An object representing the email validation batch
     * @throws VerifaliaException
     * @throws IOException
     */
    public Validation submit(Iterable<String> emailAddresses) throws IOException, VerifaliaException {
    	ValidationInput validationInput = getValidationInput(emailAddresses);
        return submit(validationInput, WaitForCompletionOptions.DontWait);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(String[] emailAddresses, WaitForCompletionOptions waitForCompletionOptions)
    		throws IOException, VerifaliaException {
    	ValidationInput validationInput = getValidationInput(Arrays.asList(emailAddresses));
    	return submit(validationInput, waitForCompletionOptions);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @param quality Validation quality based on which request needs to be proceessed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(String[] emailAddresses, String quality, WaitForCompletionOptions waitForCompletionOptions)
    		throws IOException, VerifaliaException {
    	ValidationInput validationInput = getValidationInput(Arrays.asList(emailAddresses), quality);
    	return submit(validationInput, waitForCompletionOptions);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @param deDuplication Validation deduplication based on which request needs to be proceessed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(String[] emailAddresses, ValidationDeDuplication deDuplication,
    		WaitForCompletionOptions waitForCompletionOptions) throws IOException, VerifaliaException {
    	ValidationInput validationInput = getValidationInput(Arrays.asList(emailAddresses), deDuplication);
    	return submit(validationInput, waitForCompletionOptions);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @param quality Validation quality based on which request needs to be processed
     * @param deDuplication Validation de-duplication based on which request needs to be processed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(String[] emailAddresses, String quality, ValidationDeDuplication deDuplication,
    		WaitForCompletionOptions waitForCompletionOptions) throws IOException, VerifaliaException {
    	ValidationInput validationInput = getValidationInput(Arrays.asList(emailAddresses), quality, deDuplication);
    	return submit(validationInput, waitForCompletionOptions);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @param quality Validation quality based on which request needs to be processed
     * @param deDuplication Validation de-duplication based on which request needs to be processed
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(String[] emailAddresses, String quality, ValidationDeDuplication deDuplication,
    		Integer priority, WaitForCompletionOptions waitForCompletionOptions) throws IOException, VerifaliaException {
    	ValidationInput validationInput = getValidationInput(Arrays.asList(emailAddresses), quality, deDuplication, priority);
    	return submit(validationInput, waitForCompletionOptions);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#InProgress InProgress} status.
     * Validations are completed only when their {@link ValidationOverview#status} property.
     * is {@link ValidationStatus#Completed Completed}; the {@code waitForCompletionOptions} parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link ValidationOverview#id}.
     *
     * @param validationInput An object representing the input for email validation requests
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return Validation An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(ValidationInput validationInput, WaitForCompletionOptions waitForCompletionOptions) throws IOException, VerifaliaException {
        if (waitForCompletionOptions == null)
            throw new IllegalArgumentException("waitForCompletionOptions");
        if(validateEmailValidationInputs(validationInput)){ // If validation input object is valid
 	        // Build the REST request
	        String requestData = Utils.convertObjectToJsonString(validationInput);
	        RestRequest request = new RestRequest(HttpRequestMethod.POST, Constants.EMAIL_VALIDATIONS_RESOURCE, requestData);

	        // Send the request to the Verifalia servers
	        RestResponse response = restClient.execute(request, Validation.class);
	        Validation data = (Validation)response.getData();

	        // Handle response based on status code
	        switch (response.getStatusCode()) {
	            case HttpStatusCode.OK: {
	                // The batch has been completed in real time
	                data.getOverview().setStatus(ValidationStatus.Completed);
	                return data;
	            }

	            case HttpStatusCode.ACCEPTED: {
	                // The batch has been accepted but is not yet completed
	                if (waitForCompletionOptions == WaitForCompletionOptions.DontWait) {
	                	data.getOverview().setStatus(ValidationStatus.InProgress);
	                    return data;
	                } else {
		                // Poll the service until completion
		                return query(data.getOverview().getId(), waitForCompletionOptions);
	                }
	            }

	            case HttpStatusCode.UNAUTHORIZED: {
	                // The batch has NOT been accepted because of an issue with the supplied credentials
	                throw new AuthorizationException(response);
	            }

	            case HttpStatusCode.PAYMENT_REQUIRED: {
	                // The batch has NOT been accepted because of low account credit
	                throw new InsufficientCreditException(response);
	            }

	            default: {
	                // An unhandled exception happened at the Verifalia side
	                throw new VerifaliaException(response);
	            }
	        }
        }
        return null;
    }

    private ValidationInput getValidationInput(Iterable<String> emailAddresses){
    	return getValidationInput(emailAddresses, null, null, null);
    }

    private ValidationInput getValidationInput(Iterable<String> emailAddresses, String quality){
    	return getValidationInput(emailAddresses, quality, null, null);
    }

    private ValidationInput getValidationInput(Iterable<String> emailAddresses, ValidationDeDuplication deDuplication){
    	return getValidationInput(emailAddresses, null, deDuplication, null);
    }

    private ValidationInput getValidationInput(Iterable<String> emailAddresses, String quality,
    		ValidationDeDuplication deDuplication){
    	return getValidationInput(emailAddresses, quality, deDuplication, null);
    }

    private ValidationInput getValidationInput(Iterable<String> emailAddresses, String quality,
    		ValidationDeDuplication deDuplication, Integer priority){
    	// Populate validation input object
    	List<ValidationEntryInput> entries = new ArrayList<ValidationEntryInput>();
    	for(String emailAddress: emailAddresses){
    		entries.add(new ValidationEntryInput(StringUtils.defaultString(emailAddress)));
    	}
    	ValidationInput validationInput = new ValidationInput();
    	validationInput.setEntries(entries);
    	if(nonNull(quality)){
    		validationInput.setQuality(quality);
    	}
    	if(nonNull(deDuplication)){
    		validationInput.setDeduplication(deDuplication);
    	}
    	if(nonNull(priority)){
    		validationInput.setPriority(priority);
    	}
    	return validationInput;
    }

    private boolean validateEmailValidationInputs(ValidationInput validationInput){
    	if (validationInput.getEntries() == null) {
    		throw new IllegalArgumentException("emailAddresses cannot be null");
    	}
    	if (validationInput.getEntries().size() == 0){
            throw new IllegalArgumentException("Can't validate an empty batch (emailAddresses)");
    	}
    	if(nonNull(validationInput.getPriority())){
    		if(validationInput.getPriority() < Constants.VALIDATION_INPUT_PRIORITY_MIN_VALUE
    				|| validationInput.getPriority() > Constants.VALIDATION_INPUT_PRIORITY_MAX_VALUE){
    			throw new IllegalArgumentException("Invalid priority value. It must be in range 0 to 255");
    		}
    	}
    	return true;
    }

    /**
     * Returns an object representing an email validation batch, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @return Validation An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation query(String id) throws IOException {
        return query(id, WaitForCompletionOptions.DontWait);
    }

    /**
     * Returns an object representing an email validation batch, waiting for its completion and issuing multiple retries if needed.
     * Makes a GET request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @param waitOptions The options about waiting for the validation completion.
     * @return Validation An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation query(final String id, final WaitForCompletionOptions waitOptions) throws IOException {
    	return query(id, waitOptions, null);
    }

    /**
     * Returns an object representing an email validation batch, waiting for its completion and issuing multiple retries if needed.
     * Makes a GET request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @param waitOptions The options about waiting for the validation completion.
     * @param pollingLoopEventListener Polling loop event listener, may be <b>null</b>.
     * @return An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation query(final String id, final WaitForCompletionOptions waitOptions,
    		final ServerPollingLoopEventListener pollingLoopEventListener) throws IOException {
        // Handle the case when the client wishes to avoid waiting for completion
        if (waitOptions == WaitForCompletionOptions.DontWait)
            return queryOnce(id);

        // The client needs to wait for completion

        /**
         * Polling thread task
         */
        class PollingTask implements Runnable {

	        /**
	         * Main thread method
	         */
        	public void run() {
        		if(pollingLoopEventListener != null)
        			pollingLoopEventListener.onPollingLoopEvent(ServerPollingLoopEvent.ServerPollingLoopStarted, result);

        		runActually();

        		if(pollingLoopEventListener != null)
        			pollingLoopEventListener.onPollingLoopEvent(ServerPollingLoopEvent.ServerPollingLoopFinished, result);
	        }

        	/**
        	 * Implements actual activities
        	 */
        	void runActually() {

        		try {
	        		long startTime = (new Date()).getTime();
	        		long timeout = waitOptions.getTimeout() * 1000;
		            do {
		            	if(pollingLoopEventListener != null)
		            		pollingLoopEventListener.onPollingLoopEvent(ServerPollingLoopEvent.BeforePollServer, result);

		                result = queryOnce(id);

		                if(pollingLoopEventListener != null)
		            		pollingLoopEventListener.onPollingLoopEvent(ServerPollingLoopEvent.AfterPollServer, result);

		                // A null result means the validation has not been found
		                if (result == null)
		                	return;

		                // Returns immediately if the validation has been completed
		                if (result.getOverview().getStatus() == ValidationStatus.Completed)
		                    return;

		                // Wait for the polling interval
		                try {
							Thread.sleep(waitOptions.getPollingInterval()*1000);
						} catch (InterruptedException e) {
							if(Thread.currentThread().isInterrupted())
								return;
						}
		            } while(startTime + timeout > (new Date()).getTime());
        		}
        		catch(IOException ex) {
        			this.exception = ex;
        			return;
        		}
	        }

        	/**
        	 * Returns polling result
        	 */
        	public Validation getResult() {
        		return result;
        	}

        	/*
        	 * Returns flag whether task have been completed
        	 */
        	public boolean isCompleted() {
        		return result != null && result.getOverview().getStatus() == ValidationStatus.Completed;
        	}

        	/*
        	 * Returns flag whether task has faulted
        	 */
        	public boolean isFaulted() {
        		return exception != null;
        	}

        	/**
        	 * Returns the underlying exception
        	 */
        	public IOException getException() {
        		return exception;
        	}

        	/**
        	 * Query result
        	 */
        	private Validation result;

        	/*
        	 * Thread exception
        	 */
        	private IOException exception;
        };

        PollingTask pollingTask = new PollingTask();

        // Waits for the request completion or for the timeout to expire
        pollingTask.run();

        // Handles any eventual exception
        if (pollingTask.isFaulted())
            throw pollingTask.getException();

        if (pollingTask.isCompleted())
            return pollingTask.getResult();

        // A timeout occurred
        return null;
    }

    /**
     * Returns an object representing an email validation batch, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @return An object representing the current status of the requested email validation batch.
     */
    public Validation queryOnce(String id) throws IOException {
    	if(!StringUtils.isBlank(id)){
    		// Build request
	        RestRequest request = new RestRequest(HttpRequestMethod.GET, Constants.EMAIL_VALIDATIONS_RESOURCE + "/" + id);

	        // Sends the request to the Verifalia servers
	        RestResponse response = restClient.execute(request, Validation.class);
	        Validation data = (Validation) response.getData();

	        // Handle response based on status code
	        switch (response.getStatusCode()) {
	            case HttpStatusCode.OK: {
	            	data.getOverview().setStatus(ValidationStatus.Completed);
	            	return data;
	            }

	            case HttpStatusCode.ACCEPTED: {
	                data.getOverview().setStatus(ValidationStatus.InProgress);
	                return data;
	            }

	            case HttpStatusCode.GONE:
	            case HttpStatusCode.NOT_FOUND: {
	                return null;
	            }

	            default:
	            	throw new VerifaliaException(response);
	        }
    	} else {
    		throw new IllegalArgumentException("Job ID cannot be blank");
    	}
    }

    /**
     * Returns an object representing an email validation batch overview, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}/overview"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @return ValidationOverview An object representing the overview of the requested email validation batch.
     */
    public ValidationOverview queryOverview(String id) throws IOException {
    	if(!StringUtils.isBlank(id)){
    		// Build URL
    		StringBuilder requestUrlBuilder = new StringBuilder();
    		requestUrlBuilder.append(Constants.EMAIL_VALIDATIONS_RESOURCE);
    		requestUrlBuilder.append("/");
    		requestUrlBuilder.append(id);
    		requestUrlBuilder.append("/");
    		requestUrlBuilder.append(Constants.EMAIL_VALIDATIONS_OVERVIEW_RESOURCE);

    		// Construct request object
	        RestRequest request = new RestRequest(HttpRequestMethod.GET, requestUrlBuilder.toString());

	        // Sends the request to the Verifalia servers
	        RestResponse response = restClient.execute(request, ValidationOverview.class);
	        ValidationOverview data = (ValidationOverview) response.getData();

	        // Handle response based on status code
	        switch (response.getStatusCode()) {
	            case HttpStatusCode.OK: {
	            	data.setStatus(ValidationStatus.Completed);
	                return data;
	            }

	            case HttpStatusCode.ACCEPTED: {
	            	data.setStatus(ValidationStatus.InProgress);
	                return data;
	            }

	            case HttpStatusCode.GONE:
	            case HttpStatusCode.NOT_FOUND: {
	                return null;
	            }

	            default:
	            	throw new VerifaliaException(response);
	        }
    	} else {
    		throw new IllegalArgumentException("Job ID cannot be blank");
    	}
    }

    /**
     * Returns an object representing an email validation batch entries, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}/entries"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @return List<ValidationEntry> An object list representing the entries of the requested email validation batch.
     */
    public List<ValidationEntry> queryEntries(String id) throws IOException {
    	ValidationEntriesFilter validationEntriesFilter = null;
    	return queryEntries(id, validationEntriesFilter);
    }

    /**
     * Returns an object representing an email validation batch entries, identified by the specified unique identifier and with specified statuses.
     * Makes a GET request to the <b>"/email-validations/{id}/entries"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @param statuses A collection of statuses for which the entries needs to be retrieved.
     * @return List<ValidationEntry> An object list representing the entries of the requested email validation batch.
     */
    public List<ValidationEntry> queryEntries(String id, ValidationEntryStatus[] statuses) throws IOException {
    	ValidationEntriesFilter validationEntriesFilter = new ValidationEntriesFilter();
    	validationEntriesFilter.setStatuses(Arrays.asList(statuses));
    	return queryEntries(id, validationEntriesFilter);
    }

    /**
     * Returns an object representing an email validation batch entries, identified by the specified unique identifier and with specified statuses.
     * Makes a GET request to the <b>"/email-validations/{id}/entries"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @param statuses A collection of statuses for which the entries needs to be retrieved.
     * @return List<ValidationEntry> An object list representing the entries of the requested email validation batch.
     */
    public List<ValidationEntry> queryEntries(String id, Iterable<ValidationEntryStatus> statuses) throws IOException {
    	ValidationEntriesFilter validationEntriesFilter = new ValidationEntriesFilter();
    	validationEntriesFilter.setStatuses(statuses);
    	return queryEntries(id, validationEntriesFilter);
    }

    /**
     * Returns an object representing an email validation batch entries, identified by the specified unique identifier and with specified statuses.
     * Makes a GET request to the <b>"/email-validations/{id}/entries"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @param validationEntriesFilter An object with the various filters mentioned when retrieving entries.
     * @return List<ValidationEntry> An object list representing the entries of the requested email validation batch.
     */
    public List<ValidationEntry> queryEntries(String id, ValidationEntriesFilter validationEntriesFilter) throws IOException {
    	if(!StringUtils.isBlank(id)){
    		// Assign with default values to handle pagination
        	String cursor = StringUtils.EMPTY;
        	Boolean isTruncated = Boolean.TRUE;
        	List<ValidationEntry> validationEntriesData = new ArrayList<ValidationEntry>();

        	// Run through responses to handle pagination
        	while(nonNull(isTruncated) && isTruncated){
		    	// Build query string parameters map
		    	Map<String, String> paramMap = getValidationEntriesParamMap(validationEntriesFilter, cursor);

		    	// Build request URI with the param map
		    	URI requestUri = Utils.getHttpUri(null, null, null, paramMap);

		    	// Build query entries resource string
		    	StringBuilder queryEntriesResource = new StringBuilder(Constants.EMAIL_VALIDATIONS_RESOURCE);
		    	queryEntriesResource.append("/");
		    	queryEntriesResource.append(id);
		    	queryEntriesResource.append("/");
		    	queryEntriesResource.append(Constants.EMAIL_VALIDATIONS_ENTRIES_RESOURCE);
		    	if(nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())){
		    		queryEntriesResource.append(requestUri.toString());
		    	}
		    	System.out.println("Query entries URI: " + queryEntriesResource.toString());

		    	// Make request object for the rest call
		    	RestRequest request = new RestRequest(HttpRequestMethod.GET, queryEntriesResource.toString());

		        // Sends the request to the Verifalia servers
		        RestResponse response = restClient.execute(request, ValidationEntries.class);
		        int responseStatusCode = response.getStatusCode();

		        // Handle response based on status code
		        if(responseStatusCode == HttpStatusCode.OK || responseStatusCode == HttpStatusCode.ACCEPTED){
		        	// Handle pagination with meta details
			        ValidationEntries validationEntries = (ValidationEntries) response.getData();
			        if(nonNull(validationEntries.getData())){
			        	validationEntriesData.addAll(validationEntries.getData());
			        } else {
			        	System.out.println("No validation entries found");
			        }
			        ResponseMeta meta = validationEntries.getMeta();
			        if(nonNull(meta)){
			        	isTruncated = meta.getIsTruncated();
			        	cursor = meta.getCursor();
			        } else {
			        	System.out.println("No meta details found");
			        	isTruncated = Boolean.FALSE;
			        }
		        } else if(responseStatusCode == HttpStatusCode.GONE || responseStatusCode == HttpStatusCode.NOT_FOUND){
		        	return null;
		        } else {
		        	throw new VerifaliaException(response);
		        }
	        }
        	return validationEntriesData;
    	} else {
    		throw new IllegalArgumentException("Job ID cannot be blank");
    	}
    }

    private Map<String, String> getValidationEntriesParamMap(ValidationEntriesFilter validationEntriesFilter, String cursor){
    	Map<String, String> paramMap = new HashMap<String, String>();

    	// Add cursor as param for handling pagination. If cursor is passed, no need to pass other params as per the documentation.
    	if(!StringUtils.isBlank(cursor)){
			paramMap.put(Constants.API_PARAM_CURSOR, cursor);
			if(nonNull(validationEntriesFilter) && nonNull(validationEntriesFilter.getLimit())){
				paramMap.put("limit", validationEntriesFilter.getLimit().toString());
			}
		} else {
	    	if(nonNull(validationEntriesFilter)){
	    		if(validateEntriesFilterInputs(validationEntriesFilter)){
	    			// Limit filter
	    			if(nonNull(validationEntriesFilter.getLimit())){
	    				paramMap.put("limit", validationEntriesFilter.getLimit().toString());
	    			}
			    	// Status filter
			    	String statusesStr = convertValidationEntryDataStatusEnumIteratorToString(validationEntriesFilter.getStatuses(),
			    			Constants.STRING_SEPERATOR_COMMA);
			    	if(!StringUtils.isBlank(statusesStr)){
			    		paramMap.put("status", statusesStr);
			    	}
			    	// Exclude status filter
			    	String excludeStatusesStr = convertValidationEntryDataStatusEnumIteratorToString(validationEntriesFilter.getExcludeStatuses(),
			    			Constants.STRING_SEPERATOR_COMMA);
			    	if(!StringUtils.isBlank(excludeStatusesStr)){
			    		paramMap.put("status:exclude", excludeStatusesStr);
			    	}
	    		}
	    	}
		}
    	return paramMap;
    }

    private boolean validateEntriesFilterInputs(ValidationEntriesFilter validationEntriesFilter){
    	if(nonNull(validationEntriesFilter.getStatuses()) && nonNull(validationEntriesFilter.getExcludeStatuses())){
    		if(validationEntriesFilter.getStatuses().iterator().hasNext()
    				&& validationEntriesFilter.getExcludeStatuses().iterator().hasNext()){
    			throw new IllegalArgumentException("One cannot have both statuses and exclude statuses when making request");
    		}
    	}
    	return true;
    }

    private String convertValidationEntryDataStatusEnumIteratorToString(Iterable<ValidationEntryStatus> statusIterable, String separator){
    	if(nonNull(statusIterable)){
    		return StringUtils.join(statusIterable, separator);
    	}
    	return StringUtils.EMPTY;
    }

    /**
     * Returns an object representing the various email validations jobs initiated.
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public List<ValidationOverview> listJobs() throws IOException {
    	ValidationJobsFilter validationJobsFilter = null;
    	return listJobs(validationJobsFilter);
    }

    /**
     * Returns an object representing the various email validations jobs initiated for the input date.
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param filterCreatedOn Local date for which usage job details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public List<ValidationOverview> listJobs(LocalDate filterCreatedOn) throws IOException {
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	return listJobs(validationJobsFilter);
    }

    /**
     * Returns an object representing the various email validations jobs initiated for the input date and with given statuses.
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param filterCreatedOn Local date for which usage job details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @param statuses  A collection of statuses for which the jobs needs to be retrieved.
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public List<ValidationOverview> listJobs(LocalDate filterCreatedOn, ValidationStatus[] statuses) throws IOException {
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	validationJobsFilter.setStatuses(Arrays.asList(statuses));
    	return listJobs(validationJobsFilter);
    }

    /**
     * Returns an object representing the various email validations jobs initiated for the input date and with given sort direction
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param filterCreatedOn Local date for which usage job details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @param sort String based on which sort needs to be applied when fetching results.
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public List<ValidationOverview> listJobs(LocalDate filterCreatedOn, ValidationJobsSort sort) throws IOException {
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	validationJobsFilter.setSort(sort);
    	return listJobs(validationJobsFilter);
    }

    /**
     * Returns an object representing the various email validations jobs initiated for the input date, given statuses and with given sort direction
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param filterCreatedOn Date in format YYYY-MM-DD for which usage job details needs to be fetched. If null or blank value is passed, it will not consider the param when making request.
     * @param statuses  A collection of statuses for which the jobs needs to be retrieved.
     * @param sort String based on which sort needs to be applied when fetching results.
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public List<ValidationOverview> listJobs(LocalDate filterCreatedOn, ValidationStatus[] statuses, ValidationJobsSort sort)
    		throws IOException {
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	validationJobsFilter.setStatuses(Arrays.asList(statuses));
    	validationJobsFilter.setSort(sort);
    	return listJobs(validationJobsFilter);
    }

    /**
     * Returns an object representing the various email validations jobs initiated based on filter and sort options passed.
     * Makes a GET request to the <b>"/email-validations"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param validationJobFilter Object with options for filters and sort options supported.
     * @return List<ValidationOverview> An object list representing the information related to each validation job.
     */
    public List<ValidationOverview> listJobs(ValidationJobsFilter validationJobFilter) throws IOException {
    	// Assign with default values to handle pagination
    	String cursor = StringUtils.EMPTY;
    	Boolean isTruncated = Boolean.TRUE;
    	List<ValidationOverview> validationJobsData = new ArrayList<ValidationOverview>();

    	// Run through responses to handle pagination
    	while(nonNull(isTruncated) && isTruncated){
	    	// Build query string param map
	    	Map<String, String> paramMap = getListJobsParamMap(validationJobFilter, cursor);

	    	// Build request URI with the param map
	    	URI requestUri = Utils.getHttpUri(null, null, null, paramMap);

	    	// Build query entries resource string
	    	StringBuilder listJobsResource = new StringBuilder(Constants.EMAIL_VALIDATIONS_RESOURCE);
	    	if(nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())){
	    		listJobsResource.append(requestUri.toString());
	    	}
	    	System.out.println("List jobs URI: " + listJobsResource.toString());

	    	// Make request object for the rest call
	    	RestRequest request = new RestRequest(HttpRequestMethod.GET, listJobsResource.toString());

	        // Sends the request to the Verifalia servers
	        RestResponse response = restClient.execute(request, ValidationJobs.class);

	        // Check for response. If not appropriate, throw error
	        if(response.getStatusCode() != HttpStatusCode.OK)
	        	throw new VerifaliaException(response);

	        // Handle pagination with meta details
	        ValidationJobs validationJobs = ((ValidationJobs)response.getData());
	        validationJobsData.addAll(validationJobs.getData());
	        ResponseMeta meta = validationJobs.getMeta();
	        isTruncated = meta.getIsTruncated();
	        cursor = meta.getCursor();
    	}
    	return validationJobsData;
    }

    private Map<String, String> getListJobsParamMap(ValidationJobsFilter validationJobFilter, String cursor){
    	Map<String, String> paramMap = new HashMap<String, String>();

    	// Add cursor as param for handling pagination. If cursor is passed, no need to pass other params as per the documentation.
    	if(!StringUtils.isBlank(cursor)){
			paramMap.put(Constants.API_PARAM_CURSOR, cursor);
		} else {
	    	if(nonNull(validationJobFilter)){
	    		if(validateJobsFilterInputs(validationJobFilter)){ // If data is valid
			    	// Created on filter
			    	if(nonNull(validationJobFilter.getCreatedOn())){
			    		paramMap.put("createdOn", Utils.convertLocalDateToString(validationJobFilter.getCreatedOn(),
			    				Constants.DATE_FORMAT));
			    	}
			    	// Created on since filter
			    	if(nonNull(validationJobFilter.getCreatedOnSince())){
			    		paramMap.put("createdOn:since", Utils.convertLocalDateToString(validationJobFilter.getCreatedOnSince(),
			    				Constants.DATE_FORMAT));
			    	}
			    	// Created on until filter
			    	if(nonNull(validationJobFilter.getCreatedOnUntil())){
			    		paramMap.put("createdOn:until", Utils.convertLocalDateToString(validationJobFilter.getCreatedOnUntil(),
			    				Constants.DATE_FORMAT));
			    	}
			    	// Status filter
			    	String statusesStr = convertValidationStatusEnumIteratorToString(validationJobFilter.getStatuses(),
			    			Constants.STRING_SEPERATOR_COMMA);
			    	if(!StringUtils.isBlank(statusesStr)){
			    		paramMap.put("status", statusesStr);
			    	}
			    	// Exclude status filter
			    	String excludeStatusesStr = convertValidationStatusEnumIteratorToString(validationJobFilter.getExcludeStatuses(),
			    			Constants.STRING_SEPERATOR_COMMA);
			    	if(!StringUtils.isBlank(excludeStatusesStr)){
			    		paramMap.put("status:exclude", excludeStatusesStr);
			    	}
			    	// Owner filter
			    	if(!StringUtils.isBlank(validationJobFilter.getOwner())){
			    		paramMap.put("owner", validationJobFilter.getOwner());
			    	}
			    	// Sort
			    	if(nonNull(validationJobFilter.getSort())){
			    		paramMap.put("sort", validationJobFilter.getSort().getValidationJobsSort());
			    	}
	    		}
	    	}
		}
    	return paramMap;
    }

    private boolean validateJobsFilterInputs(ValidationJobsFilter validationJobFilter){
    	// Validation related to dates
    	if(nonNull(validationJobFilter.getCreatedOn())
    			&& (nonNull(validationJobFilter.getCreatedOnSince()) || nonNull(validationJobFilter.getCreatedOnUntil()))){
    		throw new IllegalArgumentException("One cannot have both created on date and created on since or created on until or both when making request");
    	}
    	// Validation related to dates
    	if(nonNull(validationJobFilter.getCreatedOnSince())
    			&& nonNull(validationJobFilter.getCreatedOnUntil())){
    		if(validationJobFilter.getCreatedOnUntil().isBefore(validationJobFilter.getCreatedOnSince())){
    			throw new IllegalArgumentException("One cannot have created until date before created on date");
    		}
    	}
    	// Validation related to statuses and exclude statuses
    	if(nonNull(validationJobFilter.getStatuses()) && nonNull(validationJobFilter.getExcludeStatuses())){
    		if(validationJobFilter.getStatuses().iterator().hasNext()
    				&& validationJobFilter.getExcludeStatuses().iterator().hasNext()){
    			throw new IllegalArgumentException("One cannot have both statuses and exclude statuses when making request");
    		}
    	}
    	return true;
    }

    private String convertValidationStatusEnumIteratorToString(Iterable<ValidationStatus> statusIterable, String separator){
    	if(nonNull(statusIterable)){
    		return StringUtils.join(statusIterable, separator);
    	}
    	return StringUtils.EMPTY;
    }

    /**
     * Deletes an email validation batch, identified by the specified unique identifier.
     * Makes a DELETE request to the <b>"/email-validations/{id}"</b> resource.
     * @param id The identifier for an email validation batch to be deleted.
     * @throws IOException
     */
    public void delete(String id) throws IOException {
    	if(!StringUtils.isBlank(id)){
	    	RestRequest request = new RestRequest(HttpRequestMethod.DELETE, Constants.EMAIL_VALIDATIONS_RESOURCE + "/" + id);

	        // Sends the request to the Verifalia servers
	        RestResponse response = restClient.execute(request, Void.class);

	        if(response.getStatusCode() == HttpStatusCode.OK
	        		|| response.getStatusCode() == HttpStatusCode.GONE){
	        	return;
	        } else {
	        	throw new VerifaliaException(response);
	        }
    	} else {
    		throw new IllegalArgumentException("Job ID cannot be blank");
    	}
    }
}
