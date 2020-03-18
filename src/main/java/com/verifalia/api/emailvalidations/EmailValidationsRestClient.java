package com.verifalia.api.emailvalidations;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.Constants;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.common.ServerPollingLoopEventListener.ServerPollingLoopEvent;
import com.verifalia.api.common.Utils;
import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.emailvalidations.models.ValidationEntries;
import com.verifalia.api.emailvalidations.models.ValidationEntriesFilter;
import com.verifalia.api.emailvalidations.models.ValidationJobs;
import com.verifalia.api.emailvalidations.models.ValidationJobsFilter;
import com.verifalia.api.emailvalidations.models.ValidationOverview;
import com.verifalia.api.emailvalidations.models.ValidationStatus;
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
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#Pending Pending} status.
     * Validations are completed only when their {@link Validation#status} property
     * is {@link ValidationStatus#Completed Completed}. Use the {@link EmailValidationsRestClient#submit(String[], WaitForCompletionOptions)}
     * to wait for the completion of the batch without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String) query}
     * along with the batch's {@link Validation#uniqueID}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @return An object representing the email validation batch
     * @throws VerifaliaException
     * @throws IOException
     */
    public Validation submit(String[] emailAddresses) throws IOException, VerifaliaException {
        return submit(emailAddresses, WaitForCompletionOptions.DontWait);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#Pending Pending} status.
     * Validations are completed only when their {@link Validation#status} property
     * is {@link ValidationStatus#Completed Completed}. Use the {@link EmailValidationsRestClient#submit(java.lang.Iterable, WaitForCompletionOptions)}
     * to wait for the completion of the batch without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String) query}
     * along with the batch's {@link Validation#uniqueID}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @return An object representing the email validation batch
     * @throws VerifaliaException
     * @throws IOException
     */
    public Validation submit(Iterable<String> emailAddresses) throws IOException, VerifaliaException {
        return submit(emailAddresses, WaitForCompletionOptions.DontWait);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#Pending Pending} status.
     * Validations are completed only when their {@link Validation#status} property.
     * is {@link ValidationStatus#Completed Completed}; the <b>waitForCompletionOptions</b> parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link Validation#uniqueID}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(String[] emailAddresses, WaitForCompletionOptions waitForCompletionOptions) throws IOException, VerifaliaException {
    	return submit(Arrays.asList(emailAddresses), waitForCompletionOptions);
    }

    /**
     * Initiates a new email validation batch. Makes a POST request to the "/email-validations" resource.
     * <p>Upon initialization, batches usually are in the {@link ValidationStatus#Pending Pending} status.
     * Validations are completed only when their {@link Validation#status} property.
     * is {@link ValidationStatus#Completed Completed}; the {@code waitForCompletionOptions} parameter
     * allows to wait for the completion of the batch, without having to manually poll the API.
     * In order to retrieve the most up-to-date snapshot of a validation batch, call the {@link EmailValidationsRestClient#query(String)}
     * along with the batch's {@link Validation#uniqueID}.
     *
     * @param emailAddresses A collection of email addresses to validate
     * @param waitForCompletionOptions The options about waiting for the validation completion
     * @return An object representing the email validation batch.
     * @throws IOException
     * @throws VerifaliaException
     */
    public Validation submit(Iterable<String> emailAddresses, WaitForCompletionOptions waitForCompletionOptions) throws IOException, VerifaliaException {
        if (emailAddresses == null)
            throw new IllegalArgumentException("emailAddresses");

        if (waitForCompletionOptions == null)
            throw new IllegalArgumentException("waitForCompletionOptions");

        if (!emailAddresses.iterator().hasNext())
            throw new IllegalArgumentException("Can't validate an empty batch (emailAddresses)");

        // Build the REST request

        RestRequest request = new RestRequest(HttpRequestMethod.POST, Constants.EMAIL_VALIDATIONS_RESOURCE);
        request.addEntries(emailAddresses);

        // Send the request to the Verifalia servers
        RestResponse response = restClient.execute(request, Validation.class);
        Validation data = (Validation)response.getData();

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

    /**
     * Returns an object representing an email validation batch, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @return An object representing the current status of the requested email validation batch.
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
     * @return An object representing the current status of the requested email validation batch.
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
    Validation queryOnce(String id) throws IOException {
        RestRequest request = new RestRequest(HttpRequestMethod.GET, Constants.EMAIL_VALIDATIONS_RESOURCE + "/" + id);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, Validation.class);
        Validation data = (Validation) response.getData();

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
    }

    /**
     * Returns an object representing an email validation batch overview, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}/overview"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @return An object representing the overview of the requested email validation batch.
     */
    public ValidationOverview queryOverview(String id) throws IOException {
        RestRequest request = new RestRequest(HttpRequestMethod.GET, Constants.EMAIL_VALIDATIONS_RESOURCE + "/" + id
        		+ "/" + Constants.EMAIL_VALIDATIONS_OVERVIEW_RESOURCE);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, ValidationOverview.class);
        ValidationOverview data = (ValidationOverview) response.getData();

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
    }

    /**
     * Returns an object representing an email validation batch entries, identified by the specified unique identifier.
     * Makes a GET request to the <b>"/email-validations/{id}/entries"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param id The identifier for an email validation batch to be retrieved.
     * @return An object representing the entries of the requested email validation batch.
     */
    public ValidationEntries queryEntries(String id) throws IOException {
    	ValidationEntriesFilter validationEntriesFilter = null;
    	return queryEntries(id, validationEntriesFilter);
    }

    public ValidationEntries queryEntries(String id, String[] statuses) throws IOException {
    	ValidationEntriesFilter validationEntriesFilter = new ValidationEntriesFilter();
    	validationEntriesFilter.setStatuses(Arrays.asList(statuses));
    	return queryEntries(id, validationEntriesFilter);
    }

    public ValidationEntries queryEntries(String id, Iterable<String> statuses) throws IOException {
    	ValidationEntriesFilter validationEntriesFilter = new ValidationEntriesFilter();
    	validationEntriesFilter.setStatuses(statuses);
    	return queryEntries(id, validationEntriesFilter);
    }

    public ValidationEntries queryEntries(String id, ValidationEntriesFilter validationEntriesFilter)
    		throws IOException{
    	// Build query string parameters map
    	Map<String, String> paramMap = getValidationEntriesParamMap(validationEntriesFilter);

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
    	System.out.println("URI: " + queryEntriesResource.toString());

    	// Make request object for the rest call
    	RestRequest request = new RestRequest(HttpRequestMethod.GET, queryEntriesResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, ValidationEntries.class);

        if(response.getStatusCode() != HttpStatusCode.OK && response.getStatusCode() != HttpStatusCode.ACCEPTED)
        	throw new VerifaliaException(response);
        return (ValidationEntries) response.getData();
    }

    private Map<String, String> getValidationEntriesParamMap(ValidationEntriesFilter validationEntriesFilter){
    	Map<String, String> paramMap = new HashMap<String, String>();
    	if(nonNull(validationEntriesFilter)){
    		// TODO - Validate list job filter
	    	// Status filter
	    	String statusesStr = Utils.convertStringIteratorToString(validationEntriesFilter.getStatuses(), ",");
	    	if(!StringUtils.isBlank(statusesStr)){
	    		paramMap.put("status", statusesStr);
	    	}
	    	// Exclude status filter
	    	String excludeStatusesStr = Utils.convertStringIteratorToString(validationEntriesFilter.getExcludeStatuses(), ",");
	    	if(!StringUtils.isBlank(excludeStatusesStr)){
	    		paramMap.put("status:exclude", excludeStatusesStr);
	    	}
    	}
    	return paramMap;
    }


    public ValidationJobs listJobs() throws IOException{
    	ValidationJobsFilter validationJobsFilter = null;
    	return listJobs(validationJobsFilter);
    }

    public ValidationJobs listJobs(String filterCreatedOn) throws IOException{
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	return listJobs(validationJobsFilter);
    }

    public ValidationJobs listJobs(String filterCreatedOn, String[] statuses) throws IOException{
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	validationJobsFilter.setStatuses(Arrays.asList(statuses));
    	return listJobs(validationJobsFilter);
    }

    public ValidationJobs listJobs(String filterCreatedOn, String sort) throws IOException{
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	validationJobsFilter.setSort(sort);
    	return listJobs(validationJobsFilter);
    }

    public ValidationJobs listJobs(String filterCreatedOn, String[] statuses, String sort) throws IOException{
    	ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
    	validationJobsFilter.setCreatedOn(filterCreatedOn);
    	validationJobsFilter.setStatuses(Arrays.asList(statuses));
    	validationJobsFilter.setSort(sort);
    	return listJobs(validationJobsFilter);
    }

    public ValidationJobs listJobs(ValidationJobsFilter validationJobFilter) throws IOException{
    	// Build query string param map
    	Map<String, String> paramMap = getListJobsParamMap(validationJobFilter);

    	// Build request URI with the param map
    	URI requestUri = Utils.getHttpUri(null, null, null, paramMap);

    	// Build query entries resource string
    	StringBuilder listJobsResource = new StringBuilder(Constants.EMAIL_VALIDATIONS_RESOURCE);
    	if(nonNull(requestUri) && !StringUtils.isBlank(requestUri.toString())){
    		listJobsResource.append(requestUri.toString());
    	}
    	System.out.println("URI: " + listJobsResource.toString());

    	// Make request object for the rest call
    	RestRequest request = new RestRequest(HttpRequestMethod.GET, listJobsResource.toString());

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, ValidationJobs.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);
        return (ValidationJobs) response.getData();
    }

    private Map<String, String> getListJobsParamMap(ValidationJobsFilter validationJobFilter){
    	Map<String, String> paramMap = new HashMap<String, String>();
    	if(nonNull(validationJobFilter)){
    		// TODO - Validate list job filter
	    	// Created on filter
	    	if(!StringUtils.isBlank(validationJobFilter.getCreatedOn())){
	    		paramMap.put("createdOn", validationJobFilter.getCreatedOn());
	    	}
	    	// Created on since filter
	    	if(!StringUtils.isBlank(validationJobFilter.getCreatedOnSince())){
	    		paramMap.put("createdOn:since", validationJobFilter.getCreatedOnSince());
	    	}
	    	// Created on until filter
	    	if(!StringUtils.isBlank(validationJobFilter.getCreatedOnUntil())){
	    		paramMap.put("createdOn:until", validationJobFilter.getCreatedOnUntil());
	    	}
	    	// Status filter
	    	String statusesStr = Utils.convertStringIteratorToString(validationJobFilter.getStatuses(), ",");
	    	if(!StringUtils.isBlank(statusesStr)){
	    		paramMap.put("status", statusesStr);
	    	}
	    	// Exclude status filter
	    	String excludeStatusesStr = Utils.convertStringIteratorToString(validationJobFilter.getExcludeStatuses(), ",");
	    	if(!StringUtils.isBlank(excludeStatusesStr)){
	    		paramMap.put("status:exclude", excludeStatusesStr);
	    	}
	    	// Owner filter
	    	if(!StringUtils.isBlank(validationJobFilter.getOwner())){
	    		paramMap.put("owner", validationJobFilter.getOwner());
	    	}
	    	// Sort
	    	if(!StringUtils.isBlank(validationJobFilter.getSort())){
	    		paramMap.put("sort", validationJobFilter.getSort());
	    	}
    	}
    	return paramMap;
    }

    /**
     * Deletes an email validation batch, identified by the specified unique identifier.
     * Makes a DELETE request to the <b>"/email-validations/{id}"</b> resource.
     * @param uniqueId The unique identifier for an email validation batch to be deleted
     * @throws IOException
     */
    public void delete(String id) throws IOException {
    	RestRequest request = new RestRequest(HttpRequestMethod.DELETE, Constants.EMAIL_VALIDATIONS_RESOURCE + "/" + id);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, Validation.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);
    }
}
