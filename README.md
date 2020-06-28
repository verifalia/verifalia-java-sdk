![Verifalia API](https://img.shields.io/badge/Verifalia%20API-v2.2-green)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.verifalia/verifalia-java-sdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.verifalia/verifalia-java-sdk)

Verifalia RESTful API - Java SDK and helper library
===================================================

[Verifalia][0] provides a simple HTTPS-based API for validating email addresses in
real-time and checking whether they are deliverable or not; this SDK library integrates with Verifalia
and allows to [verify email addresses][0] under Java 8 and above.

Learn more about Verifalia at [https://verifalia.com][0]

## Adding Verifalia REST API support to your Java project

The easiest way to add the Verifalia email verification SDK library to your Java project is to use
[Maven](https://maven.apache.org/) and add the following dependency to your `pom.xml` file:

```xml
<dependency>
      <groupId>com.github.verifalia</groupId>
      <artifactId>verifalia-java-sdk</artifactId>
      <version>LATEST</version>
</dependency>
```

#### Manual download and compilation

As an alternative way to add the Verifalia SDK to your Java project, you can 
clone the SDK source project from GitHub, compile it and install it into your local Maven repository:

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

This will also build the Javadoc in the `target/apidocs` folder: you can open the `index.html` located there to view it locally.

### Authentication

First things first: authentication to the Verifalia API is performed by way of either the credentials
 of your root Verifalia account or of one of its users (previously known as sub-accounts): if you don't
  have a Verifalia account, just [register for a free one][4].
For security reasons, it is always advisable to [create and use a dedicated user][3] for accessing
 the API, as doing so will allow to assign only the specific needed permissions to it.

Learn more about authenticating to the Verifalia API at [https://verifalia.com/developers#authentication][2]

Once you have your Verifalia credentials at hand, use them while creating a new instance of the
 `VerifaliaRestClient` class, which will be the starting point to every other operation against the
  Verifalia API: the supplied credentials will be automatically provided to the API using the HTTP
   Basic Auth method.

```java
import com.verifalia.api.VerifaliaRestClient;

// Create REST client object with your credentials

VerifaliaRestClient verifalia = new VerifaliaRestClient("username", "password");
```

In addition to the HTTP Basic Auth method, this SDK also supports other different ways to authenticate
 to the Verifalia API, as explained in the subsequent paragraphs.

#### Authenticating via bearer token

Bearer authentication offers higher security over HTTP Basic Auth, as the latter requires sending the
 actual credentials on each API call, while the former only requires it on a first, dedicated
  authentication request. On the other side, the first authentication request needed by Bearer
   authentication takes a non-negligible time: if you need to perform only a single request, using
    HTTP Basic Auth provides the same degree of security and is the faster option too.

```java
VerifaliaRestClient verifalia =
    new VerifaliaRestClient(new BearerAuthenticationProvider("username", "password"));
```

#### Authenticating via X.509 client certificate (TLS mutual authentication)

This authentication method uses a TLS client certificate stored in the Java Keystore (JKS) to authenticate
 against the Verifalia API. This method, also
 called mutual TLS authentication (mTLS) or two-way authentication, offers the highest degree of security,
  as only a cryptographically-derived key (and not the actual credentials) is sent over the wire on each
   request.

To use this authentication method, one needs to generate an identity keystore file from both the verifalia.com
public key and the client certificate private key, and finally import and trust the resulting JKS file;
To better understand how this works, please see [this tutorial](https://www.snaplogic.com/glossary/two-way-ssl-java-example).

Once the Java Keystore is configured, you can use the below code to use your TLS client certificate to
authenticate against the Verifalia API:

```java
VerifaliaRestClient verifalia =
    new VerifaliaRestClient(new ClientCertificateAuthenticationProvider(certAlias, certPassword, new File(identifyJksFilePath), new File(trustStoreJksFilePath)));
```
where `certAlias` is the alias used when creating the client certificate, `certPassword` is the password
 used when creating it, `identityJksFilePath` and `trustStoreJksFilePath` are the path of, respectively,
  the identity and trust store JKS files.

## Validating email addresses ##

Every operation related to verifying / validating email addresses is performed through the
 `emailValidations` property exposed by the `VerifaliaRestClient` instance you created above, whose
  getter returns an object with many useful methods for verifying email addresses: in the next few
   paragraphs we are looking at the most used ones, so it is strongly advisable to explore the library
    and look at the Javadoc for other opportunities.
    
### How to validate an email address

To validate an email address from your Java project you can call the `submit()` method: it accepts
 one or more email addresses and any eventual verification options you wish to pass to Verifalia,
  including the expected results quality, deduplication preferences and processing priority.

In the next example, we are showing how to verify a single email address using this library; as the
 entire process is asynchronous, we are passing a `WaitingStrategy` value, asking `submit()` to
  automatically wait for the job completion:

```java
Validation validation = verifalia
    .getEmailValidations()
    .submit("batman@gmail.com", new WaitingStrategy(true));

// At this point the address has been validated: let's print
// its email validation result to the console.

ValidationEntry entry = validation.getEntries().get(0);

System.out.printf("%s => Classification: %s, Status: %s\n",
    entry.getInputData(),
    entry.getClassification(),
    entry.getStatus());

// Prints out something like:
// batman@gmail.com => Classification: Deliverable, Status: Success
```

### How to validate a list of email addresses ###

As an alternative to method above, you can avoid automatically waiting and retrieve the email validation
 results at a later time; this is preferred in the event you are verifying a list of email addresses,
  which could take minutes or even hours to complete.

Here is how to do that:

```java
Validation validation = verifalia
    .getEmailValidations()
    .submit(new String[] {
		"batman@gmail.com",
		"steve.vai@best.music",
		"samantha42@yahoo.de"
	});

System.out.println("Job Id: " + validation.getOverview().getId());
System.out.println("Status: " + validation.getOverview().getStatus());

// Prints out something like:
// Job Id: 290b5146-eeac-4a2b-a9c1-61c7e715f2e9
// Status: InProgress
```

Once you have an email validation job Id, which is always returned by `submit()` as part of the
 validation's `overview` property, you can retrieve the job data using the `get()` method. Similarly
  to the submission process, you can either wait for the completion of the job or just retrieve the
   current job snapshot to get its progress, using an instance of the same `WaitingStrategy` class
    mentioned above. Only completed jobs have their `entries` properties filled with the email validation
     results, however.

In the following example, we are requesting the current snapshot of a given email validation job back
 from Verifalia:

```java
Validation validation = verifalia
    .getEmailValidations()
    .get("290b5146-eeac-4a2b-a9c1-61c7e715f2e9");

if (validation.getOverview().getStatus() == ValidationStatus.Completed) {
	// validation.getEntries() will have the validation results!
}
else {
	// What about having a coffee?
}
```

And here is how to request the same job, asking the SDK to automatically wait for us until the job
 is completed (that is, _joining_ the job):

```java
Validation validation = verifalia
    .getEmailValidations()
    .get("290b5146-eeac-4a2b-a9c1-61c7e715f2e9", new WaitingStrategy(true));
```

### Don't forget to clean up, when you are done ###

Verifalia automatically deletes completed jobs after 30 days since their completion: deleting completed
 jobs is a best practice, for privacy and security reasons. To do that, you can invoke the `delete()`
  method passing the job Id you wish to get rid of:

```java
verifalia
    .getEmailValidations()
    .delete(validation.getOverview().getId());
```

Once deleted, a job is gone and there is no way to retrieve its email validation(s).

### How to import email addresses from a file ###

In addition to submitting structured data (see the paragraphs above), it also possible to import the email addresses to
verify [from a file](https://verifalia.com/developers#email-validations-importing-file) provided by the user. Once submitted,
the email verification job follows the same flow as described above.

Verifalia accepts the following file types:
- plain text files (.txt), with one email address per line
- comma-separated values (.csv), tab-separated values (.tsv) and other delimiter-separated values files
- Microsoft Excel spreadsheets (.xls and .xlsx)

Here is how to extract and verify email addresses, for example, from the third column of the first sheet of an Excel workbook,
starting from the second row:

```java
FileValidationRequest request = new FileValidationRequest("my-list.xlsx",
    WellKnownMimeTypes.EXCEL_XLSX);

// request.setSheet(0); // 0 is the default value
request.setColumn(2); // zero-based column number
request.setStartingRow(1); // zero-based starting row number

Validation validation = verifalia
    .getEmailValidations()
    .submit(request);
```

### Iterating over your email validation jobs ###

For management and reporting purposes, you may want to obtain a detailed list of your past email
 validation jobs. This SDK library allows to do that through the `list()` method, which allows
  to iterate asynchronously over a collection of `ValidationOverview` instances (the same type
   of the `overview` property of the results returned by `submit()` and `get()`).

Here is how to iterate over your jobs, from the most recent to the oldest one:

```java
Iterable<ValidationOverview> jobs = verifalia
    .getEmailValidations()
    .list(ValidationOverviewListingOptions
        .builder()
        .direction(Direction.Backward)
        .build());

for (ValidationOverview job : jobs) {
	System.out.printf("Id: %s, submitted: %s, status: %s, entries: %d\n",
		jobOverview.getId(),
		jobOverview.getSubmittedOn(),
		jobOverview.getStatus(),
		jobOverview.getNoOfEntries());
}

// Prints out something like:
// Id: a7784f9a-86d4-436c-b8e4-f72f2bd377ac, submitted: 8/2/2019 10:27:29 AM, status: InProgress, entries: 9886
// Id: 86d57c00-147a-4736-88cc-c918260c67c6, submitted: 8/2/2019 10:27:29 AM, status: Completed, entries: 1
// Id: 594bbb0f-6f12-481c-926f-606cfefc1cd5, submitted: 8/2/2019 10:27:28 AM, status: Completed, entries: 1
// Id: a5c1cd5b-39cc-43bc-9a3a-ee4a0f80ee6d, submitted: 8/2/2019 10:27:26 AM, status: InProgress, entries: 226
// Id: b6f69e30-60dd-4c21-b2cb-e73ba75fb278, submitted: 8/2/2019 10:27:21 AM, status: Completed, entries: 12077
// Id: 5e5a97dc-459f-4edf-a607-47371c32aa94, submitted: 8/2/2019 10:27:18 AM, status: Deleted, entries: 1009
// ...
```

## Managing credits ##

To manage the Verifalia credits for your account you can use the `credits` property exposed by the
 `VerifaliaRestClient` instance created above. Like for the previous topic, in the next few paragraphs
  we are looking at the most used operations, so it is strongly advisable to explore the library and
   look at the embedded Javadoc for other opportunities.

### Getting the credits balance ###

One of the most common tasks you may need to perform on your account is retrieving the available number
 of free daily credits and credit packs. To do that, you can use the `getBalance()` method, which
  returns a `Balance` object, as shown in the next example:

```java
Balance balance = verifalia
    .getCredits()
    .getBalance();

System.out.printf("Credit packs: %d, free daily credits: %d (will reset in %s)\n",
	balance.getCreditPacks(),
	balance.getFreeCredits(),
	balance.getFreeCreditsResetIn());

// Prints out something like:
// Credit packs: 956.332, free daily credits: 128.66 (will reset in PT9H8M23S)
```

### Retrieving credits usage statistics ###

As a way to monitor and forecast the credits consumption for your account, the method `listDailyUsages()`
 allows to retrieve statistics about historical credits usage, returning an iterable collection
  of `DailyUsage` instances. The method also allows to limit the period of interest by passing a
   `DailyUsageListingOptions` instance. Elements are returned only for the dates where consumption
    (either of free credits, credit packs or both) occurred.

Here is how to retrieve the daily credits consumption for the last thirty days:

```java
DateBetweenPredicate lastThirtyDays = new DateBetweenPredicate();
lastThirtyDays.setSince(LocalDate.now().minusDays(30));

Iterable<DailyUsage> dailyUsages = verifalia
    .getCredits()
    .listDailyUsages(DailyUsageListingOptions
        .builder()
        .dateFilter(lastThirtyDays)
        .build());

for (DailyUsage dailyUsage : dailyUsages) {
	System.out.printf("%s - credit packs: %d, free daily credits: %d",
		dailyUsage.Date,
		dailyUsage.CreditPacks,
		dailyUsage.FreeCredits);
}

// Prints out something like:
// 2020-04-01 - credit packs: 1965.68, free daily credits: 200
// 2020-03-26 - credit packs: 0, free daily credits: 185.628
// 2020-03-25 - credit packs: 15.32, free daily credits: 200
// ...
```

[0]: https://verifalia.com
[2]: https://verifalia.com/developers#authentication
[3]: https://verifalia.com/client-area#/users/new
[4]: https://verifalia.com/sign-up
[5]: https://verifalia.com/client-area#/credits/add