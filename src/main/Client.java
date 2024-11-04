package main;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private List<Job> jobsSubmitted;

    public Client() {
        jobsSubmitted = new ArrayList<>();
    }

    public void submitJob(Job job) {
        jobsSubmitted.add(job);
        System.out.println("Job submitted successfully: " + job.getDetails());
    }

    public List<Job> getJobsSubmitted() {
        return jobsSubmitted;
    }
}
