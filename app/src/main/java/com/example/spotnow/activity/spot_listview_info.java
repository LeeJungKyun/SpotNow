package com.example.spotnow.activity;

public class spot_listview_info {
    public int image; // Image resource ID for the spot
    public String name; // Name of the spot
    public String address; // Address of the spot

    public spot_listview_info(int image, String name, String address) {
        this.image = image;
        this.name = name;
        this.address = address;
    }

    public int getImage() {
        return image; // Get the image resource ID of the spot
    }

    public void setImage(int image) {
        this.image = image; // Set the image resource ID of the spot
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
}
