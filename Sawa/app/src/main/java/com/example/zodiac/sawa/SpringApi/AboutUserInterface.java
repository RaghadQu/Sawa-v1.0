package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.AboutUserRequestModel;
import com.example.zodiac.sawa.SpringModels.AboutUserResponseModel;
import com.example.zodiac.sawa.SpringModels.EditProfileModel;
import com.example.zodiac.sawa.SpringModels.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by zodiac on 06/28/2017.
 */

public interface AboutUserInterface {
    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/addAboutUser")
    Call<AboutUserResponseModel> addNewAboutUser(@Body AboutUserRequestModel aboutUserModel);
    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/editUserSong")
    Call<AboutUserResponseModel> editUserSong(@Body AboutUserRequestModel aboutUserModel);
    @GET("/api/v1/user/getAbout/{id}")
    Call<AboutUserResponseModel> getAboutUser(@Path("id") int id);

    @GET("/api/v1/user/getUser/{id}")
    Call<UserModel> getUserInfo(@Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/editProfile")
    Call<Integer> updateProfile(@Body EditProfileModel editUserModel);

}
