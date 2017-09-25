package com.example.zodiac.sawa.Activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecoverPassword.RecoverPass;
import com.example.zodiac.sawa.RegisterPkg.RegisterActivity;
import com.example.zodiac.sawa.SpringApi.AuthInterface;
import com.example.zodiac.sawa.SpringModels.LoginWIthGoogleModel;
import com.example.zodiac.sawa.SpringModels.LoginWithFacebookModel;
import com.example.zodiac.sawa.SpringModels.SignInModel;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.example.zodiac.sawa.SpringModels.GeneralUserInfoModel;
import com.example.zodiac.sawa.SpringModels.LoginWIthGoogleModel;
import com.example.zodiac.sawa.SpringModels.LoginWithFacebookModel;
import com.example.zodiac.sawa.SpringModels.SignInModel;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    static Dialog LoggingInDialog;
    LoginButton loginButton;
    AuthInterface service;
    CallbackManager callbackManager;
    SignInButton signInButton;
    CircleImageView fb, google;
    GoogleApiClient googleApiClient;
    Dialog progressDialog;
    TextView AppTitle;
    private EditText emailEditText;
    private EditText passEditText;
    SharedPreferences sharedPreferences ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.gc();
        sharedPreferences= getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        GeneralFunctions generalFunctions = new GeneralFunctions();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(findViewById(R.id.login_button));
        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .repeat(0)
                .playOn(findViewById(R.id.loginWithGoogleBtn));
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id)).
                        requestServerAuthCode(getString(R.string.default_web_client_id)).requestScopes(new Scope(Scopes.PLUS_LOGIN)).build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).addApi(Plus.API)
                .build();

        signInButton = (SignInButton) findViewById(R.id.loginWithGoogleBtn);
        signInButton.setOnClickListener(this);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(signInButton, "Log in with google ");
        fb = (CircleImageView) findViewById(R.id.fb);
        google = (CircleImageView) findViewById(R.id.google);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        LoggingInDialog = new Dialog(this);
        LoggingInDialog.setContentView(R.layout.logging_in_dialog);
        progressDialog = new Dialog(MainActivity.this);
        progressDialog.setContentView(R.layout.facebook_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//Login with face book

        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                Log.d("Facebook user id ", "" + loginResult.getAccessToken().getUserId());
                Log.d("Facebook token ", "" + loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        // Bundle bFacebookData = getFacebookData(object);
                        try {

                            LoginWithFacebookModel loginWithFacebookModel = new LoginWithFacebookModel();
                            loginWithFacebookModel.setEmail(object.getString("email"));
                            loginWithFacebookModel.setFirstName(object.getString("first_name"));
                            loginWithFacebookModel.setLastName(object.getString("last_name"));
                            //  loginWithFacebookModel.(object.getString("gender"));
                            loginWithFacebookModel.setId(loginResult.getAccessToken().getUserId());
                            loginWithFacebookModel.setAccessToken(loginResult.getAccessToken().getToken());
                            loginWithFacebookModel.setImage("");
                            loginWithFacebookModel.setGender(object.getString("gender"));
                            loginWithFacebook(loginWithFacebookModel);
                            Log.d("Facebook email", "" + object.getString("email"));
                            Log.d("Facebook email", "" + object.getString("first_name"));
                            Log.d("Facebook gender", "" + object.getString("gender"));
                            //  Log.d("Facebook gender", "" + object.getString("user_birthday"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("Facebook token ", "Canceld");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOK", "facebook " + error.getMessage());
            }
        });
        //Sign in with google section

