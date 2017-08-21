package com.example.zodiac.sawa.SpringModels;

import java.util.Date;

/**
 * Created by zodiac on 07/01/2017.
 */

public class PostModel {

    int postId;
    UserModel userId;
    UserModel toUserId;
    String text;
    String image;
    int isAnon;
    private Date timestamp;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public UserModel getUserId() {
        return userId;
    }

    public void setUserId(UserModel userId) {
        this.userId = userId;
    }

    public UserModel getToUserId() {
        return toUserId;
    }

    public void setToUserId(UserModel toUserId) {
        this.toUserId = toUserId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsAnon() {
        return isAnon;
    }

    public void setIsAnon(int isAnon) {
        this.isAnon = isAnon;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
