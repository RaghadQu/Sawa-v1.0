package com.example.zodiac.sawa.RecyclerViewAdapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zodiac.sawa.Services.FriendServices.FollowFunctions;
import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.NotificationTabFragment;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.SpringModels.updateNotificationModel;
import com.example.zodiac.sawa.SpringApi.NotificationInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rabee on 4/24/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder> {
    ArrayList<NotificationRecyclerViewDataProvider> notificationRecyclerViewDataProviders = new ArrayList<NotificationRecyclerViewDataProvider>();
    View view;

    public NotificationAdapter(ArrayList<NotificationRecyclerViewDataProvider> notificationRecyclerViewDataProviders) {
        this.notificationRecyclerViewDataProviders = notificationRecyclerViewDataProviders;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycle_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        NotificationRecyclerViewDataProvider dataProvider = notificationRecyclerViewDataProviders.get(position);

        String imageUrl = GeneralAppInfo.SPRING_URL + "/" + dataProvider.getImage();
        Picasso.with(dataProvider.getContext()).load(imageUrl).into(holder.image);
        holder.text.setText(dataProvider.getText() + " sent you a friend request.");
        holder.time.setText(dataProvider.getTime());
        // 0 new   1 not new
        if (dataProvider.getReadFlag() == 0) {
            holder.layout.setBackgroundColor(ContextCompat.getColor(dataProvider.getContext(), R.color.notify));
            Log.d("--- 0 ---", " enter type 0 ");
        } else {
            Log.d("--- 1 ---", " enter type 1 ");

            holder.layout.setBackgroundColor(ContextCompat.getColor(dataProvider.getContext(), R.color.white));
        }


    }

    @Override
    public int getItemCount() {
        return notificationRecyclerViewDataProviders.size();
    }

    public static class NotificationRecyclerViewDataProvider {
        String image;
        String text;
        String time;
        Context context;
        int readFlag;
        int friend_id;
        int type;
        int notificatioId;


        public NotificationRecyclerViewDataProvider(Context context, int friend_id, String image, String text, String time, int readFlag) {
            this.image = image;
            this.friend_id = friend_id;
            this.text = text;
            this.time = time;
            this.context = context;
            this.readFlag = readFlag;
        }

        public int getNotificatioId() {
            return notificatioId;
        }

        public void setNotificatioId(int notificatioId) {
            this.notificatioId = notificatioId;
        }

        public Context getContext() {
            return context;
        }

        public int getReadFlag() {
            return readFlag;
        }

        public void setReadFlag(int readFlag) {
            this.readFlag = readFlag;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(int friend_id) {
            this.friend_id = friend_id;
        }


        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView time;
        TextView text;
        RelativeLayout layout;
        int position;

        public RecyclerViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.NotificationLayout);
            image = (CircleImageView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.text);
            time = (TextView) view.findViewById(R.id.time);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GeneralAppInfo.SPRING_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            final NotificationInterface notificationApi = retrofit.create(NotificationInterface.class);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Success", "updateReadFlag " + getAdapterPosition());

                    position = getAdapterPosition();
                    updateNotificationModel notificationModel = new updateNotificationModel();

                    int notificatioID = NotificationTabFragment.NotificationList.get(position).getNotificatioId();
                    notificationModel.setNotification_id(notificatioID);
                    Call<Integer> notificationResponse = notificationApi.updateReadFlag(notificationModel);
                    notificationResponse.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d("Success", "updateReadFlag " + response.code());

                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d("Fail", "updateReadFlag : " + t.getMessage());
                        }
                    });


                    if (NotificationTabFragment.NotificationList.get(position).getType() == 3) {
                        String name = NotificationTabFragment.NotificationList.get(position).getText();
                        String image = NotificationTabFragment.NotificationList.get(position).getImage();
                        int friend_id = NotificationTabFragment.NotificationList.get(position).getFriend_id();
                        Context context = NotificationTabFragment.NotificationList.get(position).getContext();

                        final FollowFunctions freindsFunctions = new FollowFunctions();
                        freindsFunctions.startFriend(context, name, friend_id, image);
                    }
                }
            });
        }
    }

}
