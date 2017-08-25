package com.example.zodiac.sawa.SpringModels;

/**
 * Created by zodiac on 06/29/2017.
 */

public class FriendResponseModel {

    int id;
    UserModel friend1_id;
    UserModel friend2_id;
    int friend1_state;
    int friend2_state;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getFriend1_state() {
        return friend1_state;
    }

    public void setFriend1_state(int friend1_state) {
        this.friend1_state = friend1_state;
    }

    public int getFriend2_state() {
        return friend2_state;
    }

    public void setFriend2_state(int friend2_state) {
        this.friend2_state = friend2_state;
    }
}
