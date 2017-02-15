package com.vchohan.golftravel.golffactor;

public class GolfRoundInfoUtils {

    private String image;

    private String name;

    private String date;

    private String time;

    private String username;

    public GolfRoundInfoUtils() {

    }

    public GolfRoundInfoUtils(String Image, String name, String Username) {
        this.image = Image;
        this.name = name;
        this.username = Username;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
