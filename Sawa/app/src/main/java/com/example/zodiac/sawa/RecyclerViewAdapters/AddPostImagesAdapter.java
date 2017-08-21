package com.example.zodiac.sawa.RecyclerViewAdapters;

/**
 * Created by raghadq on 5/2/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.zodiac.sawa.Activities.AddPostActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.Activities.MyFollowersActivity;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringApi.FriendshipInterface;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPostImagesAdapter extends RecyclerView.Adapter<AddPostImagesAdapter.UserViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {

    public View view;
    UserViewHolder viewHolder;
    ArrayList<MyFollowersActivity.friend> friendsList;
    FriendshipInterface service;
    private Context mContext;

    public AddPostImagesAdapter(Context mContext, ArrayList<MyFollowersActivity.friend> userList) {
        this.mContext = mContext;
        this.friendsList = userList;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.post_friends_images, null);
        viewHolder = new UserViewHolder(view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(FriendshipInterface.class);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final MyFollowersActivity.friend user = friendsList.get(position);
        holder.tvName.setText(user.getUserName());
        String image;
        try {
            image = user.getImageResourceId();
            String imageUrl = GeneralAppInfo.IMAGE_URL + image;
            Picasso.with(mContext).load(imageUrl).into(holder.ivProfile);

        } catch (MalformedURLException e) {
            holder.ivProfile.setImageResource(R.drawable.account);
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(friendsList.get(position).getUserName().charAt(0));
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        TextView tvName;


        public UserViewHolder(View itemView) {
            super(itemView);
            ivProfile = (CircleImageView) itemView.findViewById(R.id.Image);
            tvName = (TextView) itemView.findViewById(R.id.Name);

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int FriendId = Integer.valueOf(AddPostActivity.FriendPostList.get(getAdapterPosition()).getId());
                    Log.d("AdapterID", " reciever id is : " + FriendId);
                    AddPostActivity.setRecieverID(FriendId);
                    try {
                        String recieverImage = AddPostActivity.FriendPostList.get(getAdapterPosition()).getImageResourceId();
                        //  Picasso.with(mContext).load(recieverImage).into(AddPostActivity.receiverImage);
                        AddPostActivity.setRecieverImage(recieverImage, mContext);

                    } catch (MalformedURLException e) {
                        Log.d("AdapterImage", " error in image");

                        e.printStackTrace();
                    }
                }


            });


        }


    }
}