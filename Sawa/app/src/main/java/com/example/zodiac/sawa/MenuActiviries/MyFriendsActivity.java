package com.example.zodiac.sawa.MenuActiviries;

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
import com.example.zodiac.sawa.Spring.Models.FriendResponseModel;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
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


public class MyFriendsActivity extends Activity {


    public static List<FriendResponseModel> FreindsList;
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
        setContentView(R.layout.friends_tab);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        friendshipApi = retrofit.create(FriendshipInterface.class);
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

        final Call<List<FriendResponseModel>> FriendsResponse = friendshipApi.getUserFriends(GeneralAppInfo.getUserID());
        FriendsResponse.enqueue(new Callback<List<FriendResponseModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<List<FriendResponseModel>> call, Response<List<FriendResponseModel>> response) {
                progressBar.setVisibility(View.INVISIBLE);

                Log.d("GetFriends", " Get friends " + response.code());
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                    FreindsList = response.body();
                    LayoutFriendsList.clear();
                    if (FreindsList != null) {

                        if (FreindsList.size() == 0) {

                            noFriendsLayout.setVisibility(View.VISIBLE);
                            CircleImageView circle = (CircleImageView) findViewById(R.id.circle);
                            circle.setImageDrawable(getDrawable(R.drawable.no_friends));

                        } else {
                            Log.d("GetFriends", " Friends are : " + FreindsList.size());
                            progressBar.setVisibility(View.GONE);
                            noFriendsLayout.setVisibility(View.GONE);
                            for (int i = 0; i < FreindsList.size(); i++) {
                                if (FreindsList.get(i).getFriend2_id().getId() == GeneralAppInfo.getUserID()) {
                                    LayoutFriendsList.add(new friend(FreindsList.get(i).getFriend1_id().getId(), FreindsList.get(i).getFriend1_id().getImage(),
                                            FreindsList.get(i).getFriend1_id().getFirst_name() + " " + FreindsList.get(i).getFriend1_id().getLast_name()));
                                } else {
                                    LayoutFriendsList.add(new friend(FreindsList.get(i).getFriend2_id().getId(), FreindsList.get(i).getFriend2_id().getImage(),
                                            FreindsList.get(i).getFriend2_id().getFirst_name() + " " + FreindsList.get(i).getFriend2_id().getLast_name()));
                                }
                                recyclerView.setAdapter(new FastScrollAdapter(MyFriendsActivity.this, LayoutFriendsList, 0));

                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FriendResponseModel>> call, Throwable t) {
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


    public static class friend {
        int Id;
        String imageResourceId;
        String userName;

        public friend(int Id, String imageResourceId, String userName) {
            setImageResourceId(imageResourceId);
            setUserName(userName);
            setId(Id);
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

