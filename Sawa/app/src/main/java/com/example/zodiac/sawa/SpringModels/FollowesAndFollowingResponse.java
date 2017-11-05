package com.example.zodiac.sawa.SpringModels;

/**
 * Created by Rabee on 8/27/2017.
 */

public class FollowesAndFollowingResponse {
    UserModel user;
    int state;
    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
