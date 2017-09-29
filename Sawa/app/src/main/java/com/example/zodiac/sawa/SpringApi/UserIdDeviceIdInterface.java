package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.DeviceTokenModel;
import com.example.zodiac.sawa.SpringModels.UserDeviceIdRequestModel;
import com.example.zodiac.sawa.SpringModels.UserIdDeviceIdResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Rabee on 9/26/2017.
 */

public interface UserIdDeviceIdInterface {
    @POST("/api/v1/deviceToken/saveIdWithDeviceId")
    Call<UserIdDeviceIdResponseModel> storeUserIdWithDeviceId(@Body UserDeviceIdRequestModel userDeviceIdRequestModel);
}
