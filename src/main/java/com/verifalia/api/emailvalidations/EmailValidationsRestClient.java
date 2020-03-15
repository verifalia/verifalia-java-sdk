package com.verifalia.api.emailvalidations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.Constants;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.common.ServerPollingLoopEventListener.ServerPollingLoopEvent;
import com.verifalia.api.emailvalidations.models.Validation;
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
                	System.out.println("Status code accepted response: " + data.toString());
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
     * @param uniqueId The unique identifier for an email validation batch to be retrieved.
     * @return An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation query(String uniqueId) throws IOException {
        return query(uniqueId, WaitForCompletionOptions.DontWait);
    }

    /**
     * Returns an object representing an email validation batch, waiting for its completion and issuing multiple retries if needed.
     * Makes a GET request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param uniqueId The unique identifier for an email validation batch to be retrieved.
     * @param waitOptions The options about waiting for the validation completion.
     * @return An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation query(final String uniqueId, final WaitForCompletionOptions waitOptions) throws IOException {
    	return query(uniqueId, waitOptions, null);
    }

    /**
     * Returns an object representing an email validation batch, waiting for its completion and issuing multiple retries if needed.
     * Makes a GET request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param uniqueId The unique identifier for an email validation batch to be retrieved.
     * @param waitOptions The options about waiting for the validation completion.
     * @param pollingLoopEventListener Polling loop event listener, may be <b>null</b>.
     * @return An object representing the current status of the requested email validation batch.
     * @throws IOException
     */
    public Validation query(final String uniqueId, final WaitForCompletionOptions waitOptions,
    		final ServerPollingLoopEventListener pollingLoopEventListener) throws IOException {
        // Handle the case when the client wishes to avoid waiting for completion

        if (waitOptions == WaitForCompletionOptions.DontWait)
            return queryOnce(uniqueId);

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

		                result = queryOnce(uniqueId);

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
     * Makes a GET request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * <p>To initiate a new email validation batch, please use {@link EmailValidationsRestClient#submit(java.lang.Iterable)}
     * @param uniqueId The unique identifier for an email validation batch to be retrieved.
     * @return An object representing the current status of the requested email validation batch.
     */
    Validation queryOnce(String uniqueId) throws IOException {
        RestRequest request = new RestRequest(HttpRequestMethod.GET, Constants.EMAIL_VALIDATIONS_RESOURCE + "/" + uniqueId);

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
     * Deletes an email validation batch, identified by the specified unique identifier.
     * Makes a DELETE request to the <b>"/email-validations/{uniqueId}"</b> resource.
     * @param uniqueId The unique identifier for an email validation batch to be deleted
     * @throws IOException
     */
    public void delete(String uniqueId) throws IOException {
    	RestRequest request = new RestRequest(HttpRequestMethod.DELETE, Constants.EMAIL_VALIDATIONS_RESOURCE + "/" + uniqueId);

        // Sends the request to the Verifalia servers
        RestResponse response = restClient.execute(request, Validation.class);

        if(response.getStatusCode() != HttpStatusCode.OK)
        	throw new VerifaliaException(response);
    }
}
