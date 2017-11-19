package com.example.zodiac.sawa.TabbedFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zodiac.sawa.Activities.AddPostActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewAdapters.HomePostAdapter;
import com.example.zodiac.sawa.RecyclerViewAdapters.NotificationAdapter;
import com.example.zodiac.sawa.SpringApi.PostInterface;
import com.example.zodiac.sawa.SpringModels.PostResponseModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by zodiac on 05/22/2017.
 */

public class HomeFragment extends AppCompatDialogFragment {

    //  public static ArrayList<NotificationAdapter.NotificationRecyclerViewDataProvider> NotificationList = new ArrayList<>();
    public static FastScrollRecyclerView recyclerView;
    public static HomePostAdapter adapter;
    public static Retrofit retrofit;
    public static List<PostResponseModel> postResponseModelsList;
    View view;
    Context  context = getContext();

    public static void getHomePost( final Context context) {


        //  recyclerView = (FastScrollRecyclerView) view.findViewById(R.id.post_recylerView);
        // recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PostInterface postInterface;
        GeneralFunctions generalFunctions = new GeneralFunctions();
        boolean isOnline = generalFunctions.isOnline(getApplicationContext());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        postInterface = retrofit.create(PostInterface.class);
        if (isOnline == false) {

        } else {


            final Call<List<PostResponseModel>> postResponse = postInterface.getUserHomePost(GeneralAppInfo.getUserID());
            postResponse.enqueue(new Callback<List<PostResponseModel>>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<List<PostResponseModel>> call, Response<List<PostResponseModel>> response) {

                    Log.d("ResponsePost", " Get home posts " + response.code());
                    if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                    } else {
                        postResponseModelsList = response.body();
                        Log.d("ResponsePost", " " + response.body());

                        recyclerView.setAdapter(new HomePostAdapter(context, postResponseModelsList));
                        // postResponseModelsList.clear();


                    }
                }


                @Override
                public void onFailure(Call<List<PostResponseModel>> call, Throwable t) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                    Log.d("ResponsePost", "Failure to Get friends");

                }
            });
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton addPost = (FloatingActionButton) view.findViewById(R.id.fab);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddPostActivity.class);
                startActivity(i);

            }
        });
        //recyclerView = (FastScrollRecyclerView) view.findViewById(R.id.post_recylerView);
        // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView = (FastScrollRecyclerView) view.findViewById(R.id.post_recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        getHomePost(context);

        return view;


    }
}
