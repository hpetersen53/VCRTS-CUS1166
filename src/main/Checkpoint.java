package main;



public class Checkpoint {
    private static int idCounter = 0;
    private int checkpointID;
    private String name;
    private String location;
    

    public Checkpoint(String name, String location) {
        this.checkpointID = generateCheckpointID();
        this.name = name;
        this.location = location;
       
    }

public int generateCheckpointID() {
    return ++idCounter;
}

public int checkpointID() {
    return checkpointID;
}

public void createImage() {

}

public String getDetails() {
    return "Checkpoint: " + name + ", Location: " + location + ", ID: " + checkpointID;
}

}
