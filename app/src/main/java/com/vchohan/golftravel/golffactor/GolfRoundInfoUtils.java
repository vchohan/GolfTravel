package com.vchohan.golftravel.golffactor;

public class GolfRoundInfoUtils {

    private String image;

    private String title;

    private String username;

    public GolfRoundInfoUtils() {

    }

    public GolfRoundInfoUtils(String Image, String Title, String Username) {
        this.image = Image;
        this.title = Title;
        this.username = Username;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
