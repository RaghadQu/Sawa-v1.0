package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.PostRequestModel;
import com.example.zodiac.sawa.SpringModels.PostResponseModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by zodiac on 11/10/2017.
 */

public interface PostInterface {

    @Multipart
    @POST("/api/v1/post/addNewPost")
    Call<PostResponseModel> addNewPost(@Part MultipartBody.Part file , @Body PostRequestModel postRequestModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/post/addNewPost")
    Call<PostResponseModel> addNewPost( @Body PostRequestModel postRequestModel);

}
