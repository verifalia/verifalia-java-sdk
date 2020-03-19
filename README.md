![Verifalia API](https://img.shields.io/badge/Verifalia%20API-v2.1-green)
[![Build Status](https://travis-ci.org/verifalia/verifalia-java-sdk.png?branch=master)](https://travis-ci.org/verifalia/verifalia-java-sdk)

Verifalia REST API - Java SDK and helper library
================================================

Verifalia provides a simple HTTPS-based API for validating email addresses and checking whether or not they are deliverable. 
Learn more at http://verifalia.com

## Installation ##

Verifalia REST API Java SDK is going to use public Maven repository soon. We will update this page when JARs are available from a public maven repository.

Meanwhile you need to compile it yourself, here's how:

1. Download the Verifalia REST API Java SDK source code from GitHub.
2. Compile and install it into your local Maven repository:

```shell
$ git clone git@github.com:verifalia/verifalia-java-sdk
$ cd verifalia-java-sdk
$ mvn install       # Requires maven, download from http://maven.apache.org/download.html
```
or, alternatively:

```shell
$ wget https://github.com/verifalia/verifalia-java-sdk/archive/master.zip
$ unzip master.zip
$ cd verifalia-java-sdk-master
$ mvn install       # Requires maven, download from http://maven.apache.org/download.html
```

This will also build the javadoc in target/apidocs. You can open the index.html located there to view it locally.

## Adding to Java Project ##

Specify the Verifalia REST API Java SDK as a dependency of your Java project:

```xml
  	<dependency>
  		<groupId>com.github.verifalia</groupId>
  		<artifactId>verifalia-java-sdk</artifactId>
  		<version>1.0.0</version>
		<scope>compile</scope>
  	</dependency>
```


## Validating email addresses ##

The example below shows how to validate a couple of email addresses using the Verifalia REST API Java SDK:

```java
import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.emailvalidation.models.Validation;
import com.verifalia.api.emailvalidation.models.ValidationEntry;

// Create REST client object with your credentials
VerifaliaRestClient restClient = new VerifaliaRestClient("YOUR-ACCOUNT-SID", "YOUR-AUTH-TOKEN");

// Submit email verification request with waiting parameters
Validation result = restClient.getEmailValidations().submit(new String[] { 
		"alice@example.com",
		"bob@example.net",
		"carol@example.org"
	},
	new WaitForCompletionOptions(10 * 60) // in seconds
);

if (result == null) { // Result is null if timeout expires
	System.err.println("Request timeout expired");
}
else {
	// Display results
	for (ValidationEntry entry: result.getEntries())
	{
		System.out.printf("Address: %s => Result: %s\n",
			entry.getInputData(),
			entry.getStatus()
		);
	}
}
```

Internally, the `submit()` method sends the email addresses to the Verifalia servers and then polls them until the validations complete.
Instead of relying on the automatic polling behavior, you may choose to poll Verifalia for your request status manually using the `query()` method, as shown below:

```java
import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.emailvalidation.models.Validation;
import com.verifalia.api.emailvalidation.models.ValidationEntry;
import com.verifalia.api.emailvalidation.models.ValidationStatus;
import com.verifalia.api.exceptions.VerifaliaException;

// Create REST client object with your credentials
VerifaliaRestClient restClient = new VerifaliaRestClient("YOUR-ACCOUNT-SID", "YOUR-AUTH-TOKEN");

// Submit email verification request with method that return status immediately
Validation result = restClient.getEmailValidations().submit(new String[] { 
		"alice@example.com",
		"bob@example.net",
		"carol@example.org"
	}
);

// Loop until request processing is completed or execution thread is interrupted
while (result.getStatus() != ValidationStatus.Completed)
{
	result = restClient.getEmailValidations().query(result.getUniqueID(), WaitForCompletionOptions.DontWait);
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
	for (ValidationEntry entry: result.getEntries())
	{
		System.out.printf("Address: %s => Result: %s\n",
			entry.getInputData(),
			entry.getStatus()
		);
	}
}
```

### Advanced usage: tracking polling progress ###

In more advanced scenarios, you may use the built-in timeout-based polling with a custom listener object that intercepts internal polling loop events, in order to be able to track polling progress.
Here is an example showing how to use it:

```java
import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.emailvalidation.models.Validation;
import com.verifalia.api.emailvalidation.models.ValidationEntry;
import com.verifalia.api.emailvalidation.models.ValidationStatus;

// Create REST client object with your credentials
VerifaliaRestClient restClient = new VerifaliaRestClient("YOUR-ACCOUNT-SID", "YOUR-AUTH-TOKEN");

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
if(result.getStatus() != ValidationStatus.Completed)
	result = restClient.getEmailValidations().query(
		result.getUniqueID(), 
		new WaitForCompletionOptions(10*60), // in seconds
		new ServerPollingLoopEventListenerImpl()
	);

if (result == null) // Result is null if timeout expires
	System.err.println("Request timeout expired");
else {
	// Display results
	for (ValidationEntry entry: result.getEntries())
	{
		System.out.printf("Address: %s => Result: %s\n",
			entry.getInputData(),
			entry.getStatus()
		);
	}
}
```

### Don't forget to clean up, when you are done ###

Verifalia automatically deletes completed jobs after 30 days since their completion: deleting completed jobs is a best practice, for privacy and security reasons. To do that, you can invoke the delete() method passing the job Id you wish to get rid of:

```java

VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

restClient.getEmailValidations().delete(id);
```

Once deleted, a job is gone and there is no way to retrieve its email validation(s).

### Iterating over your email validation jobs ###

For management and reporting purposes, you may want to obtain a detailed list of your past email validation jobs. This SDK library allows to do that through the listJobs() method, which allows to iterate asynchronously over a collection of ValidationOverview instances (the same type of the Overview property of the results returned by submit()).

Here is how to iterate over your jobs, from the most recent to the oldest one for a given date:

```java

VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

ValidationJobs jobs = restClient.getEmailValidations().listJobs("2020-03-17", "-createdOn");

for (ValidationOverview validationOverview: jobs.getData()){
	System.out.printf("ID: %s => Status: %s => No of Entries: %s\n",
		validationOverview.getId(),
		validationOverview.getStatus(),
		validationOverview.getNoOfEntries()
	);
}

```

## Managing credits ##

To manage the Verifalia credits for your account you can use the Credits property exposed by the VerifaliaRestClient instance created above. Like for the previous topic, in the next few paragraphs we are looking at the most used operations, so it is strongly advisable to explore the library and look at the embedded xmldoc help for other opportunities.

### Getting the credits balance ###

One of the most common tasks you may need to perform on your account is retrieving the available number of free daily credits and credit packs. To do that, you can use the balance() method, which returns a Balance object, as shown in the next example:

```java
VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

CreditBalanceData result = restClient.getCredits().balance();

System.out.printf("Credit Packs: %s => Free Credits: %s => Free Credits Reset In: %s\n",
	result.getCreditPacks(),
	result.getFreeCredits(),
	result.getFreeCreditsResetIn());
```

### Retrieving credits usage statistics ###

As a way to monitor and forecast the credits consumption for your account, the method dailyUsage() allows to retrieve statistics about historical credits usage, returning an asynchronously iterable collection of CreditBalanceData instances. The method also allows to limit the period of interest by passing a CreditDailyUsageFilter instance. Elements are returned only for the dates where consumption (either of free credits, credit packs or both) occurred.

Here is how to retrieve the daily credits consumption for the specific date period:

```java

VerifaliaRestClient restClient = new VerifaliaRestClient(accountSid, authToken);

//CreditDailyUsage usageResult = restClient.getCredits().dailyUsage("2020-03-12", "2020-03-16");

CreditDailyUsageFilter creditDailyUsageFilter = new CreditDailyUsageFilter("2020-03-12", "2020-03-16");
CreditDailyUsage usageResult = restClient.getCredits().dailyUsage(creditDailyUsageFilter);

// Display results
for (CreditBalanceData creditBalanceData: usageResult.getData()){
	System.out.printf("Date: %s =>, Credit Packs: %s => Free Credits: %s\n",
		creditBalanceData.getDate(),
		creditBalanceData.getCreditPacks(),
		creditBalanceData.getFreeCredits());
}
```

You may find these samples in the bundled file [Samples.java](https://github.com/verifalia/verifalia-java-sdk/blob/apiVersionUpgradeFrom1.0To2.1/src/main/java/com/verifalia/api/samples/Samples.java)
