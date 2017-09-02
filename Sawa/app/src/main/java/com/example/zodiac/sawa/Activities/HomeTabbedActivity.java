package com.example.zodiac.sawa.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.GeneralFunctions;
import com.example.zodiac.sawa.NotificationTabFragment;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewAdapters.NotificationAdapter;
import com.example.zodiac.sawa.SpringApi.AboutUserInterface;
import com.example.zodiac.sawa.SpringApi.AuthInterface;
import com.google.firebase.iid.FirebaseInstanceId;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.zodiac.sawa.Services.BadgeViewService;
import static com.example.zodiac.sawa.R.id.container;
import com.example.zodiac.sawa.SpringModels.*;

public class HomeTabbedActivity extends AppCompatActivity {

    public static Handler UIHandler;
    public static Activity activity = null;
    static BadgeViewService badge;
    static UserModel userInfo;
    static SharedPreferences sharedPreferences;
    static ImageView imageView;
    static TabLayout tabLayout;
    static TextView userName;
    static Context context;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static void showBadge(Context c) {

        final int count = PreferenceManager.getDefaultSharedPreferences(c).getInt("notifications_counter",
                0);
        Log.d("enter", " count is :" + count);
        UIHandler.post(new Runnable() {
            @Override
            public void run() {

                badge.setText(String.valueOf(count));
                if (count > 0) {
                    badge.show();
                } else {
                    badge.hide();
                }
            }
        });
    }

