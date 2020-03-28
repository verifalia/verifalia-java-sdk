package com.verifalia.api.samples;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.credits.models.CreditBalanceData;
import com.verifalia.api.credits.models.CreditDailyUsageData;
import com.verifalia.api.credits.models.CreditDailyUsageFilter;
import com.verifalia.api.emailvalidations.models.ValidationDeDuplication;
import com.verifalia.api.emailvalidations.models.ValidationEntriesFilter;
import com.verifalia.api.emailvalidations.models.ValidationEntryStatus;
import com.verifalia.api.emailvalidations.models.ValidationJobsFilter;
import com.verifalia.api.emailvalidations.models.ValidationJobsSort;
import com.verifalia.api.emailvalidations.models.ValidationQuality;
import com.verifalia.api.emailvalidations.models.ValidationStatus;
import com.verifalia.api.emailvalidations.models.output.Validation;
import com.verifalia.api.emailvalidations.models.output.ValidationEntry;
import com.verifalia.api.emailvalidations.models.output.ValidationOverview;
import com.verifalia.api.exceptions.VerifaliaException;
import com.verifalia.api.rest.security.BearerAuthentication;

public class Samples {

	public static void main(String[] args) {
		if(args.length >= 2) {
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
	void queryVerifaliaEmailValidationsServiceSample1(String accountSid, String authToken){

		System.out.println("------------------------ queryVerifaliaEmailValidationsServiceSample1 ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(accountSid, authToken);
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
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
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void queryVerifaliaEmailValidationsServiceSample2(String accountSid, String authToken) {

		System.out.println("------------------------ queryVerifaliaEmailValidationsServiceSample2 ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(new BearerAuthentication(accountSid, authToken));
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

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

		try {
			if(nonNull(restClient)){
				// Submit email verification request with method that returns immediately
				Validation result = restClient.getEmailValidations().submit(new String[] {
						"alice@example.com",
						"bob@example.net",
						"carol@example.org"
					}, ValidationQuality.High, WaitForCompletionOptions.DontWait
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
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void queryVerifaliaEmailValidationsServiceSample3(String accountSid, String authToken) {

		System.out.println("------------------------ queryVerifaliaEmailValidationsServiceSample3 ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(accountSid, authToken);
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit email verification request with method that return status immediately
				Validation result = restClient.getEmailValidations().submit(new String[] {
						"alice@example.com",
						"bob@example.net",
						"carol@example.org"
					}, ValidationQuality.Standard, ValidationDeDuplication.Relaxed, 255, WaitForCompletionOptions.DontWait
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
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void queryVerifaliaEmailValidationsOverviewSample(String accountSid, String authToken) {

		System.out.println("------------------------ queryVerifaliaEmailValidationsOverviewSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(new BearerAuthentication(accountSid, authToken));
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
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
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void queryVerifaliaEmailValidationsEntriesSample(String accountSid, String authToken) {

		System.out.println("------------------------ queryVerifaliaEmailValidationsEntriesSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(accountSid, authToken);
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit email verification request with method that return status immediately
				ValidationOverview result = restClient.getEmailValidations().submit(new String[] {
						"alice@example.com",
						"bob@example.net",
						"carol@example.org"
					}).getOverview();

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
					List<ValidationEntry> entries = restClient.getEmailValidations().queryEntries(result.getId());
					if(nonNull(entries) && entries.size() > 0){
						for (ValidationEntry entryData: entries){
							System.out.printf("Address: %s => Result: %s\n",
									entryData.getInputData(),
									entryData.getStatus()
							);
						}
					} else {
						System.out.println("No validation entry found for the request");
					}
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void queryVerifaliaEmailValidationsEntriesWithFiltersSample(String accountSid, String authToken) {

		System.out.println("------------------------ queryVerifaliaEmailValidationsEntriesWithFiltersSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(accountSid, authToken);
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		}

		ValidationOverview result = null;
		try {
			if(nonNull(restClient)){
				// Submit email verification request with method that return status immediately
				result = restClient.getEmailValidations().submit(new String[] {
						"alice@example.com",
						"bob@example.net",
						"carol@example.org"
					}
				).getOverview();

				// Submit request for getting entries with filter status
				System.out.println("------------------------------------------------------");
				List<ValidationEntry> entries1 = restClient.getEmailValidations().queryEntries(result.getId(), new ValidationEntryStatus[] {
					ValidationEntryStatus.Success,
					ValidationEntryStatus.DomainHasNullMx
				});
				if(nonNull(entries1) && entries1.size() > 0){
					System.out.println("Entries results with filter status: " + entries1.size());
					for (ValidationEntry entryData: entries1){
						System.out.printf("Address: %s => Result: %s\n",
								entryData.getInputData(),
								entryData.getStatus()
						);
					}
				} else {
					System.out.println("Entries results with filter status is in progress");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient) && nonNull(result)){
				// Submit request for getting entries with filter object
				System.out.println("------------------------------------------------------");
				ValidationEntriesFilter validationEntriesFilter = new ValidationEntriesFilter();
				validationEntriesFilter.setExcludeStatuses(Arrays.asList(new ValidationEntryStatus[] {
					ValidationEntryStatus.Success,
					ValidationEntryStatus.DomainHasNullMx
				}));
				validationEntriesFilter.setLimit(1000);
				List<ValidationEntry> entries2 = restClient.getEmailValidations().queryEntries(result.getId(), validationEntriesFilter);
				if(nonNull(entries2) && entries2.size() > 0){
					System.out.println("Entries results with filter object: " + entries2.size());
					for (ValidationEntry entryData: entries2){
						System.out.printf("Address: %s => Result: %s\n",
								entryData.getInputData(),
								entryData.getStatus()
						);
					}
				} else {
					System.out.println("Entries results with filter status is in progress");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void queryVerifaliaEmailValidationsJobsSample(String accountSid, String authToken) {

		System.out.println("------------------------ queryVerifaliaEmailValidationsJobsSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(new BearerAuthentication(accountSid, authToken));
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit email verification request with method that return status immediately
				List<ValidationOverview> validationJobsData = restClient.getEmailValidations().listJobs();
				if(nonNull(validationJobsData) && validationJobsData.size() > 0){
					System.out.println("Total validation jobs: " + validationJobsData.size());
					for (ValidationOverview validationOverview: validationJobsData){
						System.out.printf("ID: %s => Status: %s => No of Entries: %s => Completed On: %s\n",
								validationOverview.getId(),
								validationOverview.getStatus(),
								validationOverview.getNoOfEntries(),
								validationOverview.getCompletedOn()
						);
					}
				} else {
					System.out.println("No validation job data exists");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void queryVerifaliaEmailValidationsJobsWithFiltersSample(String accountSid, String authToken) {

		System.out.println("------------------------ queryVerifaliaEmailValidationsJobsWithFiltersSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(new BearerAuthentication(accountSid, authToken));
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit request for listing job with filter createdOn
				System.out.println("------------------------------------------------------");
				List<ValidationOverview> jobsResult1 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-25"));
				if(nonNull(jobsResult1) && jobsResult1.size() > 0){
					System.out.println("Jobs results with filter createdOn: " + jobsResult1.size());
					for (ValidationOverview validationOverview: jobsResult1){
						System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
								validationOverview.getId(),
								validationOverview.getStatus(),
								validationOverview.getNoOfEntries()
						);
					}
				} else {
					System.out.println("Jobs results with filter createdOn: 0");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit request for listing job with filter createdOn, status
				System.out.println("------------------------------------------------------");
				List<ValidationOverview> jobsResult2 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-25"),
						new ValidationStatus[] {
								ValidationStatus.Completed,
								ValidationStatus.InProgress,
				});
				if(nonNull(jobsResult2) && jobsResult2.size() > 0){
					System.out.println("Jobs results with filter createdOn and status: " + jobsResult2.size());
					for (ValidationOverview validationOverview: jobsResult2){
						System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
								validationOverview.getId(),
								validationOverview.getStatus(),
								validationOverview.getNoOfEntries()
						);
					}
				} else {
					System.out.println("Jobs results with filter createdOn and status: 0");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit request for listing job with filter filter createdOn and sort -createdOn
				System.out.println("------------------------------------------------------");
				List<ValidationOverview> jobsResult3 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-24"),
						ValidationJobsSort.CreatedOnDesc);
				if(nonNull(jobsResult3) && jobsResult3.size() > 0){
					System.out.println("Jobs results with filter createdOn and sort -createdOn: " + jobsResult3.size());
					for (ValidationOverview validationOverview: jobsResult3){
						System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
								validationOverview.getId(),
								validationOverview.getStatus(),
								validationOverview.getNoOfEntries()
						);
					}
				} else {
					System.out.println("Jobs results with filter createdOn and sort -createdOn: 0");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit request for listing job with filter createdOn, status and sort createdOn
				System.out.println("------------------------------------------------------");
				List<ValidationOverview> jobsResult4 = restClient.getEmailValidations().listJobs(LocalDate.parse("2020-03-25"),
						new ValidationStatus[] {
								ValidationStatus.Completed,
								ValidationStatus.InProgress
				}, ValidationJobsSort.CreatedOnAsc);
				if(nonNull(jobsResult4) && jobsResult4.size() > 0){
					System.out.println("Jobs results with filter createdOn, status and sort createdOn: " + jobsResult4.size());
					for (ValidationOverview validationOverview: jobsResult4){
						System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
								validationOverview.getId(),
								validationOverview.getStatus(),
								validationOverview.getNoOfEntries()
						);
					}
				} else {
					System.out.println("Jobs results with filter createdOn, status and sort createdOn: 0");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit request for listing job with object
				System.out.println("------------------------------------------------------");
				ValidationJobsFilter validationJobsFilter = new ValidationJobsFilter();
				validationJobsFilter.setCreatedOnSince(LocalDate.parse("2020-03-24"));
				validationJobsFilter.setCreatedOnUntil(LocalDate.parse("2020-03-25"));
				validationJobsFilter.setExcludeStatuses(Arrays.asList(new ValidationStatus[] {
						ValidationStatus.Expired
				}));
				validationJobsFilter.setSort(ValidationJobsSort.CreatedOnDesc);
				List<ValidationOverview> jobsResult5 = restClient.getEmailValidations().listJobs(validationJobsFilter);
				if(nonNull(jobsResult5) && jobsResult5.size() > 0){
					System.out.println("Jobs results with object: " + jobsResult5.size());
					for (ValidationOverview validationOverview: jobsResult5){
						System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
								validationOverview.getId(),
								validationOverview.getStatus(),
								validationOverview.getNoOfEntries()
						);
					}
				} else {
					System.out.println("Jobs results with object: 0");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void getVerifaliaCreditsBalanceSample(String accountSid, String authToken) {

		System.out.println("------------------------ getVerifaliaCreditsBalanceSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(new BearerAuthentication(accountSid, authToken));
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
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
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void getVerifaliaCreditsDailyUsageSample(String accountSid, String authToken) {

		System.out.println("------------------------ getVerifaliaCreditsDailyUsageSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(new BearerAuthentication(accountSid, authToken));
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit email verification request with waiting parameters
				List<CreditDailyUsageData> result = restClient.getCredits().getDailyUsage();
				if(nonNull(result) && result.size() > 0){
					// Display results
					for (CreditDailyUsageData creditDailyUsageData: result){
						System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
								creditDailyUsageData.getDate(),
								creditDailyUsageData.getCreditPacks(),
								creditDailyUsageData.getFreeCredits());
					}
				} else {
					System.out.println("No credit daily usage data was found");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
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
	void getVerifaliaCreditsDailyUsageWithFiltersSample(String accountSid, String authToken) {

		System.out.println("------------------------ getVerifaliaCreditsDailyUsageWithFiltersSample ------------------------");

		VerifaliaRestClient restClient = null;
		try {
			// Create REST client object with your credentials
			restClient = new VerifaliaRestClient(new BearerAuthentication(accountSid, authToken));
		} catch(URISyntaxException e){
			System.out.println("URISyntaxException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit daily usage request with filter date
				System.out.println("------------------------------------------------------");
				List<CreditDailyUsageData> result1 = restClient.getCredits().getDailyUsage(LocalDate.parse("2020-03-25"));
				if(nonNull(result1) && result1.size() > 0){
					// Display results
					for (CreditDailyUsageData creditDailyUsageData: result1){
						System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
								creditDailyUsageData.getDate(),
								creditDailyUsageData.getCreditPacks(),
								creditDailyUsageData.getFreeCredits());
					}
				} else {
					System.out.println("No result found for daily usage");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit daily usage request with filter dateSince and filter dateUntil
				System.out.println("------------------------------------------------------");
				List<CreditDailyUsageData> result2 = restClient.getCredits().getDailyUsage(LocalDate.parse("2019-01-24"),
						LocalDate.parse("2020-03-25"));
				if(nonNull(result2) && result2.size() > 0){
					// Display results
					for (CreditDailyUsageData creditDailyUsageData: result2){
						System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
								creditDailyUsageData.getDate(),
								creditDailyUsageData.getCreditPacks(),
								creditDailyUsageData.getFreeCredits());
					}
				} else {
					System.out.println("No result found for daily usage");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit daily usage request with filter object
				System.out.println("------------------------------------------------------");
				CreditDailyUsageFilter creditDailyUsageFilter1 = new CreditDailyUsageFilter(LocalDate.parse("2020-03-25"));
				List<CreditDailyUsageData> result3 = restClient.getCredits().getDailyUsage(creditDailyUsageFilter1);
				if(nonNull(result3) && result3.size() > 0){
					// Display results
					for (CreditDailyUsageData creditDailyUsageData: result3){
						System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
								creditDailyUsageData.getDate(),
								creditDailyUsageData.getCreditPacks(),
								creditDailyUsageData.getFreeCredits());
					}
				} else {
					System.out.println("No result found for daily usage");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}

		try {
			if(nonNull(restClient)){
				// Submit daily usage request with filter object
				System.out.println("------------------------------------------------------");
				CreditDailyUsageFilter creditDailyUsageFilter2 = new CreditDailyUsageFilter(LocalDate.parse("2020-02-24"),
						LocalDate.parse("2020-03-25"));
				List<CreditDailyUsageData> result4 = restClient.getCredits().getDailyUsage(creditDailyUsageFilter2);
				if(nonNull(result4) && result4.size() > 0){
					// Display results
					for (CreditDailyUsageData creditDailyUsageData: result4){
						System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
								creditDailyUsageData.getDate(),
								creditDailyUsageData.getCreditPacks(),
								creditDailyUsageData.getFreeCredits());
					}
				} else {
					System.out.println("No result found for daily usage");
				}
			}
		} catch(VerifaliaException e){
			System.out.println("VerifaliaException:: " + e.getMessage());
		} catch(IOException e){
			System.out.println("IOException:: " + e.getMessage());
		}
	}
}
