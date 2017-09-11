package com.example.zodiac.sawa.SpringModels;

/**
 * Created by Rabee on 8/27/2017.
 */

public class FollowesAndFollowingResponse {
    UserModel user;
    int friend1_state;
    int friend2_state;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getFriend1State() {
        return friend1_state;
    }

    public void setFriend2State(int state) {
        this.friend2_state = state;
    }

    public int getFriend2State() {
        return friend2_state;
    }

    public void setFriend1State(int state) {
        this.friend1_state = state;
    }
}
