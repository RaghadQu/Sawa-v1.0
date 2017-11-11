package com.example.zodiac.sawa.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringApi.PostInterface;
import com.example.zodiac.sawa.SpringModels.PostRequestModel;
import com.example.zodiac.sawa.SpringModels.PostResponseModel;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zodiac on 06/07/2017.
 */

public class AddPostActivity extends Activity {/// extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int SELECTED_PICTURE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";
    static public CircleImageView senderImage, receiverImage;
    public static ArrayList<MyFollowersActivity.friend> FriendPostList = new ArrayList<>();
    static int ReceiverID;
    Switch showComments;
    public String video_id = "rzLKwtC5q1k";
    YouTubePlayerView youTubePlayerView;
    int youtubeFlag = 0;
    Button Cancelbtn, PostBtn;
    CircleButton anonymousBtn;
    EditText PostText;
    //   TextView AddImage;
    ImageView PostImage, AddImage, sendPost;
    PostRequestModel postRequestModel;
    TextView DeletePostImage;
    FastScrollRecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FriendshipInterface service;
    ProgressBar postProgress;
    static Bitmap imagePost = null;
    static boolean isThereIsImage=false;
    static String path;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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


    public static void setRecieverID(int ID) {
        ReceiverID = ID;
    }

    public static int getReceiverID() {
        return ReceiverID;
    }

    public static void setRecieverImage(String image, Context context) {
        String imageURL = GeneralAppInfo.IMAGE_URL + image;
        Picasso.with(context).load(imageURL).into(receiverImage);

    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postpost);
//        postProgress = (ProgressBar) findViewById(R.id.postProgress);
//        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube);
//        youTubePlayerView.setVisibility(View.GONE);
        PostText = (EditText) findViewById(R.id.postText);
        sendPost = (ImageView) findViewById(R.id.sendPost);
        PostImage = (ImageView) findViewById(R.id.showPhoto);
        AddImage = (ImageView) findViewById(R.id.addPhoto);
        showComments = (Switch) findViewById(R.id.showComments);

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

            /*    if (i == 0 && youtubeFlag == 0) {
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

                }*/


            }
        });


//        postProgress = (ProgressBar) findViewById(R.id.postProgress);
//        anonymousBtn = (CircleButton) findViewById(R.id.anonymous);
//        Cancelbtn = (Button) findViewById(R.id.CancelBtn);
//        PostBtn = (Button) findViewById(R.id.PostBtn);
//        senderImage = (CircleImageView) findViewById(R.id.senderImage);
//        receiverImage = (CircleImageView) findViewById(R.id.receiverImage);
//        AddImage = (TextView) findViewById(R.id.AddImage);
//        DeletePostImage = (TextView) findViewById(R.id.cross);
//        Log.d("DeletePostImage", " " + DeletePostImage);
//        DeletePostImage.setVisibility(View.INVISIBLE);
//
//        adapter = new AddPostImagesAdapter(this, FriendPostList);
//        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(adapter);
//
//        final int[] flag = {0};
//        anonymousBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (flag[0] == 0) {
//                    flag[0] = 1;
//                    anonymousBtn.setColor(getResources().getColor(R.color.green));
//                } else {
//                    flag[0] = 0;
//                    anonymousBtn.setColor(getResources().getColor(android.R.color.darker_gray));
//                }
//            }
//        });
//
        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewPost();

            }
        });

