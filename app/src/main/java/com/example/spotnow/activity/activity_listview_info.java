package com.example.spotnow.activity;

public class activity_listview_info {


    private String activityImage;
    private String activityTitle;
    private String activityContent;
    private String activityOwner;
    private String activitySport;
    private String peopleCnt;
    private String startTime;


    private String endTime;

    public activity_listview_info(String activityImage, String activityTitle, String activityAddress, String activityOwner) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityAddress;
        this.activityOwner = activityOwner;
    }

    public activity_listview_info(String activityImage, String activityTitle, String activityContent, String activityOwner, String activitySport) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityContent;
        this.activityOwner = activityOwner;
        this.activitySport = activitySport;
    }

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
