package com.example.zodiac.sawa.SpringModels;

import com.example.zodiac.sawa.SpringApi.AboutUserInterface;

public class GeneralUserInfoModel {

    UserModel user;
    AboutUserResponseModel aboutUser;

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
