package com.example.zodiac.sawa.RecoverPassword;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringModels.SignInModel;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.example.zodiac.sawa.SpringApi.AuthInterface;
import com.example.zodiac.sawa.Services.ValidationService;
import com.example.zodiac.sawa.Services.EmailSenderService.BackgroungSender;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendEmailFragment extends android.app.Fragment {

    EditText recievedEmail;
    Button btn;
    String uniqueID;
    AuthInterface service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        View view = inflater.inflate(R.layout.fragment_send_email, container, false);
        recievedEmail = (EditText) view.findViewById(R.id.userEmail);
        uniqueID = UUID.randomUUID().toString();
        uniqueID = uniqueID.split("-")[0];
        btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (recievedEmail.getText().toString().equals("")) {
                    recievedEmail.setError("Email is required");
                } else {
                    if (!(ValidationService.isEmailValid(recievedEmail.getText().toString()))) {
                        recievedEmail.setError("Invalid Email");
                    } else {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(GeneralAppInfo.SPRING_URL)
                                .addConverterFactory(GsonConverterFactory.create()).build();
                        service = retrofit.create(AuthInterface.class);
                        final SignInModel signInModel = new SignInModel();
                        signInModel.setEmail(recievedEmail.getText().toString());
                        signInModel.setPassword("@(-_-)@");
                        final Call<UserModel> userModelCall = service.signIn(signInModel);
                        userModelCall.enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if (response.code() != 204) {
                                    BackgroungSender BS = new BackgroungSender();
                                    BS.setRecievedEmail(recievedEmail.getText().toString());
                                    BS.setUniqueID(uniqueID);
                                    BS.execute("");

                                    ((RecoverPass) getActivity()).setUniqueID(uniqueID);
                                    ((RecoverPass) getActivity()).setEmail(recievedEmail.getText().toString());
                                    android.app.Fragment f = new CheckCodeFragment();
                                    ((RecoverPass) getActivity()).replaceFragmnets(f);

                                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                                    GeneralFunctions generalFunctions = new GeneralFunctions();
                                    generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
                                } else {
                                    recievedEmail.setError("Incorrect Email");
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {
                                GeneralFunctions generalFunctions = new GeneralFunctions();
                                generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
                                Log.d("Email.PassRecover", " Error " + t.getMessage());
                            }
                        });
                    }


                }
            }
        });
        return view;

    }


}
