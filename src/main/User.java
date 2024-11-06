package main;

public abstract class User {
    protected String fName;
    protected String lName;
    protected String emailAddr;
    protected String licenseNum;
    protected String password;
    protected int id;  // Add this line

    // Adjust constructor to take id as an argument
    public User(int id, String fName, String lName, String emailAddr, String password, String licenseNum) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.emailAddr = emailAddr;
        this.password = password;
        this.licenseNum = licenseNum;
    }

    public int getID() {  // Add this method
        return id;
    }

    public abstract String getDetails();
    
    public String toFileString() {
        return fName + ", " + lName + ", " + emailAddr + ", " + licenseNum;
    }
}
