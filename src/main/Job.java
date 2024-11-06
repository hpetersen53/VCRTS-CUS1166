package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Job {

	private int ID;
	private int levelOfRedundancy;
	private double jobDuration;
	private double payout;
	private String title;
	private LocalDate deadline;
	private String attachedFileName;

	 public Job(int ID, int levelOfRedundancy, double jobDuration, double payout, String title, LocalDate deadline, String attachedFileName) {
		this.ID = ID;
		this.levelOfRedundancy = levelOfRedundancy;
		this.title = title;
		this.payout = payout;
		this.jobDuration = jobDuration;
		this.attachedFileName = attachedFileName;
		this.deadline = deadline; 
	}

	public String getDetails() {
		return "Job ID: " + ID +
                "\nTitle: " + title +
                "\nPayout: $" + payout +
                "\nDuration: " + jobDuration +
                "\nDeadline: " + (deadline != null ? deadline.toString() : "None") +
                "\nAttached File: " + (attachedFileName != null ? attachedFileName : "None");
    }
	
	public int getRedundancy() {
		 return levelOfRedundancy;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

	public String toFileString() {
		return ID + "," +  levelOfRedundancy + "," + jobDuration + "," + payout + "," + title + "," +
                (deadline != null ? deadline.toString() : "None") + "," +
                (attachedFileName != null ? attachedFileName : "None");
    }


//method to get job duration for vccontroller
public double getJobDuration() {
	return jobDuration;
}

}

}