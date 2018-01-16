package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.SpringModels.FollowesAndFollowingResponse;
import com.example.zodiac.sawa.SpringModels.PostCommentModel;
import com.example.zodiac.sawa.SpringModels.PostRequestModel;
import com.example.zodiac.sawa.SpringModels.PostResponseModel;
import com.example.zodiac.sawa.SpringModels.ReactRequestModel;
import com.example.zodiac.sawa.SpringModels.ReactSingleModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zodiac on 11/10/2017.
 */

public interface PostInterface {
    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/post/getUserHomePost/{id}")
    Call<List<PostCommentModel>> getUserHomePost(@Path("id") int id);
    @Multipart
    @POST("/api/v1/post/addNewImagePost")
    Call<PostResponseModel> addNewPost(@Part MultipartBody.Part file ,@Query("id") int id,@Query("text") String text,@Query("is_public_comment") boolean is_public_comment);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/post/addNewPost")
    Call<PostResponseModel> addNewPost(@Body PostRequestModel postRequestModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/post/addNewReact")
    Call<Integer> addNewReact(@Body ReactRequestModel reactRequestModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/post/deleteReact")
    Call<Integer> deleteReact(@Body ReactRequestModel reactRequestModel);
    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/post/getPostReact/{postId}/{type}")
    Call<ReactSingleModel> getPostReact(@Path("postId") int postId, @Path("type") int type);


}
