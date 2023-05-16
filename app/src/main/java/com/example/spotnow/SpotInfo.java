package com.example.spotnow;

public class SpotInfo
{
    public double latitude;
    public double longitude;
    public String name;
    public String address;
    public long spotID;

    public SpotInfo()
    {

    }

    public SpotInfo(double latitude, double longitude, String name, String address, long spotID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
        this.spotID = spotID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getSpotID() {
        return spotID;
    }

    public void setSpotID(long spotID) {
        this.spotID = spotID;
    }
}
