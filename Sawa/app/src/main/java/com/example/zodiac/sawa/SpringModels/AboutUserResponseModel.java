package com.example.zodiac.sawa.Spring.Models;

/**
 * Created by zodiac on 06/28/2017.
 */

public class AboutUserResponseModel {
    int id;
    UserModel user;
    String userBio;
    String userStatus;
    String userSong;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserSong() {
        return userSong;
    }

    public void setUserSong(String userSong) {
        this.userSong = userSong;
    }

}


