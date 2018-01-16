package com.example.zodiac.sawa.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zodiac.sawa.Activities.MyProfileActivity;
import com.example.zodiac.sawa.Activities.ReactsActivity;
import com.example.zodiac.sawa.Activities.YoutubePlayerDialogActivity;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.Services.FriendServices.FollowFunctions;
import com.example.zodiac.sawa.SpringApi.PostInterface;
import com.example.zodiac.sawa.SpringModels.PostCommentModel;
import com.example.zodiac.sawa.SpringModels.PostResponseModel;
import com.example.zodiac.sawa.SpringModels.ReactRequestModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.zodiac.sawa.R.id.loveCount;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Rabee on 11/17/2017.
 */

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.UserViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {
    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";
    public View view;
    HomePostAdapter.UserViewHolder viewHolder;
    List<PostCommentModel> postResponseModelsList;
    PostInterface service;
    private Context mContext;


    public HomePostAdapter(Context mContext, List<PostCommentModel> postResponseModelsList) {
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
        final PostResponseModel postResponseModel = postResponseModelsList.get(position).getPost();
        final int[] postLoveIntCount = {postResponseModelsList.get(position).getReacts().getLoveList().getCount()};
        final int[] postLikeIntCount = {postResponseModelsList.get(position).getReacts().getLikeList().getCount()};
        final int[] postDislikeIntCount = {postResponseModelsList.get(position).getReacts().getDislikeList().getCount()};

        boolean pressedLoveFlag = false, PressedLikeFlag = false, PressedUnlikeFlag = false;
        if (postResponseModelsList.get(position).getReacts().getLoveList().getMyAction()) {
            holder.postLoveIcon.setImageResource(R.drawable.filled_love_post);
            pressedLoveFlag = true;
        }

        if (postResponseModelsList.get(position).getReacts().getLikeList().getMyAction()) {
            holder.postLikeIcon.setImageResource(R.drawable.filled_like3);
            PressedLikeFlag = true;

        }
        if (postResponseModelsList.get(position).getReacts().getDislikeList().getMyAction()) {
            holder.postUnlikeIcon.setImageResource(R.drawable.filled_unlike3);
            PressedUnlikeFlag = true;

        }


        Log.d("ResponsePost::", " " + postResponseModel.getYoutubelink());
        holder.posterUserName.setText(postResponseModel.getUserId().getFirst_name() + " " + postResponseModel.getUserId().getLast_name());
        holder.postBodyText.setText(postResponseModel.getText());

        String image;
        image = postResponseModel.getUserId().getImage();
        String imageUrl = GeneralAppInfo.SPRING_URL + "/" + image;
        Picasso.with(mContext).load(imageUrl).into(holder.posterProfilePicture);
        if (postResponseModel.getImage() != null) {
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


        holder.postLoveCount.setText(postLoveIntCount[0] > 0 ? (String.valueOf(postLoveIntCount[0]) + (postLoveIntCount[0] == 1 ? " Love" : " Loves")) : "");
        holder.postLikeCount.setText(postLikeIntCount[0] > 0 ? (String.valueOf(postLikeIntCount[0]) + (postLikeIntCount[0] == 1 ? " Like" : " Likes")) : "");
        holder.postDislikePost.setText(postDislikeIntCount[0] > 0 ? (String.valueOf(postDislikeIntCount[0]) + (postDislikeIntCount[0] > 1 ? " Unlikes" : " Unlike")) : "");
        if (postResponseModel.getLink() != "" && postResponseModel.getImage() == null) {
            holder.youtubeLinkTitle.setText(postResponseModel.getYoutubelink().getTitle());
            holder.youtubeLinkAuthor.setText("Channel: " + postResponseModel.getYoutubelink().getAuthor_name());
            imageUrl = postResponseModel.getYoutubelink().getImage();
            Picasso.with(mContext).load(imageUrl).into(holder.youtubeLinkImage);
        } else {
            holder.youtubeLinkLayout.setVisibility(View.GONE);
        }

        View.OnClickListener myClickLIstener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), YoutubePlayerDialogActivity.class);
                Bundle b = new Bundle();
                b.putString("youtubeSongUrl", postResponseModel.getLink());
                i.putExtras(b);
                mContext.startActivity(i);
                // your stuff
            }
        };
        View.OnClickListener reactsClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReactsActivity.class);
                Bundle b = new Bundle();
                b.putInt("postId",postResponseModelsList.get(position).getPost().getPostId());

                i.putExtras(b);
                mContext.startActivity(i);
                // your stuff
            }
        };

        holder.youtubeLinkTitle.setOnClickListener(myClickLIstener);
        holder.youtubeLinkAuthor.setOnClickListener(myClickLIstener);
        holder.youtubeLinkImage.setOnClickListener(myClickLIstener);
        holder.postLoveCount.setOnClickListener(reactsClickListener);
        holder.postLikeCount.setOnClickListener(reactsClickListener);
        final boolean[] finalPressedLoveFlag = {pressedLoveFlag};
        holder.postLoveIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!finalPressedLoveFlag[0]) {
                            AddNewReact(position, 3);
                            Log.d("PostHolder", " press on love icon " + position);
                            holder.postLoveIcon.setImageResource(R.drawable.filled_love_post);
                            postLoveIntCount[0]++;
                            holder.postLoveCount.setText(postLoveIntCount[0] > 0 ? (String.valueOf(postLoveIntCount[0]) + (postLoveIntCount[0] == 1 ? " Love" : " Loves")) : "");
                        } else {
                            DeleteReact(position, 0);
                            holder.postLoveIcon.setImageResource(R.drawable.love2);
                            postLoveIntCount[0]--;
                            holder.postLoveCount.setText(postLoveIntCount[0] > 0 ? (String.valueOf(postLoveIntCount[0]) + (postLoveIntCount[0] == 1 ? " Love" : " Loves")) : "");
                        }
                        finalPressedLoveFlag[0] = !finalPressedLoveFlag[0];
                    }
                });
        final boolean[] finalPressedLikeFlag = {PressedLikeFlag};

        holder.postLikeIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!finalPressedLikeFlag[0]) {
                            Log.d("PostHolder", " press on like icon " + position);
                            AddNewReact(position, 1);
                            holder.postLikeIcon.setImageResource(R.drawable.filled_like3);
                            postLikeIntCount[0]++;
                            holder.postLikeCount.setText(postLikeIntCount[0] > 0 ? (String.valueOf(postLikeIntCount[0]) + (postLikeIntCount[0] == 1 ? " Like" : " Likes")) : "");
                        } else {
                            DeleteReact(position, 0);
                            holder.postLikeIcon.setImageResource(R.drawable.like3);
                            postLikeIntCount[0]--;
                            holder.postLikeCount.setText(postLikeIntCount[0] > 0 ? (String.valueOf(postLikeIntCount[0]) + (postLikeIntCount[0] == 1 ? " Like" : " Likes")) : "");

                        }
                        finalPressedLikeFlag[0] = !finalPressedLikeFlag[0];


                    }
                });
        final boolean[] finalPressedUnlikeFlag = {PressedUnlikeFlag};
        holder.postLoveCount.setOnClickListener(reactsClickListener);

        holder.postUnlikeIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!finalPressedUnlikeFlag[0]) {
                            AddNewReact(position, 2);
                            Log.d("PostHolder", " press on unlike icon " + position);
                            holder.postUnlikeIcon.setImageResource(R.drawable.filled_unlike3);
                            postDislikeIntCount[0]++;
                            holder.postDislikePost.setText(postDislikeIntCount[0] > 0 ? (String.valueOf(postDislikeIntCount[0]) + (postDislikeIntCount[0] > 1 ? " Unlikes" : " Unlike")) : "");
                        } else {
                            DeleteReact(position, 0);
                            holder.postUnlikeIcon.setImageResource(R.drawable.unlike3);
                            postDislikeIntCount[0]--;
                            holder.postDislikePost.setText(postDislikeIntCount[0] > 0 ? (String.valueOf(postDislikeIntCount[0]) + (postDislikeIntCount[0] > 1 ? " Unlikes" : " Unlike")) : "");

                        }
                        finalPressedUnlikeFlag[0] = !finalPressedUnlikeFlag[0];
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
        return String.valueOf(postResponseModelsList.get(position).getPost().getUserId().getFirst_name().charAt(0));
    }

    public void AddNewReact(int position, int type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PostInterface addNewReact = retrofit.create(PostInterface.class);

        ReactRequestModel reactRequestModel = new ReactRequestModel();
        int postId = postResponseModelsList.get(position).getPost().getPostId();
        reactRequestModel.setPostId(postId);
        reactRequestModel.setType(type);
        reactRequestModel.setUserId(GeneralAppInfo.getGeneralUserInfo().getUser().getId());
        Call<Integer> addNewReactResponse = addNewReact.addNewReact(reactRequestModel);

        addNewReactResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("PostHolder", " Post react addition " + response.code());
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("PostHolder", t.getMessage());
            }

        });
    }

    public void DeleteReact(int position, int type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PostInterface deleteReact = retrofit.create(PostInterface.class);

        ReactRequestModel reactRequestModel = new ReactRequestModel();
        int postId = postResponseModelsList.get(position).getPost().getPostId();
        reactRequestModel.setPostId(postId);
        reactRequestModel.setType(type);
        reactRequestModel.setUserId(GeneralAppInfo.getGeneralUserInfo().getUser().getId());
        Call<Integer> addNewReactResponse = deleteReact.deleteReact(reactRequestModel);

        addNewReactResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("PostHolder", " Post react addition " + response.code());
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("PostHolder", t.getMessage());
            }

        });
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView posterProfilePicture;
        TextView posterUserName;
        TextView postTime;
        TextView    postLoveCount, postLikeCount, postDislikePost;
        TextView postCommentCount;
        TextView postBodyText;
        ImageView postImage;
        ImageView youtubeLinkImage;
        TextView youtubeLinkTitle;
        TextView youtubeLinkAuthor;
        LinearLayout youtubeLinkLayout;
        ImageView postLoveIcon, postLikeIcon, postUnlikeIcon;


        public UserViewHolder(View itemView) {

            super(itemView);

            posterProfilePicture = (CircleImageView) itemView.findViewById(R.id.userProfilePicture);
            posterUserName = (TextView) itemView.findViewById(R.id.username);
            postBodyText = (TextView) view.findViewById(R.id.postText);
            postImage = (ImageView) view.findViewById(R.id.postImage);
            youtubeLinkImage = (ImageView) view.findViewById(R.id.youtubeLinkImage);
            youtubeLinkTitle = (TextView) view.findViewById(R.id.youtubeLinkTitle);
            youtubeLinkLayout = (LinearLayout) view.findViewById(R.id.youtubeLinkLayout);
            youtubeLinkAuthor = (TextView) view.findViewById(R.id.youtubeLinkAuthor);
            postLoveCount = (TextView) view.findViewById(loveCount);
            postLikeCount = (TextView) view.findViewById(R.id.likeCount);
            postDislikePost = (TextView) view.findViewById(R.id.unlikeCount);
            postLoveIcon = (ImageView) view.findViewById(R.id.love_post);
            postLikeIcon = (ImageView) view.findViewById(R.id.like_post);
            postUnlikeIcon = (ImageView) view.findViewById(R.id.dislike_post);


        }


    }
}
