package com.example.zodiac.sawa;

/**
 * Created by Rabee on 4/8/2017.
 */

public class GeneralAppInfo {
    public static String BACKEND_URL = "http://cb2d8852.ngrok.io/Sawa/public/index.php/";
    public static String IMAGE_URL = "http://cb2d8852.ngrok.io/Sawa/public/";
    //   http://a2203e01.ngrok.io/_437.022828770496916832227_1341475782539973_288842465026282085_n.jpg
    public static String SPRING_URL = "http://290799cb.ngrok.io";
    public static int notifications_counter = 0;
    public static int home_tab_position = 0;
    public static int notifications_tab_position = 1;
    public static int setting_tab_position = 2;
    public static int friendMode = -1;
    public static int userID;

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        GeneralAppInfo.userID = userID;
    }

}
