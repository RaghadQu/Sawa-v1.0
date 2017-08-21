package com.example.zodiac.sawa.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Services.ImageConverterService.*;
import com.example.zodiac.sawa.RecyclerViewAdapters.AddPostImagesAdapter;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zodiac on 06/07/2017.
 */

public class AddPostActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int SELECTED_PICTURE = 100;

    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";
    static public CircleImageView senderImage, receiverImage;
    public static ArrayList<MyFollowersActivity.friend> FriendPostList = new ArrayList<>();
    static int ReceiverID;
    static String postImage = "";
    public String video_id = "rzLKwtC5q1k";
    YouTubePlayerView youTubePlayerView;
    int youtubeFlag = 0;
    Button Cancelbtn, PostBtn;
    CircleButton anonymousBtn;
    EditText PostText;
    TextView AddImage;
    ImageView PostImage;
    TextView DeletePostImage;
    FastScrollRecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FriendshipInterface service;
    ProgressBar postProgress;

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };
    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    public static String getPostImage() {
        return postImage;
    }

    public static void setRecieverID(int ID) {
        ReceiverID = ID;
    }

    public static int getReceiverID() {
        return ReceiverID;
    }

    public static void setRecieverImage(String image, Context context) {
        String imageURL = GeneralAppInfo.IMAGE_URL + image;
        Picasso.with(context).load(imageURL).into(receiverImage);
        Log.d("AdapterImage", " reciever image is : " + image);

    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postProgress = (ProgressBar) findViewById(R.id.postProgress);
        setContentView(R.layout.add_post_activity);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube);
        youTubePlayerView.setVisibility(View.GONE);
        PostText = (EditText) findViewById(R.id.PostText);
        PostText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                String pattern = "https://m.youtube.com/watch?v=";
                String s = String.valueOf(PostText.getText());
                int i = s.indexOf(pattern);
                Log.d("II", "" + i);

                if (i == 0 && youtubeFlag == 0) {
                    String[] split = s.split("v=");
                    video_id = split[1];
                    youTubePlayerView = new YouTubePlayerView(AddPostActivity.this);
                    youTubePlayerView.initialize(api_key, AddPostActivity.this);

                    addContentView(youTubePlayerView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    youTubePlayerView.setVisibility(View.VISIBLE);
                    youTubePlayerView.initialize(api_key, AddPostActivity.this);
                    youtubeFlag = 1;
                } else if (i == -1 && youtubeFlag == 0) {
                    youTubePlayerView.setVisibility(View.INVISIBLE);

                }


            }
        });
        PostImage = (ImageView) findViewById(R.id.PostImage);
        postProgress = (ProgressBar) findViewById(R.id.postProgress);
        anonymousBtn = (CircleButton) findViewById(R.id.anonymous);
        Cancelbtn = (Button) findViewById(R.id.CancelBtn);
        PostBtn = (Button) findViewById(R.id.PostBtn);
        senderImage = (CircleImageView) findViewById(R.id.senderImage);
        receiverImage = (CircleImageView) findViewById(R.id.receiverImage);
        AddImage = (TextView) findViewById(R.id.AddImage);
        DeletePostImage = (TextView) findViewById(R.id.cross);
        Log.d("DeletePostImage", " " + DeletePostImage);
        DeletePostImage.setVisibility(View.INVISIBLE);

        adapter = new AddPostImagesAdapter(this, FriendPostList);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        final int[] flag = {0};
        anonymousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0] == 0) {
                    flag[0] = 1;
                    anonymousBtn.setColor(getResources().getColor(R.color.green));
                } else {
                    flag[0] = 0;
                    anonymousBtn.setColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

        PostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewPost(flag[0]);
            }
        });

        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                startActivity(i);
            }
        });

        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_PICTURE);
                //    PostImage.setImageResource(R.drawable.image2);
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(FriendshipInterface.class);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
            Uri imageuri = data.getData();
            try {
                Bitmap bitmap = setPostImage(imageuri);
               /*  Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);

                int newWidth = (int) (bitmap.getWidth());
                int newHeight = (int) (bitmap.getHeight());
                Log.d("ImageAddPost", " width " + newWidth + "  "+ newHeight);
                Log.d("ImageAddPost", " width " + PostImage.getWidth() + "  "+ PostImage.getHeight());

               if (newHeight >= 500) {
                   // if (newWidth > newHeight) {
                        double scale = 920.0 / newWidth;
                        newWidth = (int) (newWidth * scale);
                        newHeight = (int) (newHeight * scale);
                   } else {
                        double scale = 920.0 / newWidth;
                        newWidth = (int) (newWidth * scale);
                        newHeight = (int) (newHeight * scale);
                    }
                }*/
                //Bitmap resized = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                PostImage.setImageBitmap(bitmap);

                //  PostImage.setImageBitmap(bitmap);
                DeletePostImage.setVisibility(View.VISIBLE);

                DeletePostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostImage.setImageBitmap(null);
                        DeletePostImage.setVisibility(View.INVISIBLE);

                    }
                });

                Cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                        startActivity(i);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            // PostImage.setImageURI(imageuri);

        }
    }

    public void AddNewPost(int is_anon) {
     /*   postProgress.setVisibility(ProgressBar.VISIBLE);
        AddPostApi Postservice;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Postservice = retrofit.create(AddPostApi.class);

        final AddNewPostModel request = new AddNewPostModel();
        request.setImage(getPostImage());
        request.setIs_Anon(is_anon);
        request.setLink(" ");
        request.setTo_user_id(getReceiverID());
        request.setUser_id(GeneralAppInfo.getUserID());

        String PostBody = PostText.getText().toString();
        if (getReceiverID() == 0) {
            Toast.makeText(AddPostActivity.this, "Please select a friend.",
                    Toast.LENGTH_SHORT).show();
        } else if (!PostBody.trim().matches("")) {
            request.setText(PostBody.trim());
            final Call<GeneralStateResponeModel> PostRespone = Postservice.addPost(request);
            Log.d("AddPostActivity", " Add post after request");
            PostRespone.enqueue(new Callback<GeneralStateResponeModel>() {
                @Override
                public void onResponse(Call<GeneralStateResponeModel> call, Response<GeneralStateResponeModel> response) {
                    postProgress.setVisibility(ProgressBar.INVISIBLE);

                    Log.d("AddPost", " Add Post done with code " + response.code() + " " + response.body().getState());
                    Log.d("AddPost", " Add Post done with code " + response.code() + " " + response.body().getState());
                    Log.d("AddPost", " Add Post done with code " + response.code() + " ");
                    Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<GeneralStateResponeModel> call, Throwable t) {
                    postProgress.setVisibility(ProgressBar.INVISIBLE);


                    Log.d("fail to get friends ", "Failure to Get friends in AddPostActivity");
                    Log.d("fail to get friends ", "Failure to Get friends in AddPostActivity  ... " + t.getMessage());
                    Toast.makeText(AddPostActivity.this, "Oops! Something went wrong, please try again.",
                            Toast.LENGTH_SHORT).show();
                }

            });

        }

*/
    }

    public Bitmap setPostImage(Uri imageuri) throws IOException {
        GeneralFunctions generalFunctions = new GeneralFunctions();
        String path = generalFunctions.getRealPathFromURI(this, imageuri);
        int rotate = generalFunctions.getPhotoOrientation(path);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
        bitmap = MyProfileActivity.scaleDown(bitmap, 3000, true);
        bitmap = MyProfileActivity.RotateBitmap(bitmap, rotate);
       /* ImageConverter imageConverter = new ImageConverter();
        byte[] image = imageConverter.getBytes(bitmap);
        //DBHandler dbHandler = new DBHandler(this);
       // dbHandler.updateUserImage(GeneralAppInfo.getUserID(), image);
        String encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
        postImage = encodedImage;*/
        return bitmap;

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.cueVideo(video_id);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
