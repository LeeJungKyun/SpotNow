package com.example.spotnow.common;

public class MarkerInfo {
    public Coordinate location; // Holds the coordinates of the marker's location
    public String spotName; // Holds the name of the spot associated with the marker
    public String address; // Holds the address of the spot associated with the marker
    public long spotID; // Holds the ID of the spot associated with the marker

    public MarkerInfo(String spotName) {
        this.spotName = spotName; // Initialize the spotName property with the provided value
    }
}
