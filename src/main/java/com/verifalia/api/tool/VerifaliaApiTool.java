package com.verifalia.api.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.WaitForCompletionOptions;
import com.verifalia.api.common.Constants;
import com.verifalia.api.common.ServerPollingLoopEventListener;
import com.verifalia.api.emailvalidations.models.Validation;

public class VerifaliaApiTool {

	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("Missing required arguments. Try supplying option --help to find out more.");
			System.exit(1);
		}
		int status = 0;
		try {
			String command = args[0];
			if(command.equals("--help")) {
				showHelp();
			} else if(command.equals("--version")) {
				showVersion();
			} else if(command.equals("--submit")) {
				status = submitJob(args);
			} else if(command.equals("--status")) {
				status = checkJobStatus(args);
			} else if(command.equals("--delete")) {
				status = deleteJob(args);
			} else {
				System.err.println("Error: Invalid command '" + command + "'.");
				status = 2;
			}
		} catch(Exception ex) {
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
			status = 3;
		}
		System.exit(status);
	}

	private static int deleteJob(String[] args) throws IOException, URISyntaxException {
		if(args.length < 3) {
			System.err.println("Error: Too few arguments for the command '" + args[0] + "'.");
			return 1;
		}
		System.out.println("Deleting job (id=" + args[1] + "...");
		VerifaliaRestClient client = new VerifaliaRestClient(args[2], args[3]);
		client.getEmailValidations().delete(args[1]);
		return 0;
	}

	private static int checkJobStatus(String[] args) throws URISyntaxException, IOException {
		if(args.length < 4) {
			System.err.println("Error: Too few arguments for the command '" + args[0] + "'.");
			return 1;
		}

		WaitForCompletionOptions waitOptions = WaitForCompletionOptions.DontWait;
		if(args.length >= 6) {
			int timeout = Integer.parseInt(args[4]);
			int period = Integer.parseInt(args[5]);
			waitOptions = new WaitForCompletionOptions(timeout, period);
		}

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

		System.out.println("Querying job status (uniqueId=" + args[1] + ")...");
		VerifaliaRestClient client = new VerifaliaRestClient(args[2], args[3]);
		Validation validation = client.getEmailValidations().query(args[1], waitOptions, new ServerPollingLoopEventListenerImpl());
		if(validation != null){
			System.out.println("Result:");
			System.out.println(validation.toString());
		}
		else {
			System.err.println("Warning: Timeout expired!");
		}
		return 0;
	}

	private static int submitJob(String[] args) throws URISyntaxException, IOException {
		if(args.length < 4) {
			System.err.println("Error: Too few arguments for the command '" + args[0] + "'.");
			return 1;
		}

		System.out.println("Submitting new  job...");
		ArrayList<String> emails = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[1]))))) {
			int lineNo = 0;
			String line;
			while((line = br.readLine()) != null) {
				++lineNo;
				line = line.trim();
				if(line.isEmpty() || line.charAt(0) == '#' || line.charAt(0) == ';')
					continue;
				else {
					emails.add(line);
				}
			}
			System.out.println("Info: Read " + lineNo + " line(s), saved " + emails.size() + " line(s).");
		}

		VerifaliaRestClient client = new VerifaliaRestClient(args[2], args[3]);
		Validation validation = client.getEmailValidations().submit(emails);
		System.out.println("New job ID: " + validation.getOverview().getId());
		System.out.println("Result:");
		System.out.println(validation.toString());
		return 0;
	}

	private static void showHelp() {
		System.out.println(
				"Usage: " + Constants.PROGRAM_NAME + "<command> [parameters]\n"
				+ "\n"
				+ "Commands:\n"
				+ "--submit <file> <sid> <token>    Submit new job with email list from a given file\n"
				+ "--check <job id> <sid> <token>   Check job status\n"
				+ "--delete <job id>                Delete given job\n"
				+ "--version                        Show program version\n"
				+ "--help                           Show this help message\n"
		);
	}

	private static void showVersion() {
		System.out.println(
				Constants.PROGRAM_NAME + " ver. " + Constants.PROGRAM_VERSION
			+ ". Copyright (c) 2015 Ivan Pizhenko. All rights reserved."
		);
	}
}
