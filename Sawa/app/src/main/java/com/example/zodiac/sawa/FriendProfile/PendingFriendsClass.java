package com.example.zodiac.sawa.FriendProfile;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.R;

/**
 * Created by Rabee on 5/12/2017.
 */

public class PendingFriendsClass {
    Dialog ConfirmDeletion;
    Button NoBtn;
    Button YesBtn;
    TextView textMsg;
    FreindsFunctions friendFunction;


    public void SetFriendButtn(final Button friendStatus, Context context, final int Id) {
        friendStatus.setText("Pending");

        friendFunction = new FreindsFunctions();
        ConfirmDeletion = new Dialog(context);
        ConfirmDeletion.setContentView(R.layout.confirm_delete_friend_or_request_dialog);
        NoBtn = (Button) ConfirmDeletion.findViewById(R.id.NoBtn);
        YesBtn = (Button) ConfirmDeletion.findViewById(R.id.YesBtn);
        textMsg = (TextView) ConfirmDeletion.findViewById(R.id.TextMsg);
        textMsg.setText("Are you sure you want to delete this request ?");
        friendStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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
                        ConfirmDeletion.dismiss();
                        friendFunction.DeleteFriend(GeneralAppInfo.getUserID(), Id, friendStatus);


                    }
                });
            }
        });
    }
}
