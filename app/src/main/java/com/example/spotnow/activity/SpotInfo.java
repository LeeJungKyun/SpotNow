package com.example.spotnow.activity;

public class SpotInfo {
    public double latitude; // Latitude of the spot
    public double longitude; // Longitude of the spot
    public String name; // Name of the spot
    public String address; // Address of the spot
    public long spotID; // ID of the spot

    public SpotInfo() {} // Default constructor

    public SpotInfo(double latitude, double longitude, String name, String address, long spotID) {
        this.latitude = latitude; // Initialize the latitude of the spot
        this.longitude = longitude; // Initialize the longitude of the spot
        this.name = name; // Initialize the name of the spot
        this.address = address; // Initialize the address of the spot
        this.spotID = spotID; // Initialize the ID of the spot
    }

    public double getLatitude() {
        return latitude; // Get the latitude of the spot
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude; // Set the latitude of the spot
    }

    public double getLongitude() {
        return longitude; // Get the longitude of the spot
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude; // Set the longitude of the spot
    }

    public String getName() {
        return name; // Get the name of the spot
    }

    public void setName(String name) {
        this.name = name; // Set the name of the spot
    }

    public String getAddress() {
        return address; // Get the address of the spot
    }

    public void setAddress(String address) {
        this.address = address; // Set the address of the spot
    }

    public long getSpotID() {
        return spotID; // Get the ID of the spot
    }

    public void setSpotID(long spotID) {
        this.spotID = spotID; // Set the ID of the spot
    }
}