//        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile()
//                .requestIdToken(getString(R.string.default_web_client_id)).
//                        requestServerAuthCode(getString(R.string.default_web_client_id)).requestScopes(new Scope(Scopes.PLUS_LOGIN)).build();

        //end section

        //check if the user is already signed in
        int id = sharedPreferences.getInt("id", -1);
        String isLogined = sharedPreferences.getString("isLogined", "");
        GeneralAppInfo.setUserID(id);

        if ((isLogined.equals("1"))) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("generalUserInfo","");
            GeneralAppInfo.setGeneralUserInfo(gson.fromJson(json, GeneralUserInfoModel.class));
            Log.d("Logged",GeneralAppInfo.getGeneralUserInfo()+ " " +json);
            Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
            startActivity(i);
            finish();
        }

        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(AuthInterface.class);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void checkLogin(View arg0) {

        final GeneralFunctions generalFunctions = new GeneralFunctions();
        final boolean isOnline = generalFunctions.isOnline(getApplicationContext());
       if (isOnline == false) {
             Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();
        } else {
            final SignInModel signInModel = new SignInModel();
            signInModel.setEmail(emailEditText.getText().toString());
            signInModel.setPassword(passEditText.getText().toString());
            if (valid(signInModel.getEmail(), signInModel.getPassword()) == 0) {
                LoggingInDialog.show();

                final Call<GeneralUserInfoModel> userModelCall = service.signIn(signInModel);
                userModelCall.enqueue(new Callback<GeneralUserInfoModel>() {
                    @Override
                    public void onResponse(Call<GeneralUserInfoModel> call, Response<GeneralUserInfoModel> response) {


                        int statusCode = response.code();
                        Log.d("-----", " enter request " + statusCode);
                        GeneralUserInfoModel generalUserInfoModel = response.body();
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                        if (statusCode == 200) {

                            GeneralAppInfo.setUserID(Integer.valueOf(generalUserInfoModel.getUser().getId()));
                            GeneralUserInfoModel generalUserModel = response.body();

                            if (statusCode == 200) {

                                GeneralAppInfo.setGeneralUserInfo(generalUserModel);
                                GeneralAppInfo.setUserID(Integer.valueOf(generalUserModel.getUser().getId()));
                                sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(generalUserModel);
                                editor.putString("generalUserInfo", json);
                                editor.putString("email", emailEditText.getText().toString());
                                editor.putString("password", passEditText.getText().toString());
                                editor.putString("profileImage", generalUserInfoModel.getUser().getImage());
                                editor.putString("coverImage", generalUserInfoModel.getUser().getCover_image());

                                editor.putInt("id", GeneralAppInfo.getUserID());
                                editor.putString("isLogined", "1");
                                editor.apply();
                                Log.d("Logged", " sign in " + json);

                                Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                                LoggingInDialog.dismiss();
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                finish();


                            } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {

                                LoggingInDialog.dismiss();
                                generalFunctions.showErrorMesaage(getApplicationContext());
                            } else {
                                LoggingInDialog.dismiss();
                                YoYo.with(Techniques.Shake)
                                        .duration(700)
                                        .repeat(0)
                                        .playOn(findViewById(R.id.username));
                                YoYo.with(Techniques.Shake)
                                        .duration(700)
                                        .repeat(0)
                                        .playOn(findViewById(R.id.password));
                                emailEditText.setError("Invalid Email or Password");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralUserInfoModel> call, Throwable t) {
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                        LoggingInDialog.dismiss();
                        if (isOnline == false) {
                            Toast.makeText(MainActivity.this, "no internet connection!",
                                    Toast.LENGTH_LONG).show();
                        }
                        Log.d("----", " Error " + t.getMessage());


                    }
                });
            }
        }
    }


    public void forgot_pass(View arg0) {
        Intent i = new Intent(getApplicationContext(), RecoverPass.class);
        startActivity(i);

    }

    public void register(View arg0) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);

    }


    public int valid(String email, String password) {
        int flag = 0;
        if ((password.trim().equals(""))) {
            passEditText.setError("Password is required");
            flag = 1;
        } else if (password.length() < 8) {
            passEditText.setError("Invalid password");
            flag = 1;
        }
        if ((email.trim().equals(""))) {
            emailEditText.setError("Email is required");
            flag = 1;
        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            Pattern p = Pattern.compile(emailPattern);
            Matcher m = p.matcher(email);
            boolean b = m.matches();
            if (!b) {
                emailEditText.setError("Invalid email");
                flag = 1;
            }
        }
        return flag;
    }

    public void onBackPressed() {
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {


        if (requestCode == 9001) {
            // data.getStringExtra("")

            LoggingInDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
            if (googleApiClient.hasConnectedApi(Plus.API)) {
                LoggingInDialog.show();

                com.google.android.gms.plus.model.people.Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                Log.i("", "Gender: " + person.getGender());
            }

            com.google.android.gms.plus.model.people.Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            Log.i("", "Gender: " + person.getGender());
        } else {
            progressDialog.dismiss();
            callbackManager.onActivityResult(requestCode, responseCode, data);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginWithGoogleBtn:
                signIn();
                break;
        }
        if (v == fb) {
            progressDialog.show();
            loginButton.performClick();
        }
        if (v == google) {
            signInButton.performClick();
            signIn();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("FACEBOOKFACEOOK", "facebook  failre onActivityResult");


    }

    public void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, 9001);
    }

    public void handleResult(GoogleSignInResult googleSignInResult) {

        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            String email = account.getEmail();
            String userId = account.getId();
            LoginWIthGoogleModel loginWIthGoogleModel = new LoginWIthGoogleModel();
            loginWIthGoogleModel.setEmail(email);
            loginWIthGoogleModel.setAccessToken(account.getIdToken());
            loginWIthGoogleModel.setFirstName(account.getGivenName());
            loginWIthGoogleModel.setLastName(account.getFamilyName());
            loginWIthGoogleModel.setGender("male");
            loginWIthGoogleModel.setId(userId);
            loginWIthGoogleModel.setImage(account.getPhotoUrl().toString());
            loginWithGoogle(loginWIthGoogleModel);
            Log.d("account.photo", account.getPhotoUrl().toString());
            Log.d("Google email", email);
            Log.d("account.getIdToken();", account.getIdToken());
            Log.d("account.getIdToken();", account.getServerAuthCode());


        }
    }

    public void loginWithGoogle(LoginWIthGoogleModel loginWIthGoogleModel) {

       // final SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        final Call<GeneralUserInfoModel> userModelCall = service.loginWithGoogle(loginWIthGoogleModel);
        userModelCall.enqueue(new Callback<GeneralUserInfoModel>() {
            @Override
            public void onResponse(Call<GeneralUserInfoModel> call, Response<GeneralUserInfoModel> response) {


                int statusCode = response.code();
                GeneralUserInfoModel generalUserModel = response.body();
                //SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                if (statusCode == 200 || statusCode == 202) {

                    GeneralAppInfo.setUserID(Integer.valueOf(generalUserModel.getUser().getId()));
                    GeneralAppInfo.setGeneralUserInfo(generalUserModel);
                    sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(generalUserModel);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("generalUserInfo", json);
                    editor.putString("email", emailEditText.getText().toString());
                    editor.putString("password", passEditText.getText().toString());
                    editor.putInt("id", GeneralAppInfo.getUserID());
                    editor.putString("isLogined", "1");
                    editor.apply();
                    LoggingInDialog.dismiss();

                    Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    finish();

                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());

                } else {
                    emailEditText.setError("Invalid Email or Password");
                }


            }

            @Override
            public void onFailure(Call<GeneralUserInfoModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("----", " Error " + t.getMessage());


            }
        });

    }

    public void loginWithFacebook(LoginWithFacebookModel loginWithFacebookModel) {

      //  final SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        final Call<GeneralUserInfoModel> userModelCall = service.loginWithFacebook(loginWithFacebookModel);
        userModelCall.enqueue(new Callback<GeneralUserInfoModel>() {
            @Override
            public void onResponse(Call<GeneralUserInfoModel> call, Response<GeneralUserInfoModel> response) {
                int statusCode = response.code();
                GeneralUserInfoModel generalUserModel = response.body();

                if (statusCode == 200 || statusCode == 202) {
                    GeneralAppInfo.setUserID(Integer.valueOf(generalUserModel.getUser().getId()));
                    GeneralAppInfo.setGeneralUserInfo(generalUserModel);
                    sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(generalUserModel);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("generalUserInfo", json);
                    editor.putString("email", emailEditText.getText().toString());
                    editor.putString("password", passEditText.getText().toString());
                    editor.putInt("id", GeneralAppInfo.getUserID());
                    editor.putString("isLogined", "1");
                    editor.apply();

                    Intent i = new Intent(getApplicationContext(), HomeTabbedActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    finish();

                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {
                    emailEditText.setError("Invalid Email or Password");
                }


            }

            @Override
            public void onFailure(Call<GeneralUserInfoModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());


            }
        });

    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text

        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }


}


