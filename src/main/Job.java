package main;

import java.time.LocalDate;
import java.io.Serializable;

public class Job implements Serializable {
	private static final long serialVersionUID = 1L; // Recommended for Serializable classes

	private int clientID;
	private int levelOfRedundancy;
	private int jobDuration; // Estimated time in hours
	private double payout;
	private String title;
	private LocalDate deadline;
	private String attachedFileName;

	// Constructor
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

	// Getters and Setters
	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getLevelOfRedundancy() {
		return levelOfRedundancy;
	}

	public void setLevelOfRedundancy(int levelOfRedundancy) {
		this.levelOfRedundancy = levelOfRedundancy;
	}

	public int getJobDuration() {
		return jobDuration;
	}

	public void setJobDuration(int jobDuration) {
		this.jobDuration = jobDuration;
	}

	public double getPayout() {
		return payout;
	}

	public void setPayout(double payout) {
		this.payout = payout;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public String getAttachedFileName() {
		return attachedFileName;
	}

	public void setAttachedFileName(String attachedFileName) {
		this.attachedFileName = attachedFileName;
	}

	// Method to get job details as a formatted string
	public String getDetails() {
		return "Job Title: " + title + "\n" +
				"Payout: " + payout + "\n" +
				"Deadline: " + deadline + "\n" +
				"File: " + (attachedFileName == null ? "None" : attachedFileName);
	}

	// Method to format job details for saving to a file
	public String toFileString() {
		return "Client ID: " + clientID + "\n" +
				"Level of Redundancy: " + levelOfRedundancy + "\n" +
				"Job Duration: " + jobDuration + "\n" +
				"Payout: " + payout + "\n" +
				"Title: " + title + "\n" +
				"Deadline: " + deadline + "\n" +
				"FileName: " + (attachedFileName != null ? attachedFileName : "N/A") + "\n";
	}
}
