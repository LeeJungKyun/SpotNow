package com.example.spotnow;
public class CommentInfo {
    public String userid;
    public String comment;

    public CommentInfo() {

    }

    public CommentInfo(String userid, String comment) {
        this.userid = userid;
        this.comment = comment;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
