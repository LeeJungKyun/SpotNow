package com.example.spotnow.activity;

public class activity_listview_info {

    private String activityImage; // Image URL of the activity
    private String activityTitle; // Title of the activity
    private String activityContent; // Address or content of the activity
    private String activityOwner; // Owner of the activity
    private String activitySport; // Sport associated with the activity
    private String peopleCnt; // Number of people participating in the activity
    private String startTime; // Start time of the activity
    private String endTime; // End time of the activity

    // Constructor with image, title, content, and owner parameters
    public activity_listview_info(String activityImage, String activityTitle, String activityAddress, String activityOwner) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityAddress;
        this.activityOwner = activityOwner;
    }

    // Constructor with additional sport parameter
    public activity_listview_info(String activityImage, String activityTitle, String activityContent, String activityOwner, String activitySport) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityContent;
        this.activityOwner = activityOwner;
        this.activitySport = activitySport;
    }

    // Constructor with additional parameters for people count, start time, and end time
    public activity_listview_info(String activityImage, String activityTitle, String activityContent, String activityOwner, String activitySport, String peopleCnt, String startTime, String endTime) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityContent;
        this.activityOwner = activityOwner;
        this.activitySport = activitySport;
        this.peopleCnt = peopleCnt;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter and setter methods for the private fields

    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getActivityOwner() {
        return activityOwner;
    }

    public void setActivityOwner(String activityOwner) {
        this.activityOwner = activityOwner;
    }

    public String getActivitySport() {
        return activitySport;
    }

    public void setActivitySport(String activitySport) {
        this.activitySport = activitySport;
    }

    public String getPeopleCnt() {
        return peopleCnt;
    }

    public void setPeopleCnt(String peopleCnt) {
        this.peopleCnt = peopleCnt;
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
}
