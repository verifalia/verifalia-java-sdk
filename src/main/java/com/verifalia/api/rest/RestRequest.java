package com.verifalia.api.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Represents REST request
 */
public class RestRequest {

	/**
	 * Constructs new object for a given resource with given method
	 */
	public RestRequest(HttpRequestMethod method, String resource) {
		this.method = method;
		this.resource = resource;
	}

	/**
	 * Returns request method
	 */
	public HttpRequestMethod getMethod() {
		return method;
	}

	/**
	 * Returns content resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * Add string to body
	 */
	public void addEntries(Iterable<String> entries) {
		if(this.entries == null)
			this.entries = new EntryList();

		for(String entry: entries)
			this.entries.addEntry(entry);
	}

	/**
	 * Returns request body
	 */
	public List<Entry> getEntries() {
		return entries != null ? entries.getEntries() : null;
	}

	/**
	 * Returns items encoded into JSON suitable for sending with HTTP
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String getEncodedEntries() throws JsonGenerationException, JsonMappingException, IOException {
		if(entries == null)
			return "";
		else {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(entries);
		}
	}

	/**
	 * HTTP request method
	 */
	HttpRequestMethod method;

	/**
	 * Request target resource
	 */
	private String resource;

	/**
	 * Represents single entry for the JSON request with list of emails
	 */
	public static class Entry {
		/**
		 * The data to be validated (usually, email address)
		 * Warning: this field must be called exactly "inputData" in order to generate proper JSON.
		 * Do not rename it.
		 */
		private String inputData;

		/**
		 * Returns data to be validated
		 */
		public String getInputData() {
			return inputData;
		}

		/**
		 * Constructs new object
		 */
		public Entry() {}

		/**
		 * Constructs new object
		 * @param inputData A data to be validated
		 */
		public Entry(String inputData) {
			this.inputData = inputData;
		}
	};

	/**
	 * Represents list of request entries
	 */
	private static class EntryList {
		/**
		 * List of entries.
		 * Warning: this field must be called exactly "entries" in order to generate proper JSON.
		 * Do not rename it.
		 */
		private List<Entry> entries = new ArrayList<Entry>();

		/**
		 * Returns list of entries
		 */
		public List<Entry> getEntries() {
			return this.entries;
		}

		/**
		 * Creates and adds new entry from given string
		 * @param str String to be added
		 */
		void addEntry(String str) {
			this.entries.add(new Entry(str));
		}
	};

	/**
	 * Request entries
	 */
	private EntryList entries;
}
