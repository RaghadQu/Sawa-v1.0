package com.example.zodiac.sawa.SpringModels;

/**
 * Created by zodiac on 11/10/2017.
 */

public class PostResponseModel {

    int postId;
    UserModel userId;
    String text;
    String image;
    String link;
    boolean isPublic_comment;
    YoutubeLinkModel youtubeLink;

    public void setYoutubelink(YoutubeLinkModel youtubelink) {
        this.youtubeLink = youtubelink;
    }
    public YoutubeLinkModel getYoutubelink() {
        return youtubeLink;
    }
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

    public boolean is_public_comment() {
        return isPublic_comment;
    }

    public void setIs_public_comment(boolean is_public_comment) {
        this.isPublic_comment = is_public_comment;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
