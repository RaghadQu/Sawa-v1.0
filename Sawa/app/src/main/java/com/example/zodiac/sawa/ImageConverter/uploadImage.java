package com.example.zodiac.sawa.ImageConverter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.MenuActiviries.MyProfileActivity;
import com.example.zodiac.sawa.Spring.Models.UserModel;
import com.example.zodiac.sawa.SpringApi.ImageInterface;
import com.example.zodiac.sawa.interfaces.UserImageApi;
import com.example.zodiac.sawa.models.userImageFromDb;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rabee on 4/13/2017.
 */

public class uploadImage {
    String imageUrl;
    Bitmap encodedByte;

    public uploadImage() {

    }

    public void uploadImagetoDB(String path, Bitmap bitmap, int requestCode, final ProgressBar imageProgressBar) {
        File file;
        GeneralFunctions generalFunctions = new GeneralFunctions();
        final MultipartBody.Part body;
        file = generalFunctions.saveBitmap(bitmap, path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        body = MultipartBody.Part.createFormData("uploadfile", file.getName(), requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ImageInterface imageInterface = retrofit.create(ImageInterface.class);
        Call<UserModel> userImageResponse;
        if (requestCode == 100) {
            userImageResponse = imageInterface.uploadProfileImage(body, GeneralAppInfo.userID);
            Log.d("images", "  Profile");
        } else {
            userImageResponse = imageInterface.uploadCoverImage(body, GeneralAppInfo.userID);
            Log.d("images", "  Cover");

        }

        userImageResponse.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("ImagesCode ", " " + response.code());
                MyProfileActivity.getUserInfo();
                imageProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("ImagesCode ", " Error " + t.getMessage());

            }
        });
        /*HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://4bfb21e0.ngrok.io/api/v1/uploadFile");
        Log.d("XX","arrive2");

        httpPost.setEntity(new FileEntity(new File(path), "application/octet-stream"));

        try {
            Log.d("XX","arrive3");

            HttpResponse response = httpClient.execute(httpPost);
            Log.d("XX","arrive4");

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    public String getUserImageFromDB(int user_id, final ImageView img, final Context context, final int isAnim, final ObjectAnimator anim) {
        Log.d("Arrive to ge fro Db", "ss");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        UserImageApi userImageApi = retrofit.create(UserImageApi.class);
        Call<List<userImageFromDb>> userImageResponse = userImageApi.getUserImageFromDb(user_id);
        userImageResponse.enqueue(new Callback<List<userImageFromDb>>() {
            @Override
            public void onResponse(Call<List<userImageFromDb>> call, Response<List<userImageFromDb>> response) {
//     here  //
                Log.d("ImageUpload", " " + response.code());
                //  imageUrl = userImageFromDbs.get(0).getUser_image();
                Log.d("Arrive to ge fro Db11", "s" + imageUrl);
                imageUrl = GeneralAppInfo.IMAGE_URL + imageUrl;
                Log.d("imageYtl", imageUrl);


                if (isAnim == 1) {

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //here animation finished
                            Picasso.with(context).load(imageUrl).into(img);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });


                } else {
                    Picasso.with(context).load(imageUrl).into(img);
                    Log.d("Image", " image loaded");
                }
            }

            @Override
            public void onFailure(Call<List<userImageFromDb>> call, Throwable t) {
                Log.d("fail to get fro Db", "ss");

            }
        });
        return imageUrl;

    }
}
