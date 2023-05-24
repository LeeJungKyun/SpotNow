package com.example.spotnow.activity;

public class CommentInfo {
    private String userID;
    private String comment;
    private long timestamp;

    public CommentInfo() {
    }

    public CommentInfo(String userID, String comment, long timestamp) {
        this.userID = userID;
        this.comment = comment;
        this.timestamp = timestamp;
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

    public long getTimestamp(){return timestamp;}

    public void setTimestamp(long timestamp){this.timestamp = timestamp;}
}