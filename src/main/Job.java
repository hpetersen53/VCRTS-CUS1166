package main;

import java.time.LocalDate;

public class Job {
    private int clientID;
    private int levelOfRedundancy;
    private int jobDuration;
    private double payout;
    private String title;
    private LocalDate deadline;
    private String attachedFileName;

    public Job(int clientID, int levelOfRedundancy, int jobDuration, double payout, String title, LocalDate deadline, String attachedFileName) {
        this.clientID = clientID;
        this.levelOfRedundancy = levelOfRedundancy;
        this.jobDuration = jobDuration;
        this.payout = payout;
        this.title = title;
        this.deadline = deadline;
        this.attachedFileName = attachedFileName;
    }

    public String getDetails() {
        return "Job Title: " + title + "\nPayout: " + payout + "\nDeadline: " + deadline + "\nFile: " + (attachedFileName == null ? "None" : attachedFileName);
    }

    public String toFileString() {
        return clientID + "," + title + "," + payout + "," + deadline + "," + (attachedFileName == null ? "" : attachedFileName);
    }
    
    public int getID() {
    	return clientID;
    }
}

}