package com.example.zodiac.sawa.TabbedFragments;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zodiac.sawa.Activities.MyFollowersActivity;
import com.example.zodiac.sawa.Activities.MyFollowingActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewAdapters.FastScrollAdapter;
import com.example.zodiac.sawa.RecyclerViewAdapters.NotificationAdapter;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringApi.NotificationInterface;
import com.example.zodiac.sawa.SpringApi.PostInterface;
import com.example.zodiac.sawa.SpringModels.NotificationModel;
import com.example.zodiac.sawa.SpringModels.PostRequestModel;
import com.example.zodiac.sawa.SpringModels.PostResponseModel;
import com.example.zodiac.sawa.SpringModels.UserModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodiac.sawa.RecyclerViewAdapters.NotificationAdapter;
import com.example.zodiac.sawa.SpringModels.NotificationModel;
import com.example.zodiac.sawa.SpringApi.NotificationInterface;
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

    public static ArrayList<NotificationAdapter.NotificationRecyclerViewDataProvider> NotificationList = new ArrayList<>();
    public static FastScrollRecyclerView recyclerView;
    public static NotificationAdapter adapter;
    public static Retrofit retrofit;
    View view;
    Context context = getContext();
    List<PostResponseModel> postResponseModelsList;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (FastScrollRecyclerView) view.findViewById(R.id.post_recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;


    }
    public void getHomePost() {

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

                    Log.d("Status", " Get home posts " + response.code());
                    if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                    } else {
                        postResponseModelsList = response.body();
                       // postResponseModelsList.clear();
                        //recyclerView.setAdapter(new FastScrollAdapter(MyFollowingActivity.this, LayoutFriendsList, 2));


                    }
                }


                @Override
                public void onFailure(Call<List<PostResponseModel>> call, Throwable t) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                    Log.d("fail to get friends ", "Failure to Get friends");

                }
            });
        }


    }
}
