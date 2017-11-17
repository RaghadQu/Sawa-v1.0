package com.example.zodiac.sawa.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.zodiac.sawa.RecyclerViewAdapters.FastScrollAdapter;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringModels.FollowesAndFollowingResponse;
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

    public class MyFollowersActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {


        public static List<FollowesAndFollowingResponse> FriendsList;
        public static List<FollowesAndFollowingResponse> otherFriendList;
        public static ArrayList<friend> LayoutFriendsList = new ArrayList<>();
        public static FastScrollRecyclerView recyclerView;
        public static RecyclerView.Adapter adapter;
        LinearLayout noFriendsLayout;
        FriendshipInterface friendshipApi;
        TextView toolbarText, FriendshipTypeLabel;
        LinearLayout noFollowersLayout;
        SwipeRefreshLayout swipeRefreshLayout;
        ProgressBar progressBar;
        int source = 0;
        int friendId;


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
            toolbarText = (TextView) findViewById(R.id.toolBarText);
            FriendshipTypeLabel = (TextView) findViewById(R.id.FriendshipTabType);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

            swipeRefreshLayout.setOnRefreshListener(this);
            //source var to indicate if we need to list our followers or other following or followers
            //0 source ->our followers
            //1 source ->indicate others followers
            //2 source indicate ->others following
            // or other values

            Bundle b = getIntent().getExtras();
            friendId = -1;
            String friendName = "";
            if (b != null) {
                source = b.getInt("source");

                if (source == 1 || source == 2) {
                    Log.d("source: ", "" + source);
                    friendId = b.getInt("friendId");
                    friendName = b.getString("friendName");
                    if (source == 1) {
                        FriendshipTypeLabel.setText(friendName + " Followers");
                    } else {
                        FriendshipTypeLabel.setText(friendName + " Following");

                    }
                } else {
                    FriendshipTypeLabel.setText("Followers");

                }
            }
            friendshipApi = retrofit.create(FriendshipInterface.class);
            noFollowersLayout = (LinearLayout) findViewById(R.id.no_friends_Layout);
            progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            ObjectAnimator anim;
            anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
            anim.setDuration(2000);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.start();

            adapter = new FastScrollAdapter(this, LayoutFriendsList, 0);
            noFriendsLayout = (LinearLayout) findViewById(R.id.no_friends_Layout);
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
                getFollowerView();
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

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                getFollowerView();

            }
        }, 2000);
    }

    public void handleDateInAdapter(ProgressBar progressBar, LinearLayout noFriendsLayout, int source,List<FollowesAndFollowingResponse> FriendsList) {
        int id=-1;
        String image="";
        String first_name="";
        String last_name="";
        int state=-1;

        if (FriendsList != null) {
            if (FriendsList.size() == 0) {

                noFriendsLayout.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                noFriendsLayout.setVisibility(View.GONE);
                for (int i = 0; i < FriendsList.size(); i++) {
                    // if(source==0){
                    id=FriendsList.get(i).getUser().getId();
                    image=FriendsList.get(i).getUser().getImage();
                    first_name=FriendsList.get(i).getUser().getFirst_name();
                    last_name=FriendsList.get(i).getUser().getLast_name();
                    state=FriendsList.get(i).getState();

                    LayoutFriendsList.add(new MyFollowersActivity.friend(id, image,(first_name + " " + last_name), state));
                }
                recyclerView.setAdapter(new FastScrollAdapter(MyFollowersActivity.this, LayoutFriendsList, 2));

            }
        }
    }

    public static class friend {
        int Id;
        String imageResourceId;
        String userName;
        int state;

        public friend(int Id, String imageResourceId, String userName , int state) {
            setImageResourceId(imageResourceId);
            setUserName(userName);
            setId(Id);
            setState(state);
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


        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }


    }
     public void getFollowerView(){
         if (source == 0) {


             final Call<List<FollowesAndFollowingResponse>> FriendsResponse = friendshipApi.getFollowers(GeneralAppInfo.getUserID());
             final int finalSource = source;
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

                         FriendsList = response.body();
                         otherFriendList = null;
                         LayoutFriendsList.clear();
                         if (FriendsList != null) {
                             if (FriendsList.size() == 0) {
                                 CircleImageView circle = (CircleImageView) findViewById(R.id.circle);
                                 circle.setImageDrawable(getDrawable(R.drawable.no_friends));
                                 Log.d("MyFollowerActivity", "size 0 layout set");
                                 noFollowersLayout.setVisibility(View.VISIBLE);
                             } else {
                                 handleDateInAdapter(progressBar, noFriendsLayout, finalSource, FriendsList);
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
         } else if (source == 1 || source == 2) {
             Call<List<FollowesAndFollowingResponse>> FriendsResponse = friendshipApi.getOtherFollowers(friendId, GeneralAppInfo.getUserID());

             if (source == 2) {
                 FriendsResponse = friendshipApi.getOtherFollowing(friendId, GeneralAppInfo.getUserID());
                 Log.d("otherFollowing", " Response " + 2);

             }
             final int finalSource1 = source;
             FriendsResponse.enqueue(new Callback<List<FollowesAndFollowingResponse>>() {
                 @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                 @Override
                 public void onResponse(Call<List<FollowesAndFollowingResponse>> call, Response<List<FollowesAndFollowingResponse>> response) {
                     progressBar.setVisibility(View.INVISIBLE);
                     Log.d("otherFollowing", " Response " + response.code());

                     Log.d("GetFriends", " Get friends " + response.code());
                     if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                         GeneralFunctions generalFunctions = new GeneralFunctions();
                         generalFunctions.showErrorMesaage(getApplicationContext());
                     } else {


                         otherFriendList = response.body();

                         FriendsList = null;
                         LayoutFriendsList.clear();
                         if (otherFriendList != null) {
                             Log.d("otherFollowing", " Response  not null");

                             if (otherFriendList.size() == 0) {
                                 Log.d("otherFollowing", " Response  zero");
                                 CircleImageView circle = (CircleImageView) findViewById(R.id.circle);
                                 circle.setImageDrawable(getDrawable(R.drawable.no_requests));
                                 noFriendsLayout.setVisibility(View.VISIBLE);
                             } else {
                                 Log.d("otherFollowing", " Response not  zero");

                                 handleDateInAdapter(progressBar, noFriendsLayout, finalSource1, otherFriendList);
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
         }
         }
}

