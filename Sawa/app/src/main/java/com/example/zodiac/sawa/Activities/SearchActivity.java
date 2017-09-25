package com.example.zodiac.sawa.Activities;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewAdapters.FastScrollAdapter;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.example.zodiac.sawa.SpringApi.SearchInterface;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    public static List<UserModel> userModelList;
    public static ArrayList<MyFollowersActivity.friend> LayoutFriendsList = new ArrayList<>();
    public static FastScrollRecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    static SearchView mSearchView;
    SearchInterface searchInterface;
    static  ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
         mSearchView = (SearchView) findViewById(R.id.search);
        //mSearchView.setSelected(true);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);
        LayoutFriendsList.clear();
        //   mSearchView.setFocusable(true);
//        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
//        progressBar.setProgress(0);
//        progressBar.setMax(100);
//
//        ObjectAnimator anim;
//        anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
//        anim.setDuration(2000);
//        anim.setInterpolator(new DecelerateInterpolator());
//        anim.start();
//        progressBar.setVisibility(View.INVISIBLE);


        adapter = new FastScrollAdapter(this, LayoutFriendsList, 1);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Enter the query", " Enter search submit " + s);
                sendSearchQuery(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                Log.d("Enter the query", " Enter search change " + s + " here is the change");
                if (mSearchView.getQuery().length() == 0) {
                    Log.d("Enter the query", "empty");

                    LayoutFriendsList.clear();
                    recyclerView.setAdapter(new FastScrollAdapter(SearchActivity.this, LayoutFriendsList, 1));
                } else {
                    Log.d("Enter the sendSearchQuery", "empty");

                    sendSearchQuery(s);
                }


                return false;
            }

        });


    }
    protected void onResume() {
        super.onResume();
        if (mSearchView.getQuery().length() == 0) {
            Log.d("Enter the query onResume", "empty");

            LayoutFriendsList.clear();
            recyclerView.setAdapter(new FastScrollAdapter(SearchActivity.this, LayoutFriendsList, 1));
        } else {
            sendSearchQuery(mSearchView.getQuery().toString());
        }
    }

    public void sendSearchQuery(String word) {
      //  progressBar.setVisibility(View.VISIBLE);
        LayoutFriendsList.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        searchInterface = retrofit.create(SearchInterface.class);


        final Call<List<UserModel>> FriendsResponse = searchInterface.getSearchResult(word);
        FriendsResponse.enqueue(new Callback<List<UserModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                    userModelList = response.body();
                    LayoutFriendsList.clear();

                    if (userModelList != null) {

                        if (userModelList.size() == 0) {

                            recyclerView.setAdapter(new FastScrollAdapter(SearchActivity.this, LayoutFriendsList, 1));

                        } else {
                            Log.d("Not null", Integer.toString(userModelList.get(0).getId()));
                            for (int i = 0; i < userModelList.size(); i++) {
                                LayoutFriendsList.add(new MyFollowersActivity.friend(Integer.valueOf(userModelList.get(i).getId()), userModelList.get(i).getImage(),
                                        userModelList.get(i).getFirst_name() + " " + userModelList.get(i).getLast_name(),-1));
                                //progressBar.setVisibility(View.INVISIBLE);
                            }
                            recyclerView.setAdapter(new FastScrollAdapter(SearchActivity.this, LayoutFriendsList, 2));



                        }
                    }
                }
             /*   else{

                    recyclerView.setAdapter(new FastScrollAdapter(SearchActivity.this, LayoutFriendsList));}*/

            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                // progressBar.setVisibility(View.GONE);
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("fail to get friends ", "Failure to Get friends");

            }
        });
    }

}

