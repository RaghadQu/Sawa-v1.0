package com.example.zodiac.sawa.Services.FriendServices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.SpringModels.FriendRequestModel;
import com.example.zodiac.sawa.SpringModels.FriendResponseModel;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rabee on 5/12/2017.
 */

public class NotFriendProfileClass {
    public void SetFriendButtn(final Button friendStatus, RecyclerView recyclerView, final int Id, Context c) {
        //recyclerView.setVisibility(View.GONE);
        friendStatus.setText("Add as friend");
        friendStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (view == friendStatus && friendStatus.getText().equals("Add")) {
                    friendStatus.setText("Pending");


                    addNewFriendShip(GeneralAppInfo.getUserID(), Id);

                } else
                    Log.d("Add friend ship", "Already sent");


            }
        });
    }


    public void addNewFriendShip(int friend1_id, int friend2_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FriendshipInterface FriendApi = retrofit.create(FriendshipInterface.class);

        FriendRequestModel friendshipModel = new FriendRequestModel();
        friendshipModel.setFriend1_id(friend1_id);
        friendshipModel.setFriend2_id(friend2_id);


        Call<FriendResponseModel> addFrienshipCall = FriendApi.sendFollowRequest(friendshipModel);
        addFrienshipCall.enqueue(new Callback<FriendResponseModel>() {
            @Override
            public void onResponse(Call<FriendResponseModel> call, Response<FriendResponseModel> response) {
                FriendResponseModel FriendshipResponse = response.body();
                Log.d("AddFriendShip", "" + FriendshipResponse + " " + response.code());

            }

            @Override
            public void onFailure(Call<FriendResponseModel> call, Throwable t) {
                Log.d("stateeee", " Failure ");

            }
        });

    }
}
