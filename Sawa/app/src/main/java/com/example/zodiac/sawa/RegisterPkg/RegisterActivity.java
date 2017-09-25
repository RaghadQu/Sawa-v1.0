package com.example.zodiac.sawa.RegisterPkg;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.zodiac.sawa.Activities.HomeTabbedActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.AboutUserInterface;
import com.example.zodiac.sawa.SpringApi.AuthInterface;
import com.example.zodiac.sawa.SpringModels.AboutUserRequestModel;
import com.example.zodiac.sawa.SpringModels.AboutUserResponseModel;
import com.example.zodiac.sawa.SpringModels.GeneralUserInfoModel;
import com.example.zodiac.sawa.SpringModels.SignUpModel;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zodiac on 07/10/2017.
 */

public class RegisterActivity extends Activity {

    String FirstName;
    String LastName;
    String userEmail;
    String Password;
    String mobileNumber;
    String userGender;
    String userBirthDate;
    Dialog LoggingInDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.your_placeholder, new NameFragment());
        ft.commit();
    }


    public void replaceFragmnets(android.app.Fragment f) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.your_placeholder, f);
        ft.addToBackStack(null);
        ft.commit();
    }


    public String getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void SignUp() {
        GeneralFunctions generalFunctions=new GeneralFunctions();
        SignUpModel signUpModel = new SignUpModel();
        signUpModel.setFirst_name(getFirstName());
        signUpModel.setLast_name(getLastName());
        signUpModel.setMobile(getMobileNumber());
        signUpModel.setEmail(getUserEmail());
        signUpModel.setPassword(getPassword());
        signUpModel.setGender(getUserGender());
        signUpModel.setBirthdate(getUserBirthDate());
        Log.d("Birthdate1", getUserBirthDate().toString().trim());
        LoggingInDialog = new Dialog(this);
        LoggingInDialog.setContentView(R.layout.logging_in_dialog);
        boolean isOnline = generalFunctions.isOnline(getApplicationContext());

        if (isOnline == false) {
            Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();
        } else {
            LoggingInDialog.show();


            AuthInterface authInterface;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GeneralAppInfo.SPRING_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            authInterface = retrofit.create(AuthInterface.class);


            final Call<GeneralUserInfoModel> signResponse = authInterface.signUp(signUpModel);
            signResponse.enqueue(new Callback<GeneralUserInfoModel>() {


                @Override
                public void onResponse(Call<GeneralUserInfoModel> call, Response<GeneralUserInfoModel> response) {
                    int statusCode = response.code();
                    GeneralUserInfoModel userModel = response.body();
                    Log.d("SignUpNew", response.code() + " ");
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                    if (response.code() == 200) {
                        updateAbout("", "", "");
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); // what 90 does ??
                        byte[] image = stream.toByteArray();
                        GeneralAppInfo.setUserID(userModel.getUser().getId());
                        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", userModel.getUser().getEmail());
                        editor.putInt("id", GeneralAppInfo.getUserID());
                        editor.putString("isLogined", "1");
                        editor.apply();
                        Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                        LoggingInDialog.dismiss();
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(i);
                        finish();

                    } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                    } else Log.d("valid", "already added");

                }

                @Override
                public void onFailure(Call<GeneralUserInfoModel> call, Throwable t) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                    Log.d("notvalid", "valid" + t.getMessage());
                }
            });
        }
    }
	
	 public static void updateAbout(final String bioText, final String statusText, final String songText) {
        AboutUserRequestModel aboutUserModel = new AboutUserRequestModel(GeneralAppInfo.getUserID(), bioText, statusText, songText);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AboutUserInterface aboutUserApi = retrofit.create(AboutUserInterface.class);

        Call<AboutUserResponseModel> call = aboutUserApi.addNewAboutUser(aboutUserModel);
        call.enqueue(new Callback<AboutUserResponseModel>() {
            @Override
            public void onResponse(Call<AboutUserResponseModel> call, Response<AboutUserResponseModel> response) {
                Log.d("AboutUserUpdate", "Done successfully");
            }

            @Override
            public void onFailure(Call<AboutUserResponseModel> call, Throwable t) {
                Log.d("AboutUserUpdate", "Failure " + t.getMessage());
            }
        });

    }


}

