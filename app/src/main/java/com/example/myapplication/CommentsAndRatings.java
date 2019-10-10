package com.example.myapplication;

public class CommentsAndRatings {

    private float ratingStar;
    private String name;
    private String photoUrl;

    public CommentsAndRatings(int ratingStar, String name, String photoUrl) {
        this.ratingStar = ratingStar;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
