package com.example.spotnow;

public class UserInfo
{
    public String name;
    public String email;
    public String sport;
    public String region;

    public UserInfo()
    {

    }

    public UserInfo(String name, String email, String sport, String region) {
        this.name = name;
        this.email = email;
        this.sport = sport;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
