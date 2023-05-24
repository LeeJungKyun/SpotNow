package com.example.spotnow.activity;

public class CommentInfo {
    private String userName;
    private String comment;
    private long timestamp;

    public CommentInfo() {
    }

    public CommentInfo(String userName, String comment, long timestamp) {
        this.userName = userName;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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