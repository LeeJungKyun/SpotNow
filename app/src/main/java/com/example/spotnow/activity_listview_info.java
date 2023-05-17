package com.example.spotnow;

public class activity_listview_info {
    private int activityImage;
    private String activityTitle;
    private String activityContent;

    public activity_listview_info(int activityImage, String activityTitle, String activityAddress) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityContent = activityAddress;
    }

    public int getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(int activityImage) {
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
