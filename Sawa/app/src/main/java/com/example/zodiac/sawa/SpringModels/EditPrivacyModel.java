package com.example.zodiac.sawa.SpringModels;

/**
 * Created by zodiac on 10/11/2017.
 */

public class EditPrivacyModel {
    int id ;
    boolean isPublic;
    boolean isProfileImagePublic;
    String themeColor;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        this.isPublic = aPublic;
    }

    public boolean isProfileImagePublic() {
        return isProfileImagePublic;
    }

    public void setProfileImagePublic(boolean profileImagePublic) {
        this.isProfileImagePublic = profileImagePublic;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }


}
