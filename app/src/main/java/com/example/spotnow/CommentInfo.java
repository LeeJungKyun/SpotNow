package com.example.spotnow;

public class CommentInfo {
    public String UserID;
    public String Comment;

    public CommentInfo(){

    }
    public CommentInfo(String UserID, String Comment){
        UserID = this.UserID;
        Comment = this.Comment;
    }

    public String getUserId() {
        return UserID;
    }

    public void setUserId(String userId) {
        this.UserID = userId;
    }

    public String getCommentText() {
        return Comment;
    }

    public void setCommentText(String commentText) {
        this.Comment = commentText;
    }

}
