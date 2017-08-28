package com.example.zodiac.sawa.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewAdapters.RequestScroll;
import com.example.zodiac.sawa.SpringModels.FriendResponseModel;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by raghadq on 5/2/2017.
 */

/**
 * Created by zodiac on 04/03/2017.
 */


public class MyRequestsActivity extends Activity {

    public static List<UserModel> FreindsList;
    public static ArrayList<friend> LayoutFriendsList = new ArrayList<>();
    public static FastScrollRecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    FriendshipInterface friendshipApi;
    TextView toolbarText;

    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request_tab);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        friendshipApi = retrofit.create(FriendshipInterface.class);

        toolbarText = (TextView) findViewById(R.id.toolBarText);
        final LinearLayout noRequestsLaout = (LinearLayout) findViewById(R.id.no_request_Layout);
        final ProgressBar progressBar;
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        ObjectAnimator anim;
        anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        adapter = new RequestScroll(this, LayoutFriendsList);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        GeneralFunctions generalFunctions = new GeneralFunctions();
        boolean isOnline = generalFunctions.isOnline(getApplicationContext());


        if (isOnline == false) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();

        } else {
            LayoutFriendsList.clear();

            final Call<List<UserModel>> FriendsResponse = friendshipApi.getFollowRequest(GeneralAppInfo.getUserID());
            FriendsResponse.enqueue(new Callback<List<UserModel>>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("GetFriendRequests", " Get friends " + response.code() + LayoutFriendsList.size());
                    if (response.code() == 200) {
                        FreindsList = response.body();
                        //    LayoutFriendsList.clear();
                        if (FreindsList != null) {

                            if (FreindsList.size() == 0) {
                                CircleImageView circle = (CircleImageView) findViewById(R.id.circle);
                                circle.setImageDrawable(getDrawable(R.drawable.no_requests));
                                noRequestsLaout.setVisibility(View.VISIBLE);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                noRequestsLaout.setVisibility(View.GONE);
                                LayoutFriendsList.clear();

                                for (int i = 0; i < FreindsList.size(); i++) {
                                    LayoutFriendsList.add(new MyRequestsActivity.friend(FreindsList.get(i).getId(), FreindsList.get(i).getImage(),
                                            FreindsList.get(i).getFirst_name() + " " + FreindsList.get(i).getLast_name()));
                                }

                            }
                        }
                    } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                        progressBar.setVisibility(View.INVISIBLE);

                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<List<UserModel>> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                    Log.d("fail to get friends ", "Failure to Get friends");

                }
            });
        }
        toolbarText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;

                if (event.getX() <= (toolbarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width() + 30)) {
                    finish();
                    return true;
                }
                return false;
            }
        });

    }


    public class friend {

        int Id;
        String imageResourceId;
        String userName;

        public friend(int Id, String imageResourceId, String userName) {
            setImageResourceId(imageResourceId);
            setId(Id);
            setUserName(userName);
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getImageResourceId() throws MalformedURLException {
            return imageResourceId;
        }

        public void setImageResourceId(String imageResourceId) {
            this.imageResourceId = imageResourceId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }

}

