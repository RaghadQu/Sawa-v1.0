package com.example.zodiac.sawa.FriendProfile;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Spring.Models.FriendRequestModel;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rabee on 5/12/2017.
 */

public class FriendsClass {

    Dialog ConfirmDeletion;
    Button NoBtn;
    Button YesBtn;
    FreindsFunctions friendFunction;
    TextView textMsg;

    public void SetFriendButtn(final Button friendStatus, final RecyclerView recyclerView, final Context context, final int Id, Context c) {
        recyclerView.setVisibility(View.VISIBLE);
        if (GeneralAppInfo.friendMode == 0)
            friendStatus.setText("Pending");
        if (GeneralAppInfo.friendMode == 1)
            friendStatus.setText("Friend");
        if (GeneralAppInfo.friendMode == 3)
            friendStatus.setText("Follow");


        friendFunction = new FreindsFunctions();
        ConfirmDeletion = new Dialog(context);
        ConfirmDeletion.setContentView(R.layout.confirm_delete_friend_or_request_dialog);
        NoBtn = (Button) ConfirmDeletion.findViewById(R.id.NoBtn);
        YesBtn = (Button) ConfirmDeletion.findViewById(R.id.YesBtn);

        textMsg = (TextView) ConfirmDeletion.findViewById(R.id.TextMsg);

        friendStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Pending state
                if (GeneralAppInfo.friendMode == 0) {
                    textMsg.setText("Are you sure you want to delete this request ?");
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

                            GeneralAppInfo.friendMode = 3;
                            friendStatus.setText("Follow");
                            ConfirmDeletion.dismiss();
                            friendFunction.DeleteFriend(GeneralAppInfo.getUserID(), Id, friendStatus);


                        }
                    });

                }
                //Friend state
                else if (GeneralAppInfo.friendMode == 1) {
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
                            GeneralAppInfo.friendMode = 2;
                            recyclerView.setVisibility(View.GONE);
                            friendStatus.setText("Follow");
                            ConfirmDeletion.dismiss();
                            friendFunction.DeleteFriend(GeneralAppInfo.getUserID(), Id, friendStatus);
                        }
                    });
                }
                //not friend state
                else if (GeneralAppInfo.friendMode == 3) {
                    GeneralAppInfo.friendMode = 0;
                    friendStatus.setText("Pending");
                    NotFriendProfileClass notFriendProfileClass = new NotFriendProfileClass();
                    notFriendProfileClass.addNewFriendShip(GeneralAppInfo.getUserID(), Id);
                }
            }
        });
    }

    public void setFriendRequestButton(final Button friendStatus, final Button ConfirmRequest, final Button DeleteRequest, final int Id) {

        friendStatus.setVisibility(View.INVISIBLE);
        ConfirmRequest.setVisibility(View.VISIBLE);
        DeleteRequest.setVisibility(View.VISIBLE);

        friendFunction = new FreindsFunctions();

        DeleteRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // recyclerView.setVisibility(View.GONE);
                friendStatus.setVisibility(View.VISIBLE);
                ConfirmRequest.setVisibility(View.INVISIBLE);
                DeleteRequest.setVisibility(View.INVISIBLE);
                GeneralAppInfo.friendMode = 2;
                friendStatus.setText("Follow");
                friendFunction.DeleteFriend(GeneralAppInfo.getUserID(), Id, friendStatus);
            }
        });

        ConfirmRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // recyclerView.setVisibility(View.GONE);
                friendStatus.setVisibility(View.VISIBLE);
                ConfirmRequest.setVisibility(View.INVISIBLE);
                DeleteRequest.setVisibility(View.INVISIBLE);
                GeneralAppInfo.friendMode = 1;
                friendStatus.setText("Friend");


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GeneralAppInfo.SPRING_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                FriendshipInterface service_confirm = retrofit.create(FriendshipInterface.class);

                FriendRequestModel friendshipModel = new FriendRequestModel();
                friendshipModel.setFriend1_id(GeneralAppInfo.getUserID());
                friendshipModel.setFriend2_id(Id);

                final Call<Integer> confirmCall = service_confirm.confirmFriendship(friendshipModel);
                confirmCall.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        // Integer ConfirmFriendshipResponse = response.body();
                        Log.d("Confirm FriendShip", " state is " + response.code());
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d("fail to get friends ", "Failure to Get friends");

                    }


                });

            }

        });


    }
}

