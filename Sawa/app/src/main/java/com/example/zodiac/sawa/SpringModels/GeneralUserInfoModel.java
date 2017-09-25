package com.example.zodiac.sawa.SpringModels;

/**
 * Created by exalt on 9/24/2017.
 */

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
