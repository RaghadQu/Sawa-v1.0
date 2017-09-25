package com.example.zodiac.sawa.Activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Services.ImageConverterService;
import com.example.zodiac.sawa.SpringApi.AboutUserInterface;
import com.example.zodiac.sawa.SpringApi.ImageInterface;
import com.example.zodiac.sawa.SpringModels.AboutUserRequestModel;
import com.example.zodiac.sawa.SpringModels.AboutUserResponseModel;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProfileActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int SELECTED_PICTURE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";
    public static ObjectAnimator anim;
    static UserModel userInfo;
    static Context context;
    private static ProgressBar progressBar;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public String video_id = "rzLKwtC5q1k";
    ImageView img;
    ImageView coverImage;
    TextView userName;
    String songUrl;
    int youtubeFlag = 0;
    TextView followerTxt, followingTxt, newPostTxt;
    TextView profileBio;
    CircleImageView editProfile, editSong;
    Button saveAbout, saveSong;
    Uri imageuri;
    EditText bioTxt, statusTxt, songTxt;
    Dialog imgClick;
    Dialog ViewImgDialog;
    Dialog editMyBio, editMySong;
    TextView changePic, viewPic, RemovePic, toolBarText;
    ImageView imageView; // View image in dialog
    int image1 = R.drawable.image1;
    int image2 = R.drawable.friends_icon;
    int image3 = R.drawable.friends_icon;
    int image4 = R.drawable.image1;
    TextView editBio;
    String[] myDataset = {"Profile", "Friends", "Friend Requests", "Log out"};
    int[] images = {image1, image2, image3, image4};
    ProgressBar coverProgressBar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        Log.d("width", "" + realImage.getWidth());
        Log.d("height", "" + realImage.getHeight());
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());


        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        fillAbout();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        context = this;
        GeneralFunctions generalFunctions = new GeneralFunctions();
        boolean isOnline = generalFunctions.isOnline(getApplicationContext());
        followerTxt = (TextView) findViewById(R.id.followerTxt);
        followingTxt = (TextView) findViewById(R.id.followingTxt);
        newPostTxt = (TextView) findViewById(R.id.newPostTxt);
        editProfile = (CircleImageView) findViewById(R.id.editProfile);
        editSong = (CircleImageView) findViewById(R.id.editSong);
        coverImage = (ImageView) findViewById(R.id.coverImage);
        profileBio = (TextView) findViewById(R.id.profileBio);
        userName = (TextView) findViewById(R.id.user_profile_name);
        editMySong = new Dialog(this);
        editMySong.setContentView(R.layout.edit_song_profile_dialog);
        editMySong.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        coverProgressBar = (ProgressBar) findViewById(R.id.coverProgressBar);
        songTxt = (EditText) editMySong.findViewById(R.id.songTxt);
        saveSong = (Button) editMySong.findViewById(R.id.saveSong);
        img = (ImageView) findViewById(R.id.user_profile_photo);
        editBio = (TextView) findViewById(R.id.editBio);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        getUserInfo();

