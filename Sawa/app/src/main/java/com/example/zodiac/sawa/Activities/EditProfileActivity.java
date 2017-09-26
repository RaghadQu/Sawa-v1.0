package com.example.zodiac.sawa.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.AboutUserInterface;
import com.example.zodiac.sawa.SpringModels.EditProfileModel;
import com.example.zodiac.sawa.SpringModels.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends Activity {

    TextView backEdit, saveEdit;
    EditText firstName, lastName;
    RadioButton maleBtn, femaleBtn;
    DatePicker birthDate;
    AboutUserInterface service;
    UserModel userModel;
    EditProfileModel editProfileModle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);
        backEdit = (TextView) findViewById(R.id.backEdit);
        saveEdit = (TextView) findViewById(R.id.saveEdit);
        firstName = (EditText) findViewById(R.id.FirstName);
        lastName = (EditText) findViewById(R.id.LastName);
        maleBtn = (RadioButton) findViewById(R.id.radioM);
        femaleBtn = (RadioButton) findViewById(R.id.radioF);
        birthDate = (DatePicker) findViewById(R.id.BirthDatePicker);

        FillUserInfo();

        backEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileModle = new EditProfileModel();
                String stringDate = String.valueOf(birthDate.getYear()) + "-" + String.valueOf(birthDate.getMonth() + 1) + "-" + String.valueOf(birthDate.getDayOfMonth());
                String gender = "";
                if (maleBtn.isChecked()) gender = "male";
                if (femaleBtn.isChecked()) gender = "female";
                editProfileModle.setId(GeneralAppInfo.getUserID());
                editProfileModle.setFirst_name(firstName.getText().toString());
                editProfileModle.setLast_name(lastName.getText().toString());
                editProfileModle.setBirthdate(stringDate);
                Log.d("stringDate", stringDate);
                Log.d("stringDate", GeneralAppInfo.getUserID() + "");
                editProfileModle.setGender(gender);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GeneralAppInfo.SPRING_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                AboutUserInterface aboutUserApi = retrofit.create(AboutUserInterface.class);

                Call<Integer> call = aboutUserApi.updateProfile(editProfileModle);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                            GeneralFunctions generalFunctions = new GeneralFunctions();
                            generalFunctions.showErrorMesaage(getApplicationContext());
                        }
                        Log.d("AboutProfileUpdate", "Done successfully " + response.code() + " " + response.body());
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                        Log.d("AboutProfileUpdate", "Failure " + t.getMessage());
                    }
                });
            }
        });
    }

    public void FillUserInfo() {

                    String userBirthdate="";
                    userModel = GeneralAppInfo.generalUserInfo.getUser();
                    firstName.setText(userModel.getFirst_name());
                    lastName.setText(userModel.getLast_name());
                    if (userModel.getGender().equals("female"))
                        femaleBtn.setChecked(true);
                    else if (userModel.getGender().equals("male"))
                        maleBtn.setChecked(true);
                    //When the user doesnt has birthdate
                    //like sign up useing gmail or faceook
                    if (userModel.getBirthdate()==null) {
                        userBirthdate = "1990-01-01";
                    } else
                        userBirthdate = userModel.getBirthdate();
                    String[] separated = userBirthdate.split("-");
                    Log.d("Birthdate", " String is  " + userBirthdate);
                    String year = separated[0];
                    String month = separated[1];
                    String day = separated[2];
                    birthDate.updateDate(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));


    }
}
