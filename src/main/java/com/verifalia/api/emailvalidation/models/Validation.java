package com.verifalia.api.emailvalidation.models;

import java.util.Date;
import java.util.List;

/**
 * Represents a snapshot of an email validation batch.
 */
public class Validation {
	
	/**
     * The unique identifier of the email validation batch.
     */
    private String uniqueID = "";

    /**
     * The status of this batch.
     */
    private ValidationStatus status;
    
    /**
     * The internal version of the Verifalia engine which provided this snapshot.
     */
    private String engineVersion;

    /**
     * The date the batch has been submitted to Verifalia.
     */
    private Date submittedOn;

    /**
     * The date the batch has been completed.
     */
    private Date completedOn;

    /**
     * A list of results for the email validation batch.
     */
    private List<ValidationEntry> entries;

    /**
     * The completion progress for the batch.
     */
    private ValidationProgress progress;

	/**
     * Returns unique identifier of the email validation batch.
     */
	public String getUniqueID() {
		return uniqueID;
	}

    /**
     * Returns status of this batch.
     */
	public ValidationStatus getStatus() {
		return status;
	}

    /**
     * Sets status of this batch.
     */
	public void setStatus(ValidationStatus status) {
		this.status = status;
	}

    /**
     * Returns internal version of the Verifalia engine which provided this snapshot.
     */
	public String getEngineVersion() {
		return engineVersion;
	}

    /**
     * Returns date the batch has been submitted to Verifalia.
     */
	public Date getSubmittedOn() {
		return submittedOn;
	}

    /**
     * Returns date the batch has been completed.
     */
	public Date getCompletedOn() {
		return completedOn;
	}

    /**
     * Returns list of results for the email validation batch.
     */
	public List<ValidationEntry> getEntries() {
		return entries;
	}

    /**
     * Returns completion progress for the batch.
     */
	public ValidationProgress getProgress() {
		return progress;
	}
	
	/** 
	 * Converts this object into human-readable string representation.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{\n");
		
		if(uniqueID != null) 
			sb.append("\tuniqueID: ").append(uniqueID.toString()).append('\n');
		
		if(status != null)
			sb.append("\tstatus: ").append(status.name()).append('\n');
		
		if(engineVersion != null)
			sb.append("\tengineVersion: ").append(engineVersion).append('\n');
		
		if(submittedOn != null)
			sb.append("\tsubmittedOn: ").append(submittedOn).append('\n');
		
		if(completedOn != null)
			sb.append("\tcompletedOn: ").append(completedOn).append('\n');
		
		if(progress != null)
			sb.append("\tprogress: ").append(progress.toString()).append('\n');
		
		int entryCount = entries != null ? entries.size() : 0;
		sb.append("\tentries: count=").append(entryCount).append('\n');
		
		if(entryCount > 0)
		{
			int i = 0;
			for(ValidationEntry e: entries)
			{
				sb.append("\tEntry #").append(i).append(": \n");
				sb.append(e.toString());
				++i;
			}
		}
		
		sb.append("}\n");
		
		return sb.toString();
	}	
}
