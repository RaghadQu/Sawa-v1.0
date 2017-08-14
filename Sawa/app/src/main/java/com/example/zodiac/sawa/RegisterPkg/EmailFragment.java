package com.example.zodiac.sawa.RegisterPkg;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Spring.Models.SignInModel;
import com.example.zodiac.sawa.Spring.Models.UserModel;
import com.example.zodiac.sawa.SpringApi.AuthInterface;
import com.example.zodiac.sawa.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zodiac on 07/10/2017.
 */

public class EmailFragment extends android.app.Fragment {

    Button nextbtn;
    EditText email;
    AuthInterface service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //   imgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
        View view = inflater.inflate(R.layout.register_email_fragment, container, false);
        email = (EditText) view.findViewById(R.id.userEmail);
        email.setFocusable(true);
        nextbtn = (Button) view.findViewById(R.id.nextBtn);
        final ProgressBar checkEmailProgress = (ProgressBar) view.findViewById(R.id.checkEmailProgress);

        nextbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                if ((userEmail.equals(""))) {
                    email.setError("Email is required");
                } else if (!(Validation.isEmailValid(email.getText().toString()))) {
                    email.setError("Email is not valid");
                } else {
                    checkEmailProgress.setVisibility(View.VISIBLE);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GeneralAppInfo.SPRING_URL)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    service = retrofit.create(AuthInterface.class);
                    final SignInModel signInModel = new SignInModel();
                    signInModel.setEmail(email.getText().toString());
                    signInModel.setPassword("@(-_-)@");
                    final Call<UserModel> userModelCall = service.signIn(signInModel);
                    userModelCall.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            checkEmailProgress.setVisibility(View.INVISIBLE);

                            Log.d("EmailChecker", " " + response.code());
                            if (response.code() == 204) {
                                ((RegisterActivity) getActivity()).setUserEmail(email.getText().toString());
                                android.app.Fragment f = new MobileFragment();
                                ((RegisterActivity) getActivity()).replaceFragmnets(f);
                            } else if (response.code() == 409 || response.code() == 200) {
                                email.setError("Email is already used!");
                            } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                                GeneralFunctions generalFunctions = new GeneralFunctions();
                                generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            GeneralFunctions generalFunctions = new GeneralFunctions();
                            generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
                            checkEmailProgress.setVisibility(View.INVISIBLE);
                            Log.d("Email.Register", " Error " + t.getMessage());
                        }
                    });
                }


            }
        });
        return view;
    }

}
