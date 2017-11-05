package com.example.zodiac.sawa.Services.FriendServices;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zodiac.sawa.Activities.OtherProfileActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringModels.FriendRequestModel;
import com.example.zodiac.sawa.SpringModels.FriendResponseModel;
import com.example.zodiac.sawa.SpringModels.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Rabee on 5/5/2017.
 */

public class FollowFunctions {

    static Dialog ConfirmDeletion;
    static Button NoBtn;
    static Button YesBtn;
    static FollowFunctions friendFunction;
    static TextView textMsg;

    public void startFriend(Context mContext, String mName, int Id, String ImageUrl) {
        if (Id == GeneralAppInfo.getUserID()) {
            Intent i = new Intent(mContext, OtherProfileActivity.class);
            mContext.startActivity(i);
        } else {
            Intent i = new Intent(mContext, OtherProfileActivity.class);
            Bundle b = new Bundle();
            b.putString("mName", mName);
            b.putInt("Id", Id);
            b.putString("mImageURL", ImageUrl);


            i.putExtras(b);
            mContext.startActivity(i);
        }
    }


    public static void DeleteFriend(int friend1_id, int friend2_id) {

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
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("fail to get friends ", "Failure to Get friends");

            }


        });


    }

    public static void DeleteFriendwithBtn(int friend1_id, int friend2_id , final Button FollowState, final Button myFollowState, final ImageView otherFollowState) {

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
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }else
                {
                    FollowState.setVisibility(View.VISIBLE);
                    FollowState.setText(myFollowState.getText());
                    myFollowState.setVisibility(View.INVISIBLE);
                    otherFollowState.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("fail to get friends ", "Failure to Get friends");

            }


        });


    }
    public static void ConfirmFollowRequest(int friend1_id, int friend2_id , final ImageView otherFollowState){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FriendshipInterface service_confirm = retrofit.create(FriendshipInterface.class);

            FriendRequestModel friendshipModel = new FriendRequestModel();
            friendshipModel.setFriend1_id(friend1_id);
            friendshipModel.setFriend2_id(friend2_id);

            final Call<Integer> confirmCall = service_confirm.confirmFriendship(friendshipModel);
            confirmCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                    }
                    else
                    {
                        if(otherFollowState !=null){
                            otherFollowState.setImageResource(R.drawable.follower_icon);
                        }
                    }

                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.d("Failuer" , t.getMessage());

                }


            });
        }




    public  static  void addNewFriendShip(int friend1_id, int friend2_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FriendshipInterface FriendApi = retrofit.create(FriendshipInterface.class);

        FriendRequestModel friendshipModel = new FriendRequestModel();
        friendshipModel.setFriend1_id(friend1_id);
        friendshipModel.setFriend2_id(friend2_id);


        Call<FriendResponseModel> addFrienshipCall = FriendApi.sendNewFollow(friendshipModel);
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

    public static void setFollowRelationState(final Button friendStatus, final Context context, final UserModel userModel, Context c) {
        // recyclerView.setVisibility(View.VISIBLE);
        final int Id=userModel.getId();
        Log.d("")
        if (GeneralAppInfo.friendMode == 0)
            friendStatus.setText("Follow");

        if (GeneralAppInfo.friendMode == 1)
            friendStatus.setText("Following");


        friendFunction = new FollowFunctions();
        ConfirmDeletion = new Dialog(context);
        ConfirmDeletion.setContentView(R.layout.confirm_delete_friend_or_request_dialog);
        NoBtn = (Button) ConfirmDeletion.findViewById(R.id.NoBtn);
        YesBtn = (Button) ConfirmDeletion.findViewById(R.id.YesBtn);

        textMsg = (TextView) ConfirmDeletion.findViewById(R.id.TextMsg);

        friendStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Pending state
                if (GeneralAppInfo.friendMode == 1) {
                    textMsg.setText("Are you sure you want to delete the follow ?");
                    ConfirmDeletion.show();
                    NoBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            ConfirmDeletion.dismiss();
                        }
                    });
                    YesBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // recyclerView.setVisibility(View.GONE);
                            GeneralAppInfo.friendMode = 0;
                            friendStatus.setText("Follow");
                            ConfirmDeletion.dismiss();
                            friendFunction.DeleteFriend(Id, GeneralAppInfo.getUserID());
                        }
                    });
                }
                //Friend state

                //not friend state
                else if (GeneralAppInfo.friendMode == 0) {
                    Log.d("OtherActivityProfile","Requested");

                        GeneralAppInfo.friendMode = 1;
                        friendStatus.setText("Following");
                        FollowFunctions.addNewFriendShip(GeneralAppInfo.getUserID(), Id);
                }
            }
        });
    }
}
