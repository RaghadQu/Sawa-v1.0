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

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewAdapters.FastScrollAdapter;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringModels.FollowesAndFollowingResponse;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zodiac on 08/21/2017.
 */

public class MyFollowingActivity extends Activity {

    public static List<FollowesAndFollowingResponse> FriendList;
    public static ArrayList<MyFollowersActivity.friend> LayoutFriendsList = new ArrayList<>();
    public static FastScrollRecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    FriendshipInterface friendshipApi;
    TextView toolbarText , FriendshipTypeLabel;

    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_tab);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        friendshipApi = retrofit.create(FriendshipInterface.class);
        FriendshipTypeLabel = (TextView) findViewById(R.id.FriendshipTabType);
        FriendshipTypeLabel.setText("Following");
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        final ProgressBar progressBar;
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        ObjectAnimator anim;
        anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();


        adapter = new FastScrollAdapter(this, LayoutFriendsList, 0);
        final LinearLayout noFriendsLayout = (LinearLayout) findViewById(R.id.no_friends_Layout);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        final Call<List<FollowesAndFollowingResponse>> FriendsResponse = friendshipApi.getFollowing(GeneralAppInfo.getUserID());
        FriendsResponse.enqueue(new Callback<List<FollowesAndFollowingResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<List<FollowesAndFollowingResponse>> call, Response<List<FollowesAndFollowingResponse>> response) {
                progressBar.setVisibility(View.INVISIBLE);

                Log.d("GetFriends", " Get friends " + response.code());
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                    FriendList = response.body();
                    LayoutFriendsList.clear();
                    if (FriendList != null) {

                        if (FriendList.size() == 0) {

                            noFriendsLayout.setVisibility(View.VISIBLE);
                            CircleImageView circle = (CircleImageView) findViewById(R.id.circle);
                            TextView textBody = ( TextView) findViewById(R.id.TextBody);
                            circle.setImageDrawable(getDrawable(R.drawable.no_friends));
                            textBody.setText("   No Following To Show");

                        } else {
                            Log.d("GetFriends", " Friends are : " + FriendList.size());
                            progressBar.setVisibility(View.GONE);
                            noFriendsLayout.setVisibility(View.GONE);
                            for (int i = 0; i < FriendList.size(); i++) {
                                LayoutFriendsList.add(new MyFollowersActivity.friend(FriendList.get(i).getUser().getId(), FriendList.get(i).getUser().getImage(),
                                        FriendList.get(i).getUser().getFirst_name() + " " + FriendList.get(i).getUser().getLast_name(),FriendList.get(i).getState()));
                            }
                            recyclerView.setAdapter(new FastScrollAdapter(MyFollowingActivity.this, LayoutFriendsList, 2));

                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<List<FollowesAndFollowingResponse>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("fail to get friends ", "Failure to Get friends");

            }
        });

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



}