//
        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(AddPostActivity    .this);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_PICTURE);
                //    PostImage.setImageResource(R.drawable.image2);
            }
        });
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(GeneralAppInfo.BACKEND_URL)
//                .addConverterFactory(GsonConverterFactory.create()).build();
//        service = retrofit.create(FriendshipInterface.class);
//
//
//    }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
            Uri imageuri = data.getData();
            try {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                path = generalFunctions.getRealPathFromURI(this, imageuri);

                Bitmap bitmap = setPostImage(imageuri);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);

                int newWidth = (int) (bitmap.getWidth());
                int newHeight = (int) (bitmap.getHeight());
                Log.d("ImageAddPost", " width " + newWidth + "  " + newHeight);
                Log.d("ImageAddPost", " width " + PostImage.getWidth() + "  " + PostImage.getHeight());

                if (newHeight >= 500) {
                    if (newWidth > newHeight) {
                        double scale = 920.0 / newWidth;
                        newWidth = (int) (newWidth * scale);
                        newHeight = (int) (newHeight * scale);
                    }
                } else {
                    double scale = 920.0 / newWidth;
                    newWidth = (int) (newWidth * scale);
                    newHeight = (int) (newHeight * scale);
                }

                Bitmap resized = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                PostImage.setImageBitmap(bitmap);
                imagePost = bitmap;
                isThereIsImage=true;

                //  PostImage.setImageBitmap(bitmap);
                //    DeletePostImage.setVisibility(View.VISIBLE);

              /*  DeletePostImage.setOnClickListener(new View.OnClickListener() {
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
                });*/

            } catch (IOException e) {
                e.printStackTrace();
            }
            // PostImage.setImageURI(imageuri);

        }
    }


    public void AddNewPost() {

        if (isThereIsImage == true) {
            AddNewImagePost();
        } else {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PostInterface Postservice;
        Postservice = retrofit.create(PostInterface.class);

        PostRequestModel postRequestModel = new PostRequestModel();
        postRequestModel.setUserId(GeneralAppInfo.getGeneralUserInfo().getUser().getId());
        postRequestModel.setText(PostText.getText().toString());
        postRequestModel.setIs_public_comment(showComments.isChecked());



            Call<PostResponseModel> PostRespone = Postservice.addNewPost(postRequestModel);
            Log.d("AddPostActivity", " Add post after request");
            PostRespone.enqueue(new Callback<PostResponseModel>() {
                @Override
                public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
//                    postProgress.setVisibility(ProgressBar.INVISIBLE);

                    Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<PostResponseModel> call, Throwable t) {
                    //      postProgress.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(AddPostActivity.this, "Oops! Something went wrong, please try again.",
                            Toast.LENGTH_SHORT).show();
                }

            });
        }

    }

    public void AddNewImagePost() {
        isThereIsImage=false;
        File file = new File(path);
        final GeneralFunctions generalFunctions = new GeneralFunctions();

        file = generalFunctions.saveBitmap(imagePost, path);
        Log.d("file",path);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploadfile", file.getName(), requestFile);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PostInterface Postservice;
        Postservice = retrofit.create(PostInterface.class);
        Postservice = retrofit.create(PostInterface.class);

        PostRequestModel postRequestModel = new PostRequestModel();
        postRequestModel.setUserId(GeneralAppInfo.getGeneralUserInfo().getUser().getId());
        postRequestModel.setText(PostText.getText().toString());
        postRequestModel.setIs_public_comment(showComments.isChecked());

        Call<PostResponseModel> PostRespone = Postservice.addNewPost(body,postRequestModel.getUserId(),postRequestModel.getText(),postRequestModel.getIs_public_comment());
        PostRespone.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
//                    postProgress.setVisibility(ProgressBar.INVISIBLE);

                Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                //      postProgress.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(AddPostActivity.this, "Oops! Something went wrong, please try again.",
                        Toast.LENGTH_SHORT).show();
            }

        });


    }


    public Bitmap setPostImage(Uri imageuri) throws IOException {
        GeneralFunctions generalFunctions = new GeneralFunctions();
        String path = generalFunctions.getRealPathFromURI(this, imageuri);
        int rotate = generalFunctions.getPhotoOrientation(path);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
        bitmap = MyProfileActivity.scaleDown(bitmap, 3000, true);
        bitmap = MyProfileActivity.RotateBitmap(bitmap, rotate);

        return bitmap;

    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
//        youTubePlayer.setPlaybackEventListener(playbackEventListener);
//        youTubePlayer.cueVideo(video_id);
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    //}
    // }
}