//        userName.setText(GeneralAppInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralAppInfo.getGeneralUserInfo().getUser().getLast_name());
//        String imageUrl = GeneralAppInfo.SPRING_URL + "/" + GeneralAppInfo.getGeneralUserInfo().getUser().getImage();
//        //   progressBar.setVisibility(View.INVISIBLE);
//        Bitmap image = getBitmapfromUrl(imageUrl);
//        img.setImageBitmap(image);
//        // Picasso.with(context).load(imageUrl).into(img);
//        String coverUrl = GeneralAppInfo.SPRING_URL + "/" + GeneralAppInfo.getGeneralUserInfo().getUser().getCover_image();
//        Picasso.with(context).load(coverUrl).into(coverImage);
//        profileBio.setText(GeneralAppInfo.getGeneralUserInfo().getAboutUser().getUserBio());

        if (isOnline == false) {
            Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();
        } else {
            progressBar = (ProgressBar) findViewById(R.id.profilePictureProgressBar);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
            anim.setDuration(2000);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.start();
            //   progressBar.setVisibility(View.VISIBLE);
            imgClick = new Dialog(this);
            imgClick.setContentView(R.layout.profile_picture_dialog);

            ViewImgDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            ViewImgDialog.setContentView(R.layout.view_profilepic_dialog);
            imageView = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);


            editMyBio = new Dialog(this);
            editMyBio.setContentView(R.layout.edit_my_bio_dialog);
            editMyBio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            saveAbout = (Button) editMyBio.findViewById(R.id.saveAbout);
            bioTxt = (EditText) editMyBio.findViewById(R.id.bioTxt);
            statusTxt = (EditText) editMyBio.findViewById(R.id.statusTxt);
            fillAbout();


            //Profile Picture
            img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgClick.getWindow().getAttributes().y = -90;
                    imgClick.getWindow().getAttributes().x = 130;
                    imgClick.show();
                    changePic = (TextView) imgClick.findViewById(R.id.EditPic);
                    changePic.setText("Change Profile Picture");
                    viewPic = (TextView) imgClick.findViewById(R.id.ViewPic);
                    viewPic.setText("View Profile Picture");
                    RemovePic = (TextView) imgClick.findViewById(R.id.RemovePic);
                    RemovePic.setText("Remove Profile Picture");
                    changePic.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            imgClick.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 100);
                        }
                    });

                    viewPic.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            imgClick.dismiss();
                            imageView.setImageDrawable(img.getDrawable());
                            ImageView coverImageDialog = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);
                            coverImageDialog.setImageDrawable(img.getDrawable());
                            ViewImgDialog.show();

                        }
                    });

                    RemovePic.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            imgClick.dismiss();
                            removeImage(0);


                        }
                    });
                }
            });

            coverImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imgClick.getWindow().getAttributes().y = -280;
                    imgClick.getWindow().getAttributes().x = 30;
                    imgClick.show();
                    changePic = (TextView) imgClick.findViewById(R.id.EditPic);
                    changePic.setText("Change Cover Picture");
                    viewPic = (TextView) imgClick.findViewById(R.id.ViewPic);
                    viewPic.setText("View Cover Picture");
                    RemovePic = (TextView) imgClick.findViewById(R.id.RemovePic);
                    RemovePic.setText("Remove Cover Picture");

                    changePic.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            imgClick.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 200);
                        }
                    });

                    viewPic.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            imgClick.dismiss();
                            coverImage.setImageDrawable(coverImage.getDrawable());
                            ImageView coverImageDialog = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);
                            coverImageDialog.setImageDrawable(coverImage.getDrawable());
                            ViewImgDialog.show();
                        }
                    });

                    RemovePic.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            imgClick.dismiss();
                            removeImage(1);

                        }
                    });
                }
            });

            toolBarText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;

                    if (event.getX() <= (toolBarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        finish();
                        return true;
                    }

                    if (event.getX() >= 900) {
                        return true;
                    }


                    return false;
                }
            });


            followerTxt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), MyFollowersActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("source", 0);
                    i.putExtras(b);
                    startActivity(i);
                }
            });

            followingTxt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), MyFollowingActivity.class);
                    startActivity(i);
                }
            });

            newPostTxt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), AddPostActivity.class);
                    startActivity(i);
                }
            });

            editProfile.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                    startActivity(i);
                }
            });
            editBio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    editMyBio.show();

                }
            });

            saveAbout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String bioText, statusText, songText;
                    bioText = bioTxt.getText().toString();
                    songText = songTxt.getText().toString();
                    statusText = statusTxt.getText().toString();
                    updateAbout(bioText, statusText, songText);
                    editMyBio.dismiss();

                }
            });

            editSong.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("EditSong", " Edit Song youtubePlayer");
                    Intent i = new Intent(getApplicationContext(), MyYoutubeActivity.class);
                    Bundle b = new Bundle();
                    if (songUrl != null) {
                        b.putString("youtubeSongUrl", songUrl);
                    }
                    i.putExtras(b);
                    startActivity(i);

                }
            });

            saveSong.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String bioText, statusText, songText;
                    bioText = bioTxt.getText().toString();
                    songText = songTxt.getText().toString();
                    statusText = statusTxt.getText().toString();
                    updateAbout(bioText, statusText, songText);
                    editMySong.dismiss();

                }
            });

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == 100 || requestCode == 200)) {
            imageuri = data.getData();
            try {
                verifyStoragePermissions(this);
                GeneralFunctions generalFunctions = new GeneralFunctions();
                String path = generalFunctions.getRealPathFromURI(this, imageuri);
                Log.d("Path", path);
                int rotate = generalFunctions.getPhotoOrientation(path);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
                bitmap = scaleDown(bitmap, 1000, true);
                bitmap = RotateBitmap(bitmap, rotate);
                if (requestCode == 100) {
                    img.setImageBitmap(bitmap);
                }
                if (requestCode == 200) {
                    coverImage.setImageBitmap(bitmap);
                }
                ImageConverterService imageConverter = new ImageConverterService();
                byte[] image = imageConverter.getBytes(bitmap);
                String encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
                Log.d("XX", "arrive");
            } catch (Exception e) {
                Toast toast = Toast.makeText(this, "Image is large", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void getUserInfo() {
        Log.d("InfoUser", " Enter here " + GeneralAppInfo.getGeneralUserInfo().getUser());
        userName.setText(GeneralAppInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralAppInfo.getGeneralUserInfo().getUser().getLast_name());
        String imageUrl = GeneralAppInfo.SPRING_URL + "/" + GeneralAppInfo.getGeneralUserInfo().getUser().getImage();
        Log.d("InfoUser", " " + imageUrl);
        progressBar.setVisibility(View.INVISIBLE);
        Picasso.with(context).load(imageUrl).into(img);
        String coverUrl = GeneralAppInfo.SPRING_URL + "/" + GeneralAppInfo.getGeneralUserInfo().getUser().getCover_image();
        Picasso.with(context).load(coverUrl).into(coverImage);

    }

    public void fillAbout() {

        profileBio.setText(GeneralAppInfo.getGeneralUserInfo().getAboutUser().getUserBio());
        bioTxt.setText(GeneralAppInfo.getGeneralUserInfo().getAboutUser().getUserBio());
        statusTxt.setText(GeneralAppInfo.getGeneralUserInfo().getAboutUser().getUserStatus());
        songTxt.setText(GeneralAppInfo.getGeneralUserInfo().getAboutUser().getUserSong());
        songUrl = GeneralAppInfo.getGeneralUserInfo().getAboutUser().getUserSong();
    }

    public void updateAbout(final String bioText, final String statusText, final String songText) {
        AboutUserRequestModel aboutUserModel = new AboutUserRequestModel(GeneralAppInfo.getUserID(), bioText, statusText, songText);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AboutUserInterface aboutUserApi = retrofit.create(AboutUserInterface.class);

        Call<AboutUserResponseModel> call = aboutUserApi.addNewAboutUser(aboutUserModel);
        call.enqueue(new Callback<AboutUserResponseModel>() {
            @Override
            public void onResponse(Call<AboutUserResponseModel> call, Response<AboutUserResponseModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                    Log.d("AboutUserUpdate", "Done successfully");
                    fillAbout();
                }
            }

            @Override
            public void onFailure(Call<AboutUserResponseModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("AboutUserUpdate", "Failure " + t.getMessage());
            }
        });

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

    public void removeImage(int profileOrCover) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ImageInterface imageInterface = retrofit.create(ImageInterface.class);
        Call<Integer> removeImageResponse = imageInterface.removeImage(GeneralAppInfo.userID, profileOrCover);
        removeImageResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("ImagesCode ", " " + response.code());
                //    getUserInfo();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("ImagesCode ", " Error " + t.getMessage());

            }
        });

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}