    public static void logout() {

        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AuthInterface logoutApi = retrofit.create(AuthInterface.class);

        SignOutModel signOutModel = new SignOutModel();
        signOutModel.setDeviceId(android_id);
        signOutModel.setUserId(GeneralAppInfo.getUserID());
        Call<Integer> logOutnResponse = logoutApi.signOut(signOutModel);

        logOutnResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(HomeTabbedActivity.context);
                } else {


                    Log.d("SignOut", " " + response.code());
                    SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    HomeTabbedActivity.activity.finish();

//                ActivityCompat.finishAffinity((Activity) context);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(HomeTabbedActivity.context);
                Log.d("Fail", t.getMessage());
            }

        });
    }

    public static void getUserInfo() {
        Log.d("InfoUser", " Enter here ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralAppInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AboutUserInterface service = retrofit.create(AboutUserInterface.class);
        Log.d("InfoUser", " Enter before call ");

        final Call<UserModel> userModelCall = service.getUserInfo(GeneralAppInfo.getUserID());
        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                Log.d("InfoUser", " " + response.code());
                int statusCode = response.code();
                if (statusCode == 200) {
                    userInfo = response.body();
                    Log.d("InfoUser", " " + userInfo.getFirst_name());
                    userName.setText((userInfo.getFirst_name() + " " + userInfo.getLast_name()));

                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(HomeTabbedActivity.context);
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(HomeTabbedActivity.context);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Hello", " new resume");
        NotificationTabFragment.getUserNotifications(getApplicationContext());
        showBadge(getApplicationContext());
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("isRunning",
                1).commit();

    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("isRunning",
                0).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("isRunning",
                0).commit();
    }

    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("isRunning",
                0).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHandler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_home_tabbed2);
        HomeTabbedActivity.context = getApplicationContext();
        activity = this;
        //set As logined for badge number
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("isRunning",
                1).commit();

        ImageView searchImage = (ImageView) findViewById(R.id.serachImage);
        LinearLayout searchLayout = (LinearLayout) findViewById(R.id.SearchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("C", "Clicked");
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refresh", token);
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        GeneralFunctions generalFunctions = new GeneralFunctions();
        generalFunctions.storeUserIdWithDeviceId(GeneralAppInfo.getUserID(), android_id);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        imageView = (ImageView) findViewById(R.id.imageView);

        mViewPager.setOffscreenPageLimit(10);

        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.setIcon(R.drawable.notification);
        // tab.setCustomView(imageView);
        badge = new BadgeViewService(getApplicationContext(), imageView);
        badge.getOffsetForPosition(120, 30);
        showBadge(getApplicationContext());


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (position == GeneralAppInfo.notifications_tab_position) {

                    if (sharedPreferences.getInt("notifications_counter", 0) > 0) {
                        NotificationTabFragment.getUserNotifications(getApplicationContext());
                    }

                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("notifications_counter",
                            0).commit();
                    Log.d("notifications_counter", "set to zero");

                }
                showBadge(getApplicationContext());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            int iconId = -1;
            switch (i) {
                case 0:
                    iconId = R.drawable.home;
                    break;
                case 1:
                    iconId = R.drawable.notification;
                    break;
                case 2:

                    iconId = R.drawable.setting_small;
                    break;
            }
            tabLayout.getTabAt(i).setIcon(iconId);

        }
        final LayoutInflater factory = getLayoutInflater();
        View v = factory.inflate(R.layout.notification_tab, null);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Hello", "enter notification bar");

                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        int image1 = R.drawable.image1;
        int image2 = R.mipmap.friends_setting;
        int image3 = R.drawable.request_friend_setting;
        int image4 = R.drawable.log;
        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;
        String[] myDataset = {"Profile", "Friends", "Friend Requesst", "Log out"};
        int[] images = {image1, image2, image3, image4};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                GeneralFunctions.getSharedPreferences(getContext());
                View rootView = inflater.inflate(R.layout.fragment_home, container, false);
                FloatingActionButton addPost = (FloatingActionButton) rootView.findViewById(R.id.fab);


                addPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), AddPostActivity.class);
                        startActivity(i);

                    }
                });
                return rootView;
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                //NotificationTab;
                View rootView = inflater.inflate(R.layout.notification_tab, container, false);
                NotificationTabFragment.NotificationList = new ArrayList<>();
                NotificationTabFragment.adapter = new NotificationAdapter(NotificationTabFragment.NotificationList);
                NotificationTabFragment.recyclerView = (FastScrollRecyclerView) rootView.findViewById(R.id.recyclerNotification);
                NotificationTabFragment.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                NotificationTabFragment.recyclerView.setAdapter(NotificationTabFragment.adapter);
                NotificationTabFragment.getUserNotifications(getContext());
                return rootView;

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                GeneralFunctions.getSharedPreferences(getContext());
                View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
                userName = (TextView) rootView.findViewById(R.id.userName);
                getUserInfo();
                CircleImageView followerIcon, followingIcon, reqeustsIcon, settingsIcon, logoutIcon;
                followerIcon = (CircleImageView) rootView.findViewById(R.id.followersIcon);
                followingIcon = (CircleImageView) rootView.findViewById(R.id.FollowingIcon);
                reqeustsIcon = (CircleImageView) rootView.findViewById(R.id.RequestsIcon);
                settingsIcon = (CircleImageView) rootView.findViewById(R.id.settingsIcon);
                logoutIcon = (CircleImageView) rootView.findViewById(R.id.logoutIcon);
                LinearLayout showProfileLayout = (LinearLayout) rootView.findViewById(R.id.showProfileLayout);


                showProfileLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, MyProfileActivity.class);
                        startActivity(i);
                    }
                });
                followerIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, MyFollowersActivity.class);
                        startActivity(i);
                    }
                });
                followingIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, MyFollowingActivity.class);
                        startActivity(i);
                    }
                });
                reqeustsIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, MyRequestsActivity.class);
                        startActivity(i);
                    }
                });
                logoutIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout();
                    }
                });

                //     mRecyclerView = (RecyclerView) rootView.findViewById(R.id.Viewer);
                //   mRecyclerView.setHasFixedSize(true);
                // mLayoutManager = new LinearLayoutManager(getContext());
                //mRecyclerView.setLayoutManager(mLayoutManager);
                //MyAdapter settingAdapter = new MyAdapter(getContext(), myDataset, images);
                //mRecyclerView.setAdapter(settingAdapter);
                return rootView;

            } else {
                View rootView = inflater.inflate(R.layout.fragment_home, container, false);
                return rootView;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return " ";
        }
    }


}
