package com.example.zodiac.sawa.Activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Services.FriendServices.FollowFunctions;
import com.example.zodiac.sawa.SpringApi.AboutUserInterface;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringModels.AboutUserResponseModel;
import com.example.zodiac.sawa.SpringModels.FriendResponseModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtherProfileActivity extends AppCompatActivity {
    private static final int SELECTED_PICTURE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static ObjectAnimator anim;
    public static ObjectAnimator anim_button;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    CircleImageView img;
    Dialog ViewImgDialog;
    Dialog AboutFriendDialog;
    TextView about_status, about_bio, about_song;
    TextView user_profile_name;
    TextView toolBarText;
    TextView aboutUsername;
    TextView following, followers;
    ImageView imageView; // View image in dialog
    Button friendStatus;
    TextView editBio;
    ImageView aboutFriendIcon;
    ImageView coverPhoto;
    int Id1;
    String SongUrl;
    private ProgressBar progressBar;
    private ProgressBar progressBar_button;

    Dialog ConfirmDeletion;
    Button NoBtn;
    Button YesBtn;
    TextView textMsg;
    CircleImageView showOtherSong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend_profile);
        toolBarText = (TextView) findViewById(R.id.ToolbarText);
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        following = (TextView) findViewById(R.id.followingTxt);
        followers = (TextView) findViewById(R.id.followerTxt);
        showOtherSong=(CircleImageView)findViewById(R.id.showSong) ;


        //get parameters
        Bundle b = getIntent().getExtras();
        int Id = -1; // or other values
        String mName = "";
        String mImageUrl = "";
        if (b != null) {
            Id = b.getInt("Id");
            Log.d("IDD", "" + Id);
            mName = b.getString("mName");
            mImageUrl = b.getString("mImageURL");
        }

        user_profile_name.setText(mName);
        toolBarText.setText(mName);

        friendStatus = (Button) findViewById(R.id.friendStatus);
        friendStatus.setText(" ");
        aboutFriendIcon = (ImageView) findViewById(R.id.aboutFriendIcon);
        coverPhoto = (ImageView) findViewById(R.id.coverPhoto);
   /*    img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });*/
        final Button confirmRequest = (Button) findViewById(R.id.ConfirmRequest);
        final Button deleteRequest = (Button) findViewById(R.id.deleteRequest);
        final Button myFollowState = (Button) findViewById(R.id.myFollowState);
        final ImageView otherFollowState = (ImageView) findViewById(R.id.otherFollowState);

        progressBar_button = (ProgressBar) findViewById(R.id.progressBar_button);
        progressBar_button.setProgress(0);
        progressBar_button.setMax(100);
        anim_button = ObjectAnimator.ofInt(progressBar_button, "progress", 0, 100);
        anim_button.setDuration(1000);
        anim_button.setInterpolator(new DecelerateInterpolator());
        anim_button.start();

        progressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        // progressBar.setVisibility(View.VISIBLE);
        anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        img = (CircleImageView) findViewById(R.id.user_profile_photo);
        String imageUrl = GeneralAppInfo.SPRING_URL + "/" + mImageUrl;
        Picasso.with(getApplicationContext()).load(imageUrl).into(img);

        ConfirmDeletion = new Dialog(OtherProfileActivity.this);
        ConfirmDeletion.setContentView(R.layout.confirm_delete_friend_or_request_dialog);
        NoBtn = (Button) ConfirmDeletion.findViewById(R.id.NoBtn);
        YesBtn = (Button) ConfirmDeletion.findViewById(R.id.YesBtn);
        textMsg = (TextView) ConfirmDeletion.findViewById(R.id.TextMsg);

        Id1 = Id;
        Log.d("IDD1", "" + Id);
        GeneralFunctions generalFunctions = new GeneralFunctions();
        boolean isOnline = generalFunctions.isOnline(getApplicationContext());

        if (isOnline == false) {
            Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GeneralAppInfo.SPRING_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            FriendshipInterface getFreindApi = retrofit.create(FriendshipInterface.class);
            Call<FriendResponseModel> call = getFreindApi.getFollowRelationState(GeneralAppInfo.getUserID(), Id);
            final Integer[] FollowRelationState = new Integer[2];

            final String finalMName = mName;
            call.enqueue(new Callback<FriendResponseModel>() {
                @Override
                public void onResponse(Call<FriendResponseModel> call, final Response<FriendResponseModel> response) {
                    Log.d("GetState", "get Friend State code is " + response.code());
                    if (response.code() == 200) {
                        if (GeneralAppInfo.getUserID() == response.body().getFriend1_id().getId()) {
                            FollowRelationState[0] = response.body().getFriend1_state();
                            FollowRelationState[1] = response.body().getFriend2_state();
                        } else {
                            FollowRelationState[0] = response.body().getFriend2_state();
                            FollowRelationState[1] = response.body().getFriend1_state();

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBar_button.setVisibility(View.INVISIBLE);
                        if (FollowRelationState[1] == 0) {

                            //    FriendsClass friendsClass = new FriendsClass();

                            if (FollowRelationState[0] != null) {
                                Log.d("stateeee", "" + FollowRelationState[0]);
                                if (FollowRelationState[0] == 0) {  // No relation
                                    GeneralAppInfo.friendMode = 0;
                                    FollowFunctions.setFollowRelationState(friendStatus, OtherProfileActivity.this, Id1, getApplicationContext());

                                } else if (FollowRelationState[0] == 1) { // Follow Request Pending
                                    GeneralAppInfo.friendMode = 1;
                                    FollowFunctions.setFollowRelationState(friendStatus, OtherProfileActivity.this, Id1, getApplicationContext());
                                } else if (FollowRelationState[0] == 2) { // Follower
                                    GeneralAppInfo.friendMode = 2;
                                    FollowFunctions.setFollowRelationState(friendStatus, OtherProfileActivity.this, Id1, getApplicationContext());
                                }
                            }
                        } else {

                            friendStatus.setVisibility(View.INVISIBLE);
                            myFollowState.setVisibility(View.VISIBLE);
                            otherFollowState.setVisibility(View.VISIBLE);

                            // myFollowState
                            //   FriendsClass friendsClass = new FriendsClass();
                            if (FollowRelationState[0] != null) {
                                Log.d("stateeee", "" + FollowRelationState[0]);
                                if (FollowRelationState[0] == 0) {  // No relation
                                    GeneralAppInfo.friendMode = 0;
                                    FollowFunctions.setFollowRelationState(myFollowState, OtherProfileActivity.this, Id1, getApplicationContext());

                                } else if (FollowRelationState[0] == 1) { // Follow Request Pending
                                    GeneralAppInfo.friendMode = 1;
                                    FollowFunctions.setFollowRelationState(myFollowState, OtherProfileActivity.this, Id1, getApplicationContext());
                                } else if (FollowRelationState[0] == 2) { // Follower
                                    GeneralAppInfo.friendMode = 2;
                                    FollowFunctions.setFollowRelationState(myFollowState, OtherProfileActivity.this, Id1, getApplicationContext());
                                }
                            }

                            if (FollowRelationState[1] == 1) // follower request Pending ( follower sent request)
                            {
                                otherFollowState.setImageResource(R.drawable.follower_request_pending);
                                otherFollowState.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        textMsg.setText("Do you want to accept " + finalMName + " follow request?");
                                        ConfirmDeletion.show();

                                        NoBtn.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                ConfirmDeletion.dismiss();
                                                FollowFunctions.DeleteFriendwithBtn(GeneralAppInfo.getUserID(), Id1,friendStatus,myFollowState,otherFollowState);
                                            }
                                        });

                                        YesBtn.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                ConfirmDeletion.dismiss();
                                                FollowFunctions.ConfirmFollowRequest(GeneralAppInfo.getUserID(), Id1, otherFollowState);
                                            }
                                        });
                                    }
                                });
                            }
                            if (FollowRelationState[1] == 2) {
                                otherFollowState.setImageResource(R.drawable.follower_icon);
                                otherFollowState.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        textMsg.setText(" Do you want to remove " + finalMName + " from your followers ?");
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
                                                FollowFunctions.DeleteFriendwithBtn(GeneralAppInfo.getUserID(), Id1,friendStatus,myFollowState,otherFollowState);
                                            }
                                        });
                                    }
                                });
                            }

                        }
                        fillAbout(about_bio, about_status, about_song, Id1);


                    }
                }

                @Override
                public void onFailure(Call<FriendResponseModel> call, Throwable t) {
                    Log.d("stateeee", "fail " + t.getMessage());

                }
            });

            ViewImgDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            ViewImgDialog.setContentView(R.layout.view_profilepic_dialog);
            imageView = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);

            Log.d("Image Alpha", "  Here is  " + img.getImageAlpha());
            AboutFriendDialog = new Dialog(this);
            AboutFriendDialog.setContentView(R.layout.about_other_dialog);
            AboutFriendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            about_bio = (TextView) AboutFriendDialog.findViewById(R.id.Bio);
            about_status = (TextView) AboutFriendDialog.findViewById(R.id.status);
            about_song = (TextView) AboutFriendDialog.findViewById(R.id.Song);
            aboutUsername = (TextView) AboutFriendDialog.findViewById(R.id.aboutUsername);
            aboutUsername.setText("About " + mName);
            editBio = (TextView) findViewById(R.id.editBio);


            img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imageView.setImageDrawable(img.getDrawable());
                    ViewImgDialog.show();
                }
            });

            coverPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imageView.setImageDrawable(coverPhoto.getDrawable());
                    ViewImgDialog.show();
                }
            });


            aboutFriendIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AboutFriendDialog.show();
                }
            });
        }

        toolBarText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("---", " Clicked on Text");

                final int DRAWABLE_LEFT = 0;

                if (event.getX() <= (toolBarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                    Log.d("---", " Clicked on left");
                    finish();

                    return true;
                }

                return false;
            }
        });

        showOtherSong.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), YoutubePlayerDialogActivity.class);
                Bundle b = new Bundle();
                b.putString("youtubeSongUrl",SongUrl);
                i.putExtras(b);
                startActivity(i);

            }
        });

    }

    public void fillAbout(final TextView bio, final TextView status, final TextView song, final int ID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AboutUserInterface aboutUserApi = retrofit.create(AboutUserInterface.class);
        Call<AboutUserResponseModel> call = aboutUserApi.getAboutUser(ID);
        call.enqueue(new Callback<AboutUserResponseModel>() {
            @Override
            public void onResponse(Call<AboutUserResponseModel> call, Response<AboutUserResponseModel> response) {
                Log.d("AboutUserFill", "Success  " + response.code());

                if (response != null) {
                    if (response.body() != null) {
                        Log.d("AboutUserFill", "Success");

                        editBio.setText(response.body().getUserBio());
                        bio.setText(response.body().getUserBio());
                        status.setText(response.body().getUserStatus());
                        song.setText(response.body().getUserSong());
                        SongUrl = response.body().getUserSong();
                    }
                }
            }

            @Override
            public void onFailure(Call<AboutUserResponseModel> call, Throwable t) {
                Log.d("AboutUserFill", "Failure " + t.getMessage() + " " + ID);
            }
        });

    }

}
