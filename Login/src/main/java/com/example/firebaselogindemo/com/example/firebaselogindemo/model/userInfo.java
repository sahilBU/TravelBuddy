package com.example.firebaselogindemo.com.example.firebaselogindemo.model;

public class userInfo {

    private String userName;
    private String gender;
    private String email;
    private String picUrl;
    private String uid;

    public userInfo(){
    }

    public  userInfo(String userName, String gender, String email,String picUrl){
        this.userName = userName;
        this.gender = gender;
        this.email = email;
        this.picUrl = picUrl;
    }

    public String getUid() {return uid;}
    public void setUid(String uid) { this.uid = uid; }

    public String getUserName(){ return  userName;}

    public void setUserName(String userName){ this.userName = userName; }

    public String getGender(){ return gender; }

    public void setGender(String gender){ this.gender = gender; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public String getPicUrl(){ return picUrl; }

    public void setPicUrl(String picUrl){ this.picUrl = picUrl; }
}
