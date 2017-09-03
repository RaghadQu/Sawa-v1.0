package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.LoginWIthGoogleModel;
import com.example.zodiac.sawa.SpringModels.LoginWithFacebookModel;
import com.example.zodiac.sawa.SpringModels.SignInModel;
import com.example.zodiac.sawa.SpringModels.SignOutModel;
import com.example.zodiac.sawa.SpringModels.SignUpModel;
import com.example.zodiac.sawa.SpringModels.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Rabee on 6/26/2017.
 */

public interface AuthInterface {
    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/signIn")
    Call<UserModel> signIn(@Body SignInModel signInModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/update-password")
    Call<UserModel> updatePassword(@Body SignInModel signInModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/signUp")
    Call<UserModel> signUp(@Body SignUpModel signUpModel);

    @POST("/api/v1/user/login-with-google")
    Call<UserModel> loginWithGoogle(@Body LoginWIthGoogleModel loginWIthGoogleModel);

    @POST("/api/v1/user/login-with-facebook")
    Call<UserModel> loginWithFacebook(@Body LoginWithFacebookModel loginWithFacebookModel);

    @POST("/api/v1/user/signOut")
    Call<Integer> signOut(@Body SignOutModel signOutModel);


}
