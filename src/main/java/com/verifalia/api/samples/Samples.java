package com.verifalia.api.samples;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;

import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.credits.models.CreditBalanceData;
import com.verifalia.api.credits.models.CreditDailyUsage;
import com.verifalia.api.credits.models.CreditDailyUsageData;
import com.verifalia.api.credits.models.CreditDailyUsageFilter;
import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.emailvalidations.models.ValidationEntries;
import com.verifalia.api.emailvalidations.models.ValidationEntriesFilter;
import com.verifalia.api.emailvalidations.models.ValidationEntry;
import com.verifalia.api.emailvalidations.models.ValidationEntryStatus;
import com.verifalia.api.emailvalidations.models.ValidationJobs;
import com.verifalia.api.emailvalidations.models.ValidationJobsFilter;
import com.verifalia.api.emailvalidations.models.ValidationJobsSort;
import com.verifalia.api.emailvalidations.models.ValidationOverview;
import com.verifalia.api.emailvalidations.models.ValidationStatus;
import com.verifalia.api.exceptions.VerifaliaException;

public class Samples {

	public static void main(String[] args) {
		if(args.length >= 2) {
			try {
				Samples sample = new Samples();
				// Email validations
				sample.queryVerifaliaEmailValidationsServiceSample1(args[0], args[1]);
				sample.queryVerifaliaEmailValidationsServiceSample2(args[0], args[1]);
				sample.queryVerifaliaEmailValidationsServiceSample3(args[0], args[1]);
				sample.queryVerifaliaEmailValidationsOverviewSample(args[0], args[1]);
				sample.queryVerifaliaEmailValidationsEntriesSample(args[0], args[1]);
				sample.queryVerifaliaEmailValidationsEntriesWithFiltersSample(args[0], args[1]);
				sample.queryVerifaliaEmailValidationsJobsSample(args[0], args[1]);
				sample.queryVerifaliaEmailValidationsJobsWithFiltersSample(args[0], args[1]);
				// Credits
				sample.getVerifaliaCreditsBalanceSample(args[0], args[1]);
				sample.getVerifaliaCreditsDailyUsageSample(args[0], args[1]);
				sample.getVerifaliaCreditsDailyUsageWithFiltersSample(args[0], args[1]);
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
	void queryVerifaliaEmailValidationsServiceSample1(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsServiceSample1 ------------------------");

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
			for (ValidationEntry entryData: result.getEntries().getData()) {
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
	void queryVerifaliaEmailValidationsServiceSample2(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsServiceSample2 ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		/**
		 * This class listens for events of the internal polling loop.
		 */
		class ServerPollingLoopEventListenerImpl implements ServerPollingLoopEventListener {
			@Override
			public void onPollingLoopEvent(ServerPollingLoopEvent event, Validation currentResult) {
				switch(event) {
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
			for (ValidationEntry entryData: result.getEntries().getData()){
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
	void queryVerifaliaEmailValidationsServiceSample3(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsServiceSample3 ------------------------");

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
		while (result.getOverview().getStatus() != ValidationStatus.Completed) {
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
			for (ValidationEntry entryData: result.getEntries().getData()){
				System.out.printf("Address: %s => Result: %s\n",
						entryData.getInputData(),
						entryData.getStatus()
				);
			}
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API to get overview information related to email validation job.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaEmailValidationsOverviewSample(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsOverviewSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with method that return status immediately
		ValidationOverview result = restClient.getEmailValidations().submit(new String[] {
				"alice@example.com",
				"bob@example.net",
				"carol@example.org"
			}
		).getOverview();

		// Loop until request processing is completed or execution thread is interrupted
		while (result.getStatus() != ValidationStatus.Completed) {
			result = restClient.getEmailValidations().queryOverview(result.getId());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				if(Thread.currentThread().isInterrupted())
					break;
			}
		}

		// If request completed, display result
		if (result.getStatus() != ValidationStatus.Completed)
			System.err.println("Request still pending.");
		else {
			// Display results
			System.out.printf("No of Entries: %s => Status: %s => Completed on: %s\n",
					result.getNoOfEntries(),
					result.getStatus(),
					result.getCompletedOn()
			);
		}
	}


	/**
	 * This sample method demonstrates, how to use Verifalia API to get email validation entries information related to email validation job.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaEmailValidationsEntriesSample(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsEntriesSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with method that return status immediately
		ValidationOverview result = restClient.getEmailValidations().submit(new String[] {
				"alice@example.com",
				"bob@example.net",
				"carol@example.org"
			}
		).getOverview();

		// Loop until request processing is completed or execution thread is interrupted
		while (result.getStatus() != ValidationStatus.Completed) {
			result = restClient.getEmailValidations().queryOverview(result.getId());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				if(Thread.currentThread().isInterrupted())
					break;
			}
		}

		// If request completed, display result
		if (result.getStatus() != ValidationStatus.Completed)
			System.err.println("Request still pending.");
		else {
			// Display results
			ValidationEntries entries = restClient.getEmailValidations().queryEntries(result.getId());
			for (ValidationEntry entryData: entries.getData()){
				System.out.printf("Address: %s => Result: %s\n",
						entryData.getInputData(),
						entryData.getStatus()
				);
			}
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API to get email validation entries information related to email validation with options for filters available.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaEmailValidationsEntriesWithFiltersSample(String accountSid, String authToken) throws VerifaliaException,
		IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsEntriesWithFiltersSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with method that return status immediately
		ValidationOverview result = restClient.getEmailValidations().submit(new String[] {
				"alice@example.com",
				"bob@example.net",
				"carol@example.org"
			}
		).getOverview();

		// Submit request for getting entries with filter status
		System.out.println("------------------------------------------------------");
		ValidationEntries entries1 = restClient.getEmailValidations().queryEntries(result.getId(), new ValidationEntryStatus[] {
			ValidationEntryStatus.Success,
			ValidationEntryStatus.DomainHasNullMx
		});
		System.out.println("Entries results with filter status: " + entries1.getData().size());
		for (ValidationEntry entryData: entries1.getData()){
			System.out.printf("Address: %s => Result: %s\n",
					entryData.getInputData(),
					entryData.getStatus()
			);
		}

		// Submit request for getting entries with filter object
		System.out.println("------------------------------------------------------");
		ValidationEntriesFilter validationEntriesFilter = new ValidationEntriesFilter();
		validationEntriesFilter.setExcludeStatuses(Arrays.asList(new ValidationEntryStatus[] {
			ValidationEntryStatus.Success,
			ValidationEntryStatus.DomainHasNullMx
		}));
		ValidationEntries entries2 = restClient.getEmailValidations().queryEntries(result.getId(), validationEntriesFilter);
		System.out.println("Entries results with filter object: " + entries2.getData().size());
		for (ValidationEntry entryData: entries2.getData()){
			System.out.printf("Address: %s => Result: %s\n",
					entryData.getInputData(),
					entryData.getStatus()
			);
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API to get email validation jobs information for all submitted email validation batches.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaEmailValidationsJobsSample(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsJobsSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with method that return status immediately
		ValidationJobs jobs = restClient.getEmailValidations().listJobs();

		for (ValidationOverview validationOverview: jobs.getData()){
			System.out.printf("ID: %s => Status: %s => No of Entries: %s => Completed On: %s\n",
					validationOverview.getId(),
					validationOverview.getStatus(),
					validationOverview.getNoOfEntries(),
					validationOverview.getCompletedOn()
			);
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API to get email validation jobs information for all submitted email validation batches with options for filters available.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void queryVerifaliaEmailValidationsJobsWithFiltersSample(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ queryVerifaliaEmailValidationsJobsWithFiltersSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit request for listing job with filter createdOn
		System.out.println("------------------------------------------------------");
		ValidationJobs jobsResult1 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-11"));
		System.out.println("Jobs results with filter createdOn: " + jobsResult1.getData().size());
		for (ValidationOverview validationOverview: jobsResult1.getData()){
			System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
					validationOverview.getId(),
					validationOverview.getStatus(),
					validationOverview.getNoOfEntries()
			);
		}

		// Submit request for listing job with filter createdOn, status
		System.out.println("------------------------------------------------------");
		ValidationJobs jobsResult2 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-12"), new ValidationStatus[] {
			ValidationStatus.Completed,
			ValidationStatus.InProgress,
		});
		System.out.println("Jobs results with filter createdOn and status: " + jobsResult2.getData().size());
		for (ValidationOverview validationOverview: jobsResult2.getData()){
			System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
					validationOverview.getId(),
					validationOverview.getStatus(),
					validationOverview.getNoOfEntries()
			);
		}

		// Submit request for listing job with filter filter createdOn and sort -createdOn
		System.out.println("------------------------------------------------------");
		ValidationJobs jobsResult3 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-17"),
				ValidationJobsSort.CreatedOnDesc);
		System.out.println("Jobs results with filter createdOn and sort -createdOn: " + jobsResult3.getData().size());
		for (ValidationOverview validationOverview: jobsResult3.getData()){
			System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
					validationOverview.getId(),
					validationOverview.getStatus(),
					validationOverview.getNoOfEntries()
			);
		}

		// Submit request for listing job with filter createdOn, status and sort createdOn
		System.out.println("------------------------------------------------------");
		ValidationJobs jobsResult4 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-11"), new ValidationStatus[] {
				ValidationStatus.Completed,
				ValidationStatus.InProgress
		}, ValidationJobsSort.CreatedOnAsc);
		System.out.println("Jobs results with filter createdOn, status and sort createdOn: " + jobsResult4.getData().size());
		for (ValidationOverview validationOverview: jobsResult4.getData()){
			System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
					validationOverview.getId(),
					validationOverview.getStatus(),
					validationOverview.getNoOfEntries()
			);
		}

		// Submit request for listing job with object
		System.out.println("------------------------------------------------------");
		ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
		validationJobsFilter.setCreatedOnSince(LocalDate.parse("2020-03-11"));
		validationJobsFilter.setCreatedOnUntil(LocalDate.parse("2020-03-12"));
		validationJobsFilter.setExcludeStatuses(Arrays.asList(new ValidationStatus[] {
				ValidationStatus.Expired
		}));
		validationJobsFilter.setSort(ValidationJobsSort.CreatedOnDesc);
		ValidationJobs jobsResult5 = restClient.getEmailValidations().listJobs(validationJobsFilter);
		System.out.println("Jobs results with object: " + jobsResult5.getData().size());
		for (ValidationOverview validationOverview: jobsResult5.getData()){
			System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
					validationOverview.getId(),
					validationOverview.getStatus(),
					validationOverview.getNoOfEntries()
			);
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API to get account credit balance information.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void getVerifaliaCreditsBalanceSample(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ getVerifaliaCreditsBalanceSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with waiting parameters
		CreditBalanceData result = restClient.getCredits().getBalance();

		if (result == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display result
			System.out.printf("Credit Packs: %s => Free Credits: %s => Free Credits Reset In: %s\n",
					result.getCreditPacks(),
					result.getFreeCredits(),
					result.getFreeCreditsResetIn());
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API to get account balance daily usage details.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void getVerifaliaCreditsDailyUsageSample(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ getVerifaliaCreditsDailyUsageSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit email verification request with waiting parameters
		CreditDailyUsage result = restClient.getCredits().getDailyUsage();

		if (result == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display results
			for (CreditDailyUsageData creditDailyUsageData: result.getData()){
				System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
						creditDailyUsageData.getDate(),
						creditDailyUsageData.getCreditPacks(),
						creditDailyUsageData.getFreeCredits());
			}
		}
	}

	/**
	 * This sample method demonstrates, how to use Verifalia API to get account balance daily usage details with details of filters available.
	 * @param accountSid Your Verifalia sub-account SID
	 * @param authToken Your Verifalia sub-account authentication token
	 * @throws VerifaliaException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	void getVerifaliaCreditsDailyUsageWithFiltersSample(String accountSid, String authToken) throws VerifaliaException, IOException, URISyntaxException {

		System.out.println("------------------------ getVerifaliaCreditsDailyUsageWithFiltersSample ------------------------");

		// Create REST client object with your credentials
		VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

		// Submit daily usage request with filter date
		System.out.println("------------------------------------------------------");
		CreditDailyUsage result1 = restClient.getCredits().getDailyUsage(LocalDate.parse("2020-03-12"));
		if (result1 == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display results
			for (CreditDailyUsageData creditDailyUsageData: result1.getData()){
				System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
						creditDailyUsageData.getDate(),
						creditDailyUsageData.getCreditPacks(),
						creditDailyUsageData.getFreeCredits());
			}
		}

		// Submit daily usage request with filter dateSince and filter dateUntil
		System.out.println("------------------------------------------------------");
		CreditDailyUsage result2 = restClient.getCredits().getDailyUsage(LocalDate.parse("2020-03-12"),
				LocalDate.parse("2020-03-16"));
		if (result2 == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display results
			for (CreditDailyUsageData creditDailyUsageData: result2.getData()){
				System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
						creditDailyUsageData.getDate(),
						creditDailyUsageData.getCreditPacks(),
						creditDailyUsageData.getFreeCredits());
			}
		}

		// Submit daily usage request with filter object
		System.out.println("------------------------------------------------------");
		CreditDailyUsageFilter creditDailyUsageFilter1 = new CreditDailyUsageFilter(LocalDate.parse("2020-03-12"));
		CreditDailyUsage result3 = restClient.getCredits().getDailyUsage(creditDailyUsageFilter1);
		if (result2 == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display results
			for (CreditDailyUsageData creditDailyUsageData: result3.getData()){
				System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
						creditDailyUsageData.getDate(),
						creditDailyUsageData.getCreditPacks(),
						creditDailyUsageData.getFreeCredits());
			}
		}

		// Submit daily usage request with filter object
		System.out.println("------------------------------------------------------");
		CreditDailyUsageFilter creditDailyUsageFilter2 = new CreditDailyUsageFilter(LocalDate.parse("2020-03-12"),
				LocalDate.parse("2020-03-16"));
		CreditDailyUsage result4 = restClient.getCredits().getDailyUsage(creditDailyUsageFilter2);
		if (result2 == null) // Result is null if timeout expires
			System.err.println("Request timeout expired");
		else {
			// Display results
			for (CreditDailyUsageData creditDailyUsageData: result4.getData()){
				System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
						creditDailyUsageData.getDate(),
						creditDailyUsageData.getCreditPacks(),
						creditDailyUsageData.getFreeCredits());
			}
		}
	}
}
