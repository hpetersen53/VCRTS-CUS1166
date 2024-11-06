package main;

public abstract class User {
    protected String fName;
    protected String lName;
    protected String emailAddr;
    protected String licenseNum;
    protected String password;
    protected int id;  

    
    public User(int id, String fName, String lName, String emailAddr, String password, String licenseNum) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.emailAddr = emailAddr;
        this.password = password;
        this.licenseNum = licenseNum;
    }

    public int getID() {  
        return id;
    }

    public abstract String getDetails();
    
    public String toFileString() {
        return id + "," +fName + ", " + lName + ", " + emailAddr + ", " + password + ", " + licenseNum;
    }
}
