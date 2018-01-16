package com.example.zodiac.sawa.Requests.Activities;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.SpringApi.AuthInterface;
import com.example.zodiac.sawa.SpringModels.GeneralUserInfoModel;
import com.example.zodiac.sawa.SpringModels.SignInModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rabee on 1/6/2018.
 */

public class MainActivityRequests {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GeneralAppInfo.SPRING_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    AuthInterface service= retrofit.create(AuthInterface.class);


    public void makeSignRequest(final Callback<GeneralUserInfoModel> callback) {
        final Call<GeneralUserInfoModel> userModelCall = service.signIn(new SignInModel());
        userModelCall.enqueue(new Callback<GeneralUserInfoModel>() {


            @Override
            public void onResponse(Call<GeneralUserInfoModel> call, Response<GeneralUserInfoModel> response) {
                GeneralUserInfoModel generalUserInfoModel = response.body();

                callback.onResponse(null,response);
            }

            @Override
            public void onFailure(Call<GeneralUserInfoModel> call, Throwable t) {

            }
        });
    }
    public void getData(){

    }
}
