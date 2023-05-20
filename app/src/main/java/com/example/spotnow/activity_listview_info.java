package com.example.spotnow;

public class activity_listview_info {
    private String activityImage;
    private String activityTitle;
    private String activityContent;

    public activity_listview_info(String activityImage, String activityTitle, String activityAddress) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityAddress;
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
}
