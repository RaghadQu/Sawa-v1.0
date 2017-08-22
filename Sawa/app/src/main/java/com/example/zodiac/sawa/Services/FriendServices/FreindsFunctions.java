package com.example.zodiac.sawa.Services.FriendServices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.zodiac.sawa.Activities.MyFriendProfileActivity;
import com.example.zodiac.sawa.Activities.MyRequestsActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.Activities.MyProfileActivity;
import com.example.zodiac.sawa.SpringModels.FriendRequestModel;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rabee on 5/5/2017.
 */

public class FreindsFunctions {
    public void getFreindShipState(int friend1_id, int friend2_id, final Button button) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FriendshipInterface friendshipApi = retrofit.create(FriendshipInterface.class);

        Call<Integer> call = friendshipApi.getFollowRelationState(friend1_id, friend2_id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer FriendshipState = response.body();
                Log.d("stateeee", "" + FriendshipState);
                if (FriendshipState == 2) {
                    button.setText("Add as freind");
                } else if (FriendshipState == 0) {
                    button.setText("Pending");
                } else if (FriendshipState == 1) {
                    button.setText("Freind");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("stateeee", " Failure ");

            }
        });
    }

    public void startMyProfile(Context mContext, String mName, int Id) {
        if (Id == GeneralAppInfo.getUserID()) {
            Intent i = new Intent(mContext, MyProfileActivity.class);
            mContext.startActivity(i);
        } else {
            Intent i = new Intent(mContext, MyFriendProfileActivity.class);
            Bundle b = new Bundle();
            b.putString("mName", mName);
            b.putInt("Id", Id);

            i.putExtras(b);
            mContext.startActivity(i);
        }
    }

    public void startFriend(Context mContext, String mName, int Id, String ImageUrl) {
        if (Id == GeneralAppInfo.getUserID()) {
            Intent i = new Intent(mContext, MyFriendProfileActivity.class);
            mContext.startActivity(i);
        } else {
            Intent i = new Intent(mContext, MyFriendProfileActivity.class);
            Bundle b = new Bundle();
            b.putString("mName", mName);
            b.putInt("Id", Id);
            b.putString("mImageURL", ImageUrl);


            i.putExtras(b);
            mContext.startActivity(i);
        }
    }


    public void DeleteFriend(int friend1_id, int friend2_id, final Button button) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FriendshipInterface FriendApi = retrofit.create(FriendshipInterface.class);

        final FriendRequestModel FriendRequest = new FriendRequestModel();
        FriendRequest.setFriend1_id(friend1_id);
        FriendRequest.setFriend2_id(friend2_id);


        final Call<Integer> deleteCall = FriendApi.deleteFollow(FriendRequest);
        deleteCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {



            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("fail to get friends ", "Failure to Get friends");

            }


        });


    }

}
