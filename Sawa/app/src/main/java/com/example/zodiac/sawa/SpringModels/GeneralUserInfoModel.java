package com.example.zodiac.sawa.SpringModels;

import com.example.zodiac.sawa.SpringApi.AboutUserInterface;

public class GeneralUserInfoModel {

    UserModel user;
    AboutUserResponseModel aboutUser;
    int numberOfFollower ;
    int numberOfFollowing;

    public int getNumberOfFollower() {
        return numberOfFollower;
    }

    public void setNumberOfFollower(int numberOfFollower) {
        this.numberOfFollower = numberOfFollower;
    }

    public int getNumberOfFollowing() {
        return numberOfFollowing;
    }

    public void setNumberOfFollowing(int numberOfFollowing) {
        this.numberOfFollowing = numberOfFollowing;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public AboutUserResponseModel getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(AboutUserResponseModel aboutUser) {
        this.aboutUser = aboutUser;
    }

}
