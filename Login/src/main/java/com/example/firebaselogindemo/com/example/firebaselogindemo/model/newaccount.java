package com.example.firebaselogindemo.com.example.firebaselogindemo.model;

public class newaccount {
    private String uemail;
    private String uid;
    private String fname;
    private String lname;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public newaccount(String uid, String uemail) {
        this.uemail = uemail;
        this.uid = uid;
    }

    public newaccount(){

    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
