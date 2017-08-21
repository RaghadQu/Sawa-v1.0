package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.FriendRequestModel;
import com.example.zodiac.sawa.SpringModels.FriendResponseModel;
import com.example.zodiac.sawa.SpringModels.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by zodiac on 06/29/2017.
 */


public interface FriendshipInterface {


    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/getFollowers/{id}")
    Call<List<UserModel>> getFollowers(@Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/getFollowing/{id}")
    Call<List<UserModel>> getFollowing(@Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/getFollowRequest/{id}")
    Call<List<UserModel>> getFollowRequest(@Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/friends/getFriendShipState/{friend1_id}/{friend2_id}")
    Call<Integer> getFriendShipState(@Path("friend1_id") int friend1_id, @Path("friend2_id") int friend2_id);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/friend/sendFollowRequest")
    Call<FriendResponseModel> sendFollowRequest(@Body FriendRequestModel friendshipModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/friend/deleteFriendship")
    Call<Integer> deleteFriendship(@Body FriendRequestModel friendshipModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/friend/confirmFollowRequest")
    Call<Integer> confirmFriendship(@Body FriendRequestModel friendshipModel);


}