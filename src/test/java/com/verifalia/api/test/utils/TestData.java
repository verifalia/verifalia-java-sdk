package com.verifalia.api.test.utils;

public final class TestData {
	public static final String JSON_STRING =
			"\n"
			+ "{\n"
			+ "  'id': '9294d0e3-650a-4a7b-ad7d-d859905110a4',\n"
			+ "  'status': 'Completed',\n"
			+ "  'engineVersion': '1.0.0.0',\n"
			+ "  'submittedOn': '2015-11-08T06:00:00',\n"
			+ "  'completedOn': '2015-11-08T06:00:10',\n"
			+ "  'progress' : {\n"
			+ "      'noOfTotalEntries': 2,\n"
			+ "      'noOfCompletedEntries': 2\n"
			+ "   },\n"
			+ "  'entries': [\n"
			+ "     {\n"
			+ "       'isSuccess': true,\n"
			+ "       'inputData': 'some.address@email.com',\n"
			+ "       'emailAddress': 'some.address@email.com',\n"
			+ "       'completedOn': '2015-11-08T06:00:05',\n"
			+ "       'status': 'Success'\n"
			+ "     },\n"
			+ "     {\n"
			+ "       'isSuccess': true,\n"
			+ "       'inputData': 'another.address@email.com',\n"
			+ "       'emailAddress': 'another.address@email.com',\n"
			+ "       'completedOn': '2015-11-08T06:00:10',\n"
			+ "       'status': 'Success'\n"
			+ "     }\n"
			+ "   ]\n"
			+ "}\n"
	;

	public static final String[] JSON_ARRAY = JSON_STRING.split("[\\r\\n]+");
}
