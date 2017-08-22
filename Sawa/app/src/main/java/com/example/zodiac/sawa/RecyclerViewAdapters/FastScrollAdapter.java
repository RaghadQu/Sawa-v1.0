package com.example.zodiac.sawa.RecyclerViewAdapters;

/**
 * Created by raghadq on 5/2/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zodiac.sawa.Activities.MyFollowingActivity;
import com.example.zodiac.sawa.Activities.MyRequestsActivity;
import com.example.zodiac.sawa.Services.FriendServices.FreindsFunctions;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.Activities.MyFollowersActivity;
import com.example.zodiac.sawa.Activities.MyProfileActivity;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.example.zodiac.sawa.SpringModels.FriendRequestModel;
import com.example.zodiac.sawa.SpringModels.FriendResponseModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FastScrollAdapter extends RecyclerView.Adapter<FastScrollAdapter.UserViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {

    public int removeButtonFlag;
    public View view;
    UserViewHolder viewHolder;
    ArrayList<MyFollowersActivity.friend> userList;
    FriendshipInterface service;
    private Context mContext;

    public FastScrollAdapter(Context mContext, ArrayList<MyFollowersActivity.friend> userList, int removeButtonFlag) {
        this.mContext = mContext;
        this.userList = userList;
        this.removeButtonFlag = removeButtonFlag;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.freinds_recycle_view, null);
        viewHolder = new UserViewHolder(view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(FriendshipInterface.class);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final FreindsFunctions freindsFunctions = new FreindsFunctions();
        final MyFollowersActivity.friend user = userList.get(position);
        holder.tvName.setText(user.getUserName());
        String image;
        try {
            image = user.getImageResourceId();
            String imageUrl = GeneralAppInfo.SPRING_URL + "/" + image;
            Picasso.with(mContext).load(imageUrl).into(holder.ivProfile);

        } catch (MalformedURLException e) {
            holder.ivProfile.setImageResource(R.drawable.account);
            e.printStackTrace();
        }
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the same user enter his profile
                if (user.getId() == GeneralAppInfo.getUserID()) {
                    Intent i = new Intent(mContext, MyProfileActivity.class);
                    mContext.startActivity(i);
                } else {

                    try {
                        freindsFunctions.startFriend(mContext, user.getUserName(), user.getId(), user.getImageResourceId());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.getId() == GeneralAppInfo.getUserID()) {
                    Intent i = new Intent(mContext, MyProfileActivity.class);
                    mContext.startActivity(i);
                } else {
                    try {
                        freindsFunctions.startFriend(mContext, user.getUserName(), user.getId(), user.getImageResourceId());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(userList.get(position).getUserName().charAt(0));
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        TextView tvName;
        Button remove;


        public UserViewHolder(View itemView) {
            super(itemView);
            ivProfile = (CircleImageView) itemView.findViewById(R.id.image);
            tvName = (TextView) itemView.findViewById(R.id.Name);
            remove = (Button) view.findViewById(R.id.Remove);

            if (removeButtonFlag == 1) {
                remove.setVisibility(View.INVISIBLE);
            }

            if(removeButtonFlag == 0)
            {
                remove.setText("Follow");
            }


            remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    final int position = getAdapterPosition();


                    FreindsFunctions friendFunction = new FreindsFunctions();
                    if(userList.get(position)!= null) {
                        Log.d("FastScroll",userList.get(position) + " " + position);
                     //   friendFunction.DeleteFriend(GeneralAppInfo.getUserID(), userList.get(position).getId(), remove);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(GeneralAppInfo.SPRING_URL)
                                .addConverterFactory(GsonConverterFactory.create()).build();
                        FriendshipInterface FriendApi = retrofit.create(FriendshipInterface.class);

                        final FriendRequestModel FriendRequest = new FriendRequestModel();
                        FriendRequest.setFriend1_id(GeneralAppInfo.getUserID());
                        FriendRequest.setFriend2_id( userList.get(position).getId());

                        Log.d("FastScroll2",userList.get(position) + " " + position);

                        if(removeButtonFlag==2) {
                            final Call<Integer> deleteCall = FriendApi.deleteFollow(FriendRequest);
                            deleteCall.enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    Log.d("FastScrollResp", " " + response.code());

                                    MyFollowingActivity.recyclerView.removeViewAt(position);
                                    MyFollowingActivity.FreindsList.remove(position);
                                    MyFollowingActivity.LayoutFriendsList.remove(position);


                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Log.d("fail to get friends ", "Failure to Get friends");

                                }


                            });
                        }
                        else if (removeButtonFlag==0)
                        {
                            final Call<FriendResponseModel> deleteCall = FriendApi.sendFollowRequest(FriendRequest);
                            deleteCall.enqueue(new Callback<FriendResponseModel>() {
                                @Override
                                public void onResponse(Call<FriendResponseModel> call, Response<FriendResponseModel> response) {
                                    Log.d("FastScrollResp", " " + response.code());
                                    remove.setText("Pending");


                                }

                                @Override
                                public void onFailure(Call<FriendResponseModel> call, Throwable t) {
                                    Log.d("fail to get friends ", "Failure to Get friends");

                                }


                            });
                        }
                    }

                }
            });


        }


    }


}