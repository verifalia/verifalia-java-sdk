[![Build Status](https://travis-ci.org/verifalia/verifalia-java-sdk.png?branch=master)](https://travis-ci.org/verifalia/verifalia-java-sdk)

Verifalia REST API - Java SDK and helper library
================================================

Verifalia provides a simple HTTPS-based API for validating email addresses and checking whether or not they are deliverable. 
Learn more at http://verifalia.com

## Installation ##

Download the Verifalia REST API Java SDK source code from GitHub.
Compile and install it into your local Maven repository.

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

## Adding to Java Project ##

Specify the Verifalia REST API Java SDK as a dependency of your Java project:

```xml
  	<dependency>
  		<groupId>com.verifalia.api</groupId>
  		<artifactId>verifalia-java-sdk</artifactId>
  		<version>1.0.0</version>
		<scope>compile</scope>
  	</dependency>
```


### Sample usage ###

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

You may find these samples in the bundled file [Samples.java](https://github.com/verifalia/verifalia-java-sdk/tree/master/src/main/java/com/verifalia/api/samples/Samples.java)
