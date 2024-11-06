package main;

import java.util.ArrayList;
import java.util.List;

public class VCController {
    private List<Vehicle> vehicles;
    private List<Job> jobs;
    private List<Checkpoint> checkpoints;

    public VCController() {
        this.vehicles = new ArrayList<>();
        this.jobs = new ArrayList<>();
        this.checkpoints = new ArrayList<>();
    }

    public void setRedundancy(Job job, int levelOfRedundancy) {
        //job.setLevelOfRedundancy(levelOfRedundancy);

    }

    public void recruitVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void assignJob(Vehicle vehicle, Job job) {
        if (vehicles.contains(vehicle) && jobs.contains(job)) {
        System.out.println("Job id: " + job.getID() + " assigned to Vehicle VIN: " +vehicle.getVIN());
    } else {
        System.out.println("Vehicle or Job not found in system. ");

    }
}

// this creates an image of a checkpoint and associates it with a vehicle
public void copyImage(Checkpoint checkpoint, Vehicle vehicle) {
    checkpoint.createImage();
    System.out.println("Image created for Checkpoint ID: " + checkpoint.checkpointID() + " that belongs to Vehicle VIN: " + vehicle.getVIN());

}

public void sendData() {

}

public void addCheckpoint(Checkpoint checkpoint) {
    checkpoints.add(checkpoint);

}

public void addJob(Job job) {
    jobs.add(job);
    
}

public List<Vehicle> getVehicles() {
    return vehicles;
}

public List<Job> getJobs() {
    return jobs;
}

public List<Checkpoint> getCheckpoints() {
    return checkpoints;
}


// Method to calculate job completion times
public List<Integer> calculateCompletion() {
    List<Integer> completionTimes = new ArrayList<>();
    int cumulativeTime = 0;

    for (Job job : jobs) {
        cumulativeTime += job.getJobDuration();
        completionTimes.add(cumulativeTime);
    }

    return completionTimes;
}
}
