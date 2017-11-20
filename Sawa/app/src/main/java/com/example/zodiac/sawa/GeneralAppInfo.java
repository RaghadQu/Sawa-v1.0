package com.example.zodiac.sawa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zodiac.sawa.SpringModels.GeneralUserInfoModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Rabee on 4/8/2017.
 */

public class GeneralAppInfo  {
    public static String BACKEND_URL = "http://cb2d8852.ngrok.io/Sawa/public/index.php/";
    public static String IMAGE_URL = "http://cb2d8852.ngrok.io/Sawa/public/";

    public static String SPRING_URL = "http://4d2690cd.ngrok.io";
    public static int notifications_counter = 0;
    public static int home_tab_position = 0;
    public static int notifications_tab_position = 1;
    public static int setting_tab_position = 2;
    public static int friendMode = -1;
    public static int userID;
    public static GeneralUserInfoModel generalUserInfo;
    public static Bitmap userProfilePicture;


    public static Bitmap getUserProfilePicture() {
        return userProfilePicture;
    }

    public static void setUserProfilePicture(Bitmap userProfilePicture) {
        GeneralAppInfo.userProfilePicture = userProfilePicture;
    }

    public static GeneralUserInfoModel getGeneralUserInfo() {
        return generalUserInfo;
    }

    public static void setGeneralUserInfo(GeneralUserInfoModel generalUserInfo) {
        GeneralAppInfo.generalUserInfo = generalUserInfo;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        GeneralAppInfo.userID = userID;
    }

    public static Bitmap StringToBitMap(String encodedString) {

        URL url = null;
        try {
            url = new URL(encodedString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

}


