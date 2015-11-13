package com.verifalia.api.emailvalidation.models;

/**
 * Represents the completion progress of an email validation batch.
 */
public class ValidationProgress {
    /**
     * The number of total entries of the batch.
     */
    private int noOfTotalEntries;

    /**
     * Returns number of total entries of the batch.
     */
    public int getNoOfTotalEntries() {
		return noOfTotalEntries;
	}
	
    /**
     * The number of completed entries of the batch.
     */
    private int noOfCompletedEntries;

    /**
     * Returns number of completed entries of the batch.
     */
    public int getNoOfCompletedEntries() {
		return noOfCompletedEntries;
	}

    /**
     * Returns the number of pending entries of the batch.
     */
    public int getNoOfPendingEntries(){
    	return noOfTotalEntries - noOfCompletedEntries;
    }

    /**
     * Returns the progress percentage of the batch.
     */
    public double getPercentage() {
    	return (100.0 * noOfCompletedEntries) / noOfTotalEntries;
    }
    
    /**
	 * Converts this object into human-readable string representation.
     */
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(noOfCompletedEntries).append('/');
		sb.append(noOfTotalEntries).append(' ').append(getPercentage()).append("%");
		return sb.toString();
	}
}