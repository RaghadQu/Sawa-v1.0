package com.example.zodiac.sawa;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zodiac.sawa.RecyclerViewAdapters.NotificationAdapter;
import com.example.zodiac.sawa.Spring.Models.NotificationModel;
import com.example.zodiac.sawa.SpringApi.NotificationInterface;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by zodiac on 05/22/2017.
 */

public class NotificationTab extends AppCompatDialogFragment {

    public static ArrayList<NotificationAdapter.NotificationRecyclerViewDataProvider> NotificationList = new ArrayList<>();
    public static FastScrollRecyclerView recyclerView;
    public static NotificationAdapter adapter;
    public static Retrofit retrofit;
    View view;
    Context context = getContext();

    public static void getUserNotifications(final Context context) {

        retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        NotificationInterface notificationApi = retrofit.create(NotificationInterface.class);
        Call<NotificationModel> notificationResponse = notificationApi.getNotification(GeneralAppInfo.getUserID());

        notificationResponse.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                Log.d("NotificationSpring", " Notification code " + response.code());
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                    NotificationModel UserNotification;
                    UserNotification = response.body();
                    NotificationList.clear();
                    if (UserNotification != null) {
                        Log.d("NotificationSpring", UserNotification.getNot_sent_notifications().size() + " " + UserNotification.getSent_notifications().size());

                        for (int i = 0; i < UserNotification.getNot_sent_notifications().size(); i++) {
                            NotificationModel.Notification notificationOne = UserNotification.getNot_sent_notifications().get(i);
                            NotificationList.add(new NotificationAdapter.NotificationRecyclerViewDataProvider(context, notificationOne.getFriend1_id().getId(), notificationOne.getFriend1_id().getImage(), (notificationOne.getFriend1_id().getFirst_name() + " " + notificationOne.getFriend1_id().getLast_name()), String.valueOf(notificationOne.getTimestamp()), Integer.valueOf(notificationOne.getRead_flag())));
                            NotificationList.get(i).setType(notificationOne.getType());
                            NotificationList.get(i).setNotificatioId(notificationOne.getFriend1_id().getId());
                        }

                        for (int i = 0; i < UserNotification.getSent_notifications().size(); i++) {
                            NotificationModel.Notification notificationOne = UserNotification.getSent_notifications().get(i);
                            NotificationList.add(new NotificationAdapter.NotificationRecyclerViewDataProvider(context, notificationOne.getFriend1_id().getId(), notificationOne.getFriend1_id().getImage(), (notificationOne.getFriend1_id().getFirst_name() + " " + notificationOne.getFriend1_id().getLast_name()), String.valueOf(notificationOne.getTimestamp()), Integer.valueOf(notificationOne.getRead_flag())));
                            NotificationList.get(i).setType(notificationOne.getType());
                            NotificationList.get(i).setNotificatioId(notificationOne.getId());
                        }
                    }
                    recyclerView.setAdapter(new NotificationAdapter(NotificationList));
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("NotificationFail", t.getMessage());
            }

        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        adapter = new NotificationAdapter(NotificationList);
        view = inflater.inflate(R.layout.notification_tab, container, false);
        recyclerView = (FastScrollRecyclerView) view.findViewById(R.id.recyclerNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getUserNotifications(context);

        return view;


    }


}
