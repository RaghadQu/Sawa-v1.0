package com.example.zodiac.sawa.SpringModels;

/**
 * Created by Rabee on 6/26/2017.
 */

public class UserModel {
    int id;
    String first_name;
    String last_name;
    String email;
    String gender;
    String birthdate;
    String mobile;
    String image;
    int sign_in_out;
    int public_post_view;
    String cover_image;
    boolean isPublic;
    boolean isProfileImagePublic;
    String themeColor;


    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isProfileImagePublic() {
        return isProfileImagePublic;
    }

    public void setProfileImagePublic(boolean profileImagePublic) {
        isProfileImagePublic = profileImagePublic;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSign_in_out() {
        return sign_in_out;
    }

    public void setSign_in_out(int sign_in_out) {
        this.sign_in_out = sign_in_out;
    }

    public int getPublic_post_view() {
        return public_post_view;
    }

    public void setPublic_post_view(int public_post_view) {
        this.public_post_view = public_post_view;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}

