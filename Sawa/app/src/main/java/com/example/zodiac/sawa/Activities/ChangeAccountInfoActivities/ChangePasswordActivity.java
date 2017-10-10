package com.example.zodiac.sawa.Activities.ChangeAccountInfoActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.AccountInfoInterface;
import com.example.zodiac.sawa.SpringModels.EditPasswordModel;
import com.example.zodiac.sawa.SpringModels.GeneralUserInfoModel;
import com.example.zodiac.sawa.SpringModels.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zodiac on 10/08/2017.
 */


public class ChangePasswordActivity extends Activity {

    TextView toolbarText;
    Button saveNewEmail;
    EditText currentPasswordText, newPasswordText, confirmPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_password_activity);

        toolbarText = (TextView) findViewById(R.id.toolBarText);
        saveNewEmail = (Button) findViewById(R.id.saveNewPassword);
        currentPasswordText = (EditText) findViewById(R.id.currentPassword);
        newPasswordText = (EditText) findViewById(R.id.newPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);


        saveNewEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!(confirmPassword.getText().toString().equals(newPasswordText.getText().toString()))) {
                    Toast.makeText(ChangePasswordActivity.this, "New and confirm passwords are not the same",
                            Toast.LENGTH_SHORT).show();

                } else {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GeneralAppInfo.SPRING_URL)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    AccountInfoInterface service = retrofit.create(AccountInfoInterface.class);
                    final EditPasswordModel editPassword = new EditPasswordModel();
                    editPassword.setId(GeneralAppInfo.getUserID());
                    editPassword.setOld_password(currentPasswordText.getText().toString());
                    editPassword.setNew_password(newPasswordText.getText().toString());

                    final Call<GeneralUserInfoModel> editPasswordCall = service.editPassword(editPassword);
                    editPasswordCall.enqueue(new Callback<GeneralUserInfoModel>() {
                        @Override
                        public void onResponse(Call<GeneralUserInfoModel> call, Response<GeneralUserInfoModel> response) {
                            if (response.code() == 200) {
                                GeneralUserInfoModel generalUserInfo = response.body();
                                UserModel userModel = generalUserInfo.getUser();
                                GeneralAppInfo.setUserID(userModel.getId());
                                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("password", newPasswordText.getText().toString());
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
                            Log.d("ChangePassword", " Error " + t.getMessage());
                        }
                    });
                }
            }
        });


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
    }
}
