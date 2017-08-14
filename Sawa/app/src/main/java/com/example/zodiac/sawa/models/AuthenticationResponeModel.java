package com.example.zodiac.sawa.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationResponeModel {


    @SerializedName("state")
    @Expose
    private int state;


    @SerializedName("user_id")
    @Expose
    private String user_id;

    public int getState() {
        return state;
    }

    public void setStatus(int state) {
        this.state = state;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

