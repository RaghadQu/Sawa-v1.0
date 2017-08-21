package com.example.zodiac.sawa.Services.FirebaseServices;


import android.provider.Settings;
import android.util.Log;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.SpringModels.DeviceTokenModel;
import com.example.zodiac.sawa.SpringApi.DeviceTokenInterface;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by Rabee on 5/13/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        storeToken(refreshedToken);
    }

    public void storeToken(String token) {
        Log.d("Arrive", token);
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DeviceTokenModel deviceTokenModel = new DeviceTokenModel();
        deviceTokenModel.setDeviceId(deviceId);
        deviceTokenModel.setToken(token);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeviceTokenInterface deviceTokenInterface = retrofit.create(DeviceTokenInterface.class);
        Call<DeviceTokenModel> call = deviceTokenInterface.storeDeviceToken(deviceTokenModel);
        call.enqueue(new Callback<DeviceTokenModel>() {


            @Override
            public void onResponse(Call<DeviceTokenModel> call, Response<DeviceTokenModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
//                Log.d("Arrive",""+response.body().getDeviceId());

            }

            @Override
            public void onFailure(Call<DeviceTokenModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }
        });
    }
}
