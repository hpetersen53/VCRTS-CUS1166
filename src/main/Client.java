package main;

public class Client extends User {
    public Client(int id, String fName, String lName, String emailAddr, String password, String licenseNum) {
        super(id, fName, lName, emailAddr, password, licenseNum);
        this.id = 0;
    }
    public void setID(int id) {  // Setter to update the client ID later
        this.id = id;
    }

    @Override
    public String getDetails() {
        return "Job Lister Details:\n" + "Name: " + fName + " " + lName + "\n" + "Email: " + emailAddr + "\n" + "License Number: " + licenseNum;
    }
    
    public void submitJob(Job job) {
        System.out.println("Job submitted by: " + fName + " " + lName);
    }
}
