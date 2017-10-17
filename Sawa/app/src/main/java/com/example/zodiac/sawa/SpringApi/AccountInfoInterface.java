package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.AboutUserRequestModel;
import com.example.zodiac.sawa.SpringModels.AboutUserResponseModel;
import com.example.zodiac.sawa.SpringModels.EditEmailModel;
import com.example.zodiac.sawa.SpringModels.EditMobileModel;
import com.example.zodiac.sawa.SpringModels.EditPasswordModel;
import com.example.zodiac.sawa.SpringModels.EditPrivacyModel;
import com.example.zodiac.sawa.SpringModels.GeneralUserInfoModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by zodiac on 10/09/2017.
 */

public interface AccountInfoInterface {
    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-email")
    Call<GeneralUserInfoModel> editEmail(@Body EditEmailModel editEmailModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-password")
    Call<GeneralUserInfoModel> editPassword(@Body EditPasswordModel editPasswordModel);


    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-mobile")
    Call<GeneralUserInfoModel> editMobile(@Body EditMobileModel editMobileModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-privacy")
    Call<GeneralUserInfoModel> editPrivacy(@Body EditPrivacyModel editPrivacyModel);

}

