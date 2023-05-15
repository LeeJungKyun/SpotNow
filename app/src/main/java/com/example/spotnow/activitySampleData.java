package com.example.spotnow;

public class activitySampleData {
    private int activityImage;
    private String activityTitle;
    private String activityAddress;

    public activitySampleData(int activityImage, String activityTitle, String activityAddress) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityAddress = activityAddress;
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

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
    }
}
