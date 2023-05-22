package com.example.spotnow;

public class activity_listview_info {
    private String activityImage;
    private String activityTitle;
    private String activityContent;

    private String activityOwner;

    public activity_listview_info(String activityImage, String activityTitle, String activityAddress, String activityOwner) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityAddress;
        this.activityOwner = activityOwner;
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
}
