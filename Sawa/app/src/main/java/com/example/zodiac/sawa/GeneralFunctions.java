package com.example.zodiac.sawa;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zodiac.sawa.Activities.MyProfileActivity;
import com.example.zodiac.sawa.SpringApi.ImageInterface;
import com.example.zodiac.sawa.SpringModels.DeviceTokenModel;
import com.example.zodiac.sawa.SpringApi.DeviceTokenInterface;
import com.example.zodiac.sawa.SpringModels.UserModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getClientToken;

/**
 * Created by Rabee on 4/17/2017.
 */

public class GeneralFunctions {
    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static SharedPreferences getSharedPreferences(Context ctxt) {

        SharedPreferences sharedPreferences = ctxt.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int notifications_counter = sharedPreferences.getInt("notifications_counter", 0);
        Log.d("notifications_counte", "" + notifications_counter);


        return sharedPreferences;

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int getPhotoOrientation(String imagePath) {
        int rotate = 0;
        try {


            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public void storeUserIdWithDeviceId(int user_id, String deviceId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeviceTokenInterface tokenApi = retrofit.create(DeviceTokenInterface.class);
        DeviceTokenModel deviceTokenModel = new DeviceTokenModel();
        deviceTokenModel.setDeviceId(deviceId);
        deviceTokenModel.setToken(getClientToken());
        Call<DeviceTokenModel> call = tokenApi.storeDeviceToken(deviceTokenModel);
        call.enqueue(new Callback<DeviceTokenModel>() {

            @Override
            public void onResponse(Call<DeviceTokenModel> call, Response<DeviceTokenModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<DeviceTokenModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }
        });
    }

    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public File saveBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void showErrorMesaage(Context context) {
        Toast.makeText(context, "Something went worng",
                Toast.LENGTH_SHORT).show();
    }

    public void uploadImagetoDB(int user_id,  String path, Bitmap bitmap, int requestCode, final ProgressBar imageProgressBar) {
        File file = new File(path);
        GeneralFunctions generalFunctions = new GeneralFunctions();

        file = generalFunctions.saveBitmap(bitmap, path);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploadfile", file.getName(), requestFile);

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
               // MyProfileActivity.getUserInfo();
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
}