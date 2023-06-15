package com.example.spotnow.activity;

public class CommentInfo {
    private String userName; // User name associated with the comment
    private String comment; // The actual comment content
    private long timestamp; // Timestamp of the comment

    public CommentInfo() {
        // Empty constructor required for Firebase
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
