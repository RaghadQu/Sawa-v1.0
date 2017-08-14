package com.example.zodiac.sawa.interfaces;

import com.example.zodiac.sawa.models.AuthenticationResponeModel;
import com.example.zodiac.sawa.models.UserImage;
import com.example.zodiac.sawa.models.userImageFromDb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Rabee on 4/13/2017.
 */

public interface UserImageApi {
    @POST("api/users/saveUserImage")
    Call<AuthenticationResponeModel> saveImageUser(@Body UserImage userImage);

    @GET("api/users/getUserImage/{user_id}")
    Call<List<userImageFromDb>> getUserImageFromDb(@Path("user_id") int user_id);
}
