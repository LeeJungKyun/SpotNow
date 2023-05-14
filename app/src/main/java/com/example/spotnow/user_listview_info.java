package com.example.spotnow;

public class user_listview_info
{
    public int poster;
    public String name;
    public String introduce;

    public user_listview_info(int poster, String name, String introduce) {
        this.poster = poster;
        this.name = name;
        this.introduce = introduce;
    }

    public int getPoster() {
        return poster;
    }

    public void setPoster(int poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
