package com.example.zodiac.sawa.models;

/**
 * Created by Rabee on 4/13/2017.
 */

public class UserImage {
    int user_id;
    String user_image;

    public UserImage(int user_id, String user_image) {
        this.user_id = user_id;
        this.user_image = user_image;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
