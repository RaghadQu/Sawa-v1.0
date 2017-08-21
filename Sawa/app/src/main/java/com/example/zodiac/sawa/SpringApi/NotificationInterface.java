package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.NotificationModel;
import com.example.zodiac.sawa.SpringModels.updateNotificationModel;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationInterface {

    @GET("/api/v1/notification/getNotification/{user_id}")
    Call<NotificationModel> getNotification(@Path("user_id") int user_id);

    @FormUrlEncoded
    @POST("/api/v1/notification/updateReadFlag")
    Call<Integer> updateReadFlag(updateNotificationModel notification);
}
