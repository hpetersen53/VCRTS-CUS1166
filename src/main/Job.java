package main;

import java.time.LocalDate;
import java.io.Serializable;

public class Job implements Serializable{
	private int clientID;
	private int levelOfRedundancy;
	private int jobDuration;
	private double payout;
	private String title;
	private LocalDate deadline;
	private String attachedFileName;

	public Job(int clientID, int levelOfRedundancy, int jobDuration, double payout, String title, LocalDate deadline,
			String attachedFileName) {
		this.clientID = clientID;
		this.levelOfRedundancy = levelOfRedundancy;
		this.jobDuration = jobDuration;
		this.payout = payout;
		this.title = title;
		this.deadline = deadline;
		this.attachedFileName = attachedFileName;
	}

	public String getDetails() {
		return "Job Title: " + title + "\nPayout: " + payout + "\nDeadline: " + deadline + "\nFile: "
				+ (attachedFileName == null ? "None" : attachedFileName);
	}

	public String toFileString() {
	    return "Client ID: " + this.clientID + "\n" +
	           "Level of redundancy: " + this.levelOfRedundancy + "\n" +
	           "Job Duration: " + this.jobDuration + "\n" +
	           "Payout: " + this.payout + "\n" +
	           "Title: " + this.title + "\n" +
	           "Deadline: " + this.deadline + "\n" +
	           "FileName: " + (this.attachedFileName != null ? this.attachedFileName : "N/A") + "\n";
	}


	public int getID() {
		return clientID;
	}

	public void setLevelOfRedundancy(int levelOfRedundancy2) {
		this.levelOfRedundancy = levelOfRedundancy;
		
	}

	public int getJobDuration() {
		return jobDuration;
	}
}
