package com.example.spotnow.activity;

public class activitySampleData {
    private int activityImage; // Resource ID of the activity image
    private String activityTitle; // Title of the activity
    private String activityAddress; // Address of the activity

    // Constructor with image, title, and address parameters
    public activitySampleData(int activityImage, String activityTitle, String activityAddress) {
        this.activityImage = activityImage;
        this.activityTitle = activityTitle;
        this.activityAddress = activityAddress;
    }

    // Getter and setter methods for the private fields

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
