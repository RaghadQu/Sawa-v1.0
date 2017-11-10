package com.example.zodiac.sawa.SpringModels;

import java.util.Date;

/**
 * Created by zodiac on 07/01/2017.
 */

public class PostRequestModel {

    int userId;
    String text;
    boolean is_public_comment;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean is_public_comment() {
        return is_public_comment;
    }

    public void setIs_public_comment(boolean is_public_comment) {
        this.is_public_comment = is_public_comment;
    }




}
