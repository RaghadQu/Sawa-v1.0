package com.example.zodiac.sawa.RecyclerViewAdapters;

/**
 * Created by raghadq on 5/2/2017.
 */

import android.app.Dialog;
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

import com.example.zodiac.sawa.Activities.MyFollowersActivity;
import com.example.zodiac.sawa.Activities.MyProfileActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Services.FriendServices.FollowFunctions;
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

import static com.facebook.FacebookSdk.getApplicationContext;

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
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final FollowFunctions friendsFunctions = new FollowFunctions();
        final MyFollowersActivity.friend user = userList.get(position);
        final int FollowState = userList.get(position).getState();
        if (userList.get(position).getId() == GeneralAppInfo.getUserID()) {
            holder.remove.setVisibility(View.INVISIBLE);

        } else {

            if(FollowState != -1  )
            {
                holder.remove.setVisibility(View.VISIBLE);
            if (FollowState == 2) {
                holder.remove.setText("Following");
            }
            if (FollowState == 0) {
                holder.remove.setText("Follow");
            }
            if (FollowState == 1) {
                holder.remove.setText("Requested");
            }
        }
        else
            {
                holder.remove.setVisibility(View.INVISIBLE);

            }
        }

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
                        friendsFunctions.startFriend(mContext, user.getUserName(), user.getId(), user.getImageResourceId());
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
                        friendsFunctions.startFriend(mContext, user.getUserName(), user.getId(), user.getImageResourceId());
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
            if (removeButtonFlag != 2) {
                remove.setVisibility(View.VISIBLE);
                if (removeButtonFlag == 0) // From MyFollowersActivity
                {
                    remove.setText("");
                }
                if (removeButtonFlag == 1) //From MyFollowingActivity
                {

                    remove.setText("Following");
                }


                final Dialog ConfirmDeletion = new Dialog(mContext);
            ConfirmDeletion.setContentView(R.layout.confirm_delete_friend_or_request_dialog);
            final Button NoBtn = (Button) ConfirmDeletion.findViewById(R.id.NoBtn);
            final Button YesBtn = (Button) ConfirmDeletion.findViewById(R.id.YesBtn);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GeneralAppInfo.SPRING_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            final FriendshipInterface FriendApi = retrofit.create(FriendshipInterface.class);
            final FriendRequestModel FriendRequest = new FriendRequestModel();



                remove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final int position = getAdapterPosition();
                        if (userList.get(position) != null) {
                            if (remove.getText().toString().equals("Follow")) {
                                FriendRequest.setFriend1_id(GeneralAppInfo.getUserID());
                                FriendRequest.setFriend2_id(userList.get(position).getId());
                                final Call<FriendResponseModel> FollowCall = FriendApi.sendNewFollow(FriendRequest);
                                FollowCall.enqueue(new Callback<FriendResponseModel>() {
                                    @Override
                                    public void onResponse(Call<FriendResponseModel> call, Response<FriendResponseModel> response) {
                                        Log.d("FastScrollResp", " " + response.code());
                                        if (response.code() == 200) {
                                            remove.setText("Requested");
                                        } else {
                                            GeneralFunctions generalFunctions = new GeneralFunctions();
                                            generalFunctions.showErrorMesaage(getApplicationContext());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<FriendResponseModel> call, Throwable t) {
                                        Log.d("FastScrollResp ", "Failure " + t.getMessage());

                                    }
                                });
                            } else if (remove.getText().toString().equals("Following")) {
                                String confirmMsg = " Are you sure you want to unfollow " + userList.get(position).getUserName() + " ?";
                                TextView textMsg = (TextView) ConfirmDeletion.findViewById(R.id.TextMsg);
                                textMsg.setText(confirmMsg);
                                ConfirmDeletion.show();
                                NoBtn.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        ConfirmDeletion.dismiss();

                                    }
                                });

                                YesBtn.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        ConfirmDeletion.dismiss();
                                        callDeleteApi(position, remove);

                                    }
                                });
                            } else if (remove.getText().toString().equals("Requested")) {
                                callDeleteApi(position, remove);
                            }

                        }

                    }
                });


            }

        }
    }

    public void callDeleteApi(int position, final TextView remove) {
        Log.d("FastScrollAdapter"," 5");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FriendshipInterface FriendApi = retrofit.create(FriendshipInterface.class);

        final FriendRequestModel FriendRequest = new FriendRequestModel();
        FriendRequest.setFriend1_id(userList.get(position).getId());
        FriendRequest.setFriend2_id(GeneralAppInfo.getUserID());
        final Call<Integer> deleteCall = FriendApi.deleteFollow(FriendRequest);
        deleteCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("FastScrollResp", " " + response.code());
                if (response.code() == 200) {
                    remove.setText("Follow");
                } else {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("fail to get friends ", "Failure to Get friends");
            }
        });
    }
}