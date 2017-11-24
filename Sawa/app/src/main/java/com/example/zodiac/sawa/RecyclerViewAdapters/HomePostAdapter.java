package com.example.zodiac.sawa.RecyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.zodiac.sawa.Activities.MyProfileActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Services.FriendServices.FollowFunctions;
import com.example.zodiac.sawa.SpringApi.PostInterface;
import com.example.zodiac.sawa.SpringModels.PostResponseModel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rabee on 11/17/2017.
 */

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.UserViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {
    public View view;
    HomePostAdapter.UserViewHolder viewHolder;
    List<PostResponseModel> postResponseModelsList;
    PostInterface service;
    private Context mContext;
    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";

    String[] VideoID = {"P3mAtvs5Elc", "nCgQDjiotG0", "P3mAtvs5Elc"};

    public HomePostAdapter(Context mContext,List<PostResponseModel>postResponseModelsList) {
        this.mContext = mContext;
        this.postResponseModelsList = postResponseModelsList;

    }


    @Override
    public HomePostAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.post_view, null);
        viewHolder = new HomePostAdapter.UserViewHolder(view);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(PostInterface.class);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final HomePostAdapter.UserViewHolder holder, final int position) {
        final FollowFunctions friendsFunctions = new FollowFunctions();
        final PostResponseModel postResponseModel = postResponseModelsList.get(position);



       holder.posterUserName.setText(postResponseModel.getUserId().getFirst_name() + " " + postResponseModel.getUserId().getLast_name());
      //   holder.posterUserName.setText("Ibrahim Zahra");

        holder.postBodyText.setText(postResponseModel.getText());
      //  if (postResponseModel.getLink().equals(null) == false) {


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        String image;
        image = postResponseModel.getUserId().getImage();
        String imageUrl = GeneralAppInfo.SPRING_URL + "/" + image;
        Picasso.with(mContext).load(imageUrl).into(holder.posterProfilePicture);
        if(postResponseModel.getImage()!=null){
            imageUrl = GeneralAppInfo.SPRING_URL + "/" + postResponseModel.getImage();
            Picasso.with(mContext).load(imageUrl).into(holder.postImage);

        }

        holder.posterUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the same user enter his profile
                if (postResponseModel.getUserId().getId() == GeneralAppInfo.getUserID()) {
                    Intent i = new Intent(mContext, MyProfileActivity.class);
                    mContext.startActivity(i);
                } else {

                    friendsFunctions.startFriend(mContext, postResponseModel.getUserId().getFirst_name() + " " + postResponseModel.getUserId()
                            .getLast_name(), postResponseModel.getUserId().getId(), postResponseModel.getUserId().getImage());
                }

            }
        });
        holder.posterProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (postResponseModel.getUserId().getId() == GeneralAppInfo.getUserID()) {
                    Intent i = new Intent(mContext, MyProfileActivity.class);
                    mContext.startActivity(i);
                } else {

                    friendsFunctions.startFriend(mContext, postResponseModel.getUserId().getFirst_name() + " " + postResponseModel.getUserId()
                            .getLast_name(), postResponseModel.getUserId().getId(), postResponseModel.getUserId().getImage());
                }
            }
        });

        holder.youTubeThumbnailView.initialize(api_key, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(VideoID[position%3]);
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });

    }

    @Override
    public int getItemCount() {
        return postResponseModelsList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(postResponseModelsList.get(position).getUserId().getFirst_name().charAt(0));
    }


    public class UserViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        CircleImageView posterProfilePicture;
        TextView posterUserName;
        TextView postTime;
        TextView postLoveCount, postLikeCount, postDislikePost;
        TextView postCommentCount;
        TextView postBodyText;
        ImageView postImage;
        protected FrameLayout containerYouTubePlayer;
        YouTubeThumbnailView youTubeThumbnailView;



        public UserViewHolder(View itemView) {

            super(itemView);

            posterProfilePicture = (CircleImageView) itemView.findViewById(R.id.userProfilePicture);
            posterUserName = (TextView) itemView.findViewById(R.id.username);
            postBodyText = (TextView) view.findViewById(R.id.postText);
            postImage=(ImageView)view.findViewById(R.id.postImage);

            // containerYouTubePlayer = (FrameLayout) itemView.findViewById(R.id.youtube_holder);
            youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);
            youTubeThumbnailView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.d("PressYoutube","Press on youtube to play");
            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext,api_key, VideoID[getLayoutPosition()]);
            mContext.startActivity(intent);
        }
    }
}
