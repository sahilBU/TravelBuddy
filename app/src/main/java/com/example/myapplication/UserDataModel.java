package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDataModel {
    String name, totalLikes, bio, gender, firstName;
    ArrayList<HashMap> comAndRatings;
    int photo;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTotalLikes() {
        return totalLikes;
    }
    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }
    public int getPhoto() {
        return photo;
    }
    public void setPhoto(int photo) {
        this.photo = photo;
    }
    public String getBio() {
        return bio;
    }
    public void setBio() {
        this.bio = bio;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public ArrayList<HashMap> getComAndRatings() {
        return comAndRatings;
    }
    public void setComAndRatings(ArrayList<HashMap> comAndRatings) {
        this.comAndRatings = comAndRatings;
    }

}
