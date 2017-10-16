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

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean aPublic) {
        this.isPublic = aPublic;
    }

    public boolean getIsProfileImagePublic() {
        return isProfileImagePublic;
    }

    public void setIsProfileImagePublic(boolean profileImagePublic) {
        this.isProfileImagePublic = profileImagePublic;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }


}
