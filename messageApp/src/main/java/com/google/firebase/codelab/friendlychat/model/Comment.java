package com.google.firebase.codelab.friendlychat.model;

public class Comment {
    private String commenter_name;
    private String commenter_photoUrl;
    private String comment_text;
    private float comment_rate;

    public float getComment_rate() {
        return comment_rate;
    }

    public void setComment_rate(float comment_rate) {
        this.comment_rate = comment_rate;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getCommenter_name() {
        return commenter_name;
    }

    public void setCommenter_name(String commenter_name) {
        this.commenter_name = commenter_name;
    }

    public String getCommenter_photoUrl() {
        return commenter_photoUrl;
    }

    public void setCommenter_photoUrl(String commenter_photoUrl) {
        this.commenter_photoUrl = commenter_photoUrl;
    }

    public Comment(){

    }
}
