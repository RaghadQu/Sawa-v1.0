package com.example.zodiac.sawa.SpringApi;

import com.example.zodiac.sawa.Spring.Models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Rabee on 6/27/2017.
 */

public interface SearchInterface {
    @GET("/api/v1/serach/{word}")
    Call<List<UserModel>> getSearchResult(@Path("word") String word);
}
