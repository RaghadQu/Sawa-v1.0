package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.DeviceTokenModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Rabee on 6/28/2017.
 */

public interface DeviceTokenInterface {
    @POST("api/token/fcm")
    Call<DeviceTokenModel> storeDeviceToken(@Body DeviceTokenModel deviceTokenModel);
}
