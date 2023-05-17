package com.example.spotnow;

public class ActivityInfo {
    public String title;
    public String sport;
    public String content;
    public String startTime;
    public String endTime;
    public String peopleCnt;

    public String activityOwner;

    public long spotID;


    public ActivityInfo() {

    }

    public ActivityInfo(String title, String sport, String content, String startTime, String endTime, String peopleCnt, long spotID, String activityOwner) {
        this.title = title;
        this.sport = sport;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.peopleCnt = peopleCnt;
        this.spotID = spotID;
        this.activityOwner = activityOwner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPeopleCnt() {
        return peopleCnt;
    }

    public void setPeopleCnt(String peopleCnt) {
        this.peopleCnt = peopleCnt;
    }

    public long getSpotID() {
        return spotID;
    }

    public void setSpotID(int spotID) {
        this.spotID = spotID;
    }

    public String getActivityOwner() {
        return activityOwner;
    }

    public void setActivityOwner(String activityOwner) {
        this.activityOwner = activityOwner;
    }

}
