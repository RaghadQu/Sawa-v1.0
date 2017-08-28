package com.example.zodiac.sawa.Services.FriendServices;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringModels.FriendRequestModel;
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

    public void setFollowRelationState(final Button friendStatus, final Context context, final int Id, Context c) {
       // recyclerView.setVisibility(View.VISIBLE);
        if (GeneralAppInfo.friendMode == 0)
            friendStatus.setText("Follow");
        if (GeneralAppInfo.friendMode == 1)
            friendStatus.setText("Pending");
        if (GeneralAppInfo.friendMode == 2)
            friendStatus.setText("Following");


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
                if (GeneralAppInfo.friendMode == 1) {
                    textMsg.setText("Are you sure you want to delete the follow request ?");
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
                            friendFunction.DeleteFriend(GeneralAppInfo.getUserID(), Id, friendStatus);


                        }
                    });

                }
                //Friend state
                else if (GeneralAppInfo.friendMode == 2) {
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
                            GeneralAppInfo.friendMode = 0;
                    //        recyclerView.setVisibility(View.GONE);
                            friendStatus.setText("Follow");
                            ConfirmDeletion.dismiss();
                            friendFunction.DeleteFriend(GeneralAppInfo.getUserID(), Id, friendStatus);
                        }
                    });
                }
                //not friend state
                else if (GeneralAppInfo.friendMode == 0) {
                    GeneralAppInfo.friendMode = 1;
                    friendStatus.setText("Pending");
                    NotFriendProfileClass notFriendProfileClass = new NotFriendProfileClass();
                    notFriendProfileClass.addNewFriendShip(GeneralAppInfo.getUserID(), Id);
                }
            }
        });
    }


}

