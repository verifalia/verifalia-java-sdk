package com.verifalia.api.samples;

import java.io.IOException;
import java.net.URISyntaxException;

import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.emailvalidations.models.ValidationEntryData;
import com.verifalia.api.emailvalidations.models.ValidationStatus;
import com.verifalia.api.exceptions.VerifaliaException;

public class Samples {

	public static void main(String[] args) {
		if(args.length >= 2) {
			try {
				Samples sample = new Samples();
//				sample.queryVerifaliaServiceSample1(args[0], args[1]);
//				sample.queryVerifaliaServiceSample2(args[0], args[1]);
				sample.queryVerifaliaServiceSample3(args[0], args[1]);
			} catch(Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
			System.exit(0);
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API with request timeout.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaServiceSample1(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with waiting parameters
		Validation result = restClient.getEmailValidations().submit(new String[] {
				"alice@example.com",
				"bob@example.net",
				"carol@example.org"
			},
			new WaitForCompletionOptions(10*60) // in seconds
		);

		if (result == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display results
			for (ValidationEntryData entryData: result.getEntries().getData())
			{
				System.out.printf("Address: %s => Result: %s\n",
						entryData.getInputData(),
						entryData.getStatus()
				);
			}
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API with preconfigured request timeout
	 * and list for the status polling cycle events.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaServiceSample2(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		/**
		 * This class listens for events of the internal polling loop.
		 */
		class ServerPollingLoopEventListenerImpl implements ServerPollingLoopEventListener {
			@Override
			public void onPollingLoopEvent(ServerPollingLoopEvent event, Validation currentResult) {
				switch(event)
				{
					case ServerPollingLoopStarted: {
						System.out.println("Info: Polling loop started.");
						break;
					}

					case ServerPollingLoopFinished: {
						System.out.println("Info: Polling loop finished.");
						break;
					}

					case BeforePollServer: {
						System.out.println("Info: Going to poll the server right now...");
						break;
					}

					case AfterPollServer: {
						System.out.println("Info: Request to server has been completed.");
						showCurrentResult(currentResult);
						break;
					}
				}
			}

			private void showCurrentResult(Validation currentResult) {
				System.out.print("Info: Current status:");
				if(currentResult == null)
					System.out.println(" Unknown");
				else {
					System.out.println();
					System.out.println(currentResult.toString());
				}
			}
		};

		// Submit email verification request with method that returns immediately
		Validation result = restClient.getEmailValidations().submit(new String[] {
				"alice@example.com",
				"bob@example.net",
				"carol@example.org"
			}
		);

		// If request not completed, wait and display progress.
		// when there are polling events happening
		if(result.getOverview().getStatus() != ValidationStatus.Completed)
			result = restClient.getEmailValidations().query(
				result.getOverview().getId(),
				new WaitForCompletionOptions(10*60), // in seconds
				new ServerPollingLoopEventListenerImpl()
			);

		if (result == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display results
			for (ValidationEntryData entryData: result.getEntries().getData()){
				System.out.printf("Address: %s => Result: %s\n",
						entryData.getInputData(),
						entryData.getStatus()
				);
			}
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API for the manual status polling.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaServiceSample3(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with method that return status immediately
		Validation result = restClient.getEmailValidations().submit(new String[] {
				"alice@example.com",
				"bob@example.net",
				"carol@example.org"
			}
		);

		// Loop until request processing is completed or execution thread is interrupted
		while (result.getOverview().getStatus() != ValidationStatus.Completed)
		{
			result = restClient.getEmailValidations().query(result.getOverview().getId(), WaitForCompletionOptions.DontWait);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				if(Thread.currentThread().isInterrupted())
					break;
			}
		}

		// If request completed, display result
		if (result.getOverview().getStatus() != ValidationStatus.Completed)
			System.err.println("Request still pending.");
		else {
			// Display results
			for (ValidationEntryData entryData: result.getEntries().getData()){
				System.out.printf("Address: %s => Result: %s\n",
						entryData.getInputData(),
						entryData.getStatus()
				);
			}
		}
	}

}
