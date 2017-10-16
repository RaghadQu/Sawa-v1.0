package com.example.zodiac.sawa.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zodiac.sawa.Activities.ChangeAccountInfoActivities.ChangeEmailActivity;
import com.example.zodiac.sawa.Activities.ChangeAccountInfoActivities.ChangeMobileActivity;
import com.example.zodiac.sawa.Activities.ChangeAccountInfoActivities.ChangePasswordActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.AccountInfoInterface;
import com.example.zodiac.sawa.SpringModels.EditEmailModel;
import com.example.zodiac.sawa.SpringModels.EditPrivacyModel;
import com.example.zodiac.sawa.SpringModels.GeneralUserInfoModel;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.google.gson.Gson;

import at.markushi.ui.CircleButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zodiac on 10/08/2017.
 */

public class SettingsActivity extends Activity {

    TextView toolbarText ,saveSettings;
    CircleButton purpleColorBtn , greenColorBtn;
    RadioButton privateAccountRadio, publicAccountRadio ,privateProfilePictureRadio, publicProfilePictureRadio;
    RadioButton purpleColorRadio , greenColorRadio;
    TextView editProfile, changePassword, changeEmail, changeMobile , userMobile, userMail;
    String themeColor;
    boolean isProfileImagePublic , isAccountPublic;



    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        saveSettings = (TextView) findViewById(R.id.saveSettings);
        editProfile = (TextView) findViewById(R.id.editProfileLabel);
        changePassword = (TextView) findViewById(R.id.changePasswordLabel);
        changeEmail = (TextView) findViewById(R.id.changeEmailLabel);
        changeMobile = (TextView) findViewById(R.id.changeMobileLabel);
        userMail = (TextView) findViewById(R.id.userMail);
        userMobile = (TextView) findViewById(R.id.userMobile);
        purpleColorBtn = (CircleButton) findViewById(R.id.purpleColorBtn);
        purpleColorRadio = (RadioButton) findViewById(R.id.purpleColorRadio);
        greenColorBtn = (CircleButton) findViewById(R.id.greenColorBtn);
        greenColorRadio = (RadioButton) findViewById(R.id.greenColorRadio);
        publicAccountRadio = (RadioButton) findViewById(R.id.publicAccountRadio);
        privateAccountRadio = (RadioButton) findViewById(R.id.privateAccountRadio);
        privateProfilePictureRadio= (RadioButton) findViewById(R.id.privateProfilePictureRadio);
        publicProfilePictureRadio= (RadioButton) findViewById(R.id.publicProfilePictureRadio);


        userMail.setText(GeneralAppInfo.getGeneralUserInfo().getUser().getEmail());
        userMobile.setText(GeneralAppInfo.getGeneralUserInfo().getUser().getMobile());
        if(GeneralAppInfo.getGeneralUserInfo().getUser().getIsPublic())
        {
            publicAccountRadio.setChecked(true);
        }
        else
        {
            privateAccountRadio.setChecked(true);
        }

        if(GeneralAppInfo.getGeneralUserInfo().getUser().getIsProfileImagePublic())
        {
            publicProfilePictureRadio.setChecked(true);
        }
        else
        {
            privateProfilePictureRadio.setChecked(true);
        }
        if(GeneralAppInfo.getGeneralUserInfo().getUser().getThemeColor().equals("GREEN"))
        {
            greenColorBtn.setVisibility(View.INVISIBLE);
            greenColorRadio.setChecked(true);
            purpleColorBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            purpleColorBtn.setVisibility(View.INVISIBLE);
            purpleColorRadio.setChecked(true);
            greenColorBtn.setVisibility(View.VISIBLE);

        }

        toolbarText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                if (event.getX() <= (toolbarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width() + 40)) {
                    finish();
                    return true;
                }
                return false;
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangeEmailActivity.class);
                startActivity(i);
            }
        });
        changeMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangeMobileActivity.class);
                startActivity(i);
            }
        });
        purpleColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purpleColorBtn.setVisibility(View.INVISIBLE);
                purpleColorRadio.setChecked(true);
                greenColorBtn.setVisibility(View.VISIBLE);

            }
        });

       greenColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greenColorBtn.setVisibility(View.INVISIBLE);
                greenColorRadio.setChecked(true);
                purpleColorBtn.setVisibility(View.VISIBLE);
            }
        });

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(greenColorRadio.isChecked())
                {
                    themeColor="GREEN";
                }
                else
                {
                    themeColor="PURPLE";
                }
                if(privateAccountRadio.isChecked())
                {
                    isAccountPublic= false;
                }
                else
                {
                    isAccountPublic= true;
                }
                if(publicProfilePictureRadio.isChecked())
                {
                    isProfileImagePublic=true;
                }
                else
                {
                    isProfileImagePublic=false;
                }

                saveSettings();
            }
        });

    }

    public void saveSettings(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AccountInfoInterface service = retrofit.create(AccountInfoInterface.class);
        final EditPrivacyModel editPrivacy = new EditPrivacyModel();
        editPrivacy.setId(GeneralAppInfo.getUserID());
        editPrivacy.setThemeColor(themeColor);
        editPrivacy.setIsProfileImagePublic(isProfileImagePublic);
        editPrivacy.setIsPublic(isAccountPublic);

        final Call<GeneralUserInfoModel> editPrivacyCall = service.editPrivacy(editPrivacy);
        editPrivacyCall.enqueue(new Callback<GeneralUserInfoModel>() {
            @Override
            public void onResponse(Call<GeneralUserInfoModel> call, Response<GeneralUserInfoModel> response) {
                if (response.code() == 200) {
                    GeneralUserInfoModel generalUserInfo = response.body();
                    UserModel userModel = generalUserInfo.getUser();
                    GeneralAppInfo.setGeneralUserInfo(generalUserInfo);
                    Log.d("EditPrivacy", userModel.getThemeColor() + " " +userModel.getIsPublic() + " " +userModel.getIsProfileImagePublic() );
                    GeneralAppInfo.setUserID(userModel.getId());
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(generalUserInfo);
                    editor.putString("generalUserInfo", json);
                    Log.d("EditPrivacy","  " + json);
                    editor.apply();
                    finish();
                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<GeneralUserInfoModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("EditPrivacy", " Error " + t.getMessage());
            }
        });
    }

}
