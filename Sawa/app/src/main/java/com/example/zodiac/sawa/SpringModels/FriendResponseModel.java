package com.example.zodiac.sawa.SpringModels;

/**
 * Created by zodiac on 06/29/2017.
 */

public class FriendResponseModel {

    int id;
    UserModel friend1_id;
    UserModel friend2_id;
    int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public UserModel getFriend1_id() {
        return friend1_id;
    }

    public void setFriend1_id(UserModel friend1_id) {
        this.friend1_id = friend1_id;
    }

    public UserModel getFriend2_id() {
        return friend2_id;
    }

    public void setFriend2_id(UserModel friend2_id) {
        this.friend2_id = friend2_id;
    }
}
