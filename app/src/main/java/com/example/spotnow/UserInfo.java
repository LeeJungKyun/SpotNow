package com.example.spotnow;

public class UserInfo
{
    public String name;
    public String email;
    public String sport;
    public String region;

    public int report_cnt;

    public String introduce_self;

    public UserInfo()
    {

    }

    public UserInfo(String name, String email, String sport, String region, int report_cnt, String introduce_self) {
        this.name = name;
        this.email = email;
        this.sport = sport;
        this.region = region;
        this.report_cnt = report_cnt;
        this.introduce_self = introduce_self;
    }

    public int getReport_cnt() {
        return report_cnt;
    }

    public void setReport_cnt(int report_cnt) {
        this.report_cnt = report_cnt;
    }

    public String getIntroduce_self() {
        return introduce_self;
    }

    public void setIntroduce_self(String introduce_self) {
        this.introduce_self = introduce_self;
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
