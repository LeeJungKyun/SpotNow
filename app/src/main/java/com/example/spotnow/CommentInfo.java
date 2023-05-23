package com.example.spotnow;

public class CommentInfo {
    private String userID;
    private String comment;

    public CommentInfo() {
    }

    public CommentInfo(String userID, String comment) {
        this.userID = userID;
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}