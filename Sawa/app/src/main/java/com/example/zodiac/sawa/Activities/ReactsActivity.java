package com.example.zodiac.sawa.Activities;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewAdapters.ReactsAdapter;
import com.example.zodiac.sawa.RecyclerViewModels.ReactsRecyclerViewModel;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringApi.PostInterface;
import com.example.zodiac.sawa.SpringModels.ReactSingleModel;
import com.example.zodiac.sawa.SpringModels.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReactsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PostInterface postInterface;
    private List<ReactsRecyclerViewModel> reactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reacts);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Bundle b = getIntent().getExtras();
        int postId = 0;
        if (b != null) {
           postId=  b.getInt("postId");

        }

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        postInterface = retrofit.create(PostInterface.class);
        final Call<ReactSingleModel> reactResponse = postInterface.getPostReact(postId,1);
        reactResponse.enqueue(new Callback<ReactSingleModel>() {
            @Override
            public void onResponse(Call<ReactSingleModel> call, Response<ReactSingleModel> response) {
                ReactSingleModel reactSingleModel=new ReactSingleModel();
                reactSingleModel=response.body();
                for(int i=0; i<reactSingleModel.getUsers().size();i++){
                    reactsList.add(new ReactsRecyclerViewModel(reactSingleModel.getUsers().get(i).getId(),reactSingleModel.getUsers().get(i).getFirst_name(),
                            reactSingleModel.getUsers().get(i).getImage()));


                }
                recyclerView.setAdapter(new ReactsAdapter(getApplicationContext(),reactsList));



            }

            @Override
            public void onFailure(Call<ReactSingleModel> call, Throwable t) {

            }
        });


    }

}
