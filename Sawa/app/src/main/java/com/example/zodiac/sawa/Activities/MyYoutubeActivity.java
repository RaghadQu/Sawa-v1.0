package com.example.zodiac.sawa.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.zodiac.sawa.Activities.YoutubePlayerDialogActivity;
import com.example.zodiac.sawa.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zodiac on 09/02/2017.
 */

public class MyYoutubeActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "your api kery from google";
    private static final int RQS_ErrorDialog = 1;
    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";
    public String video_id;
    String log = "";
    private YouTubePlayer youTubePlayer;
    EditText youtubeEdit;
    private YouTubePlayerView youTubePlayerFragment;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };
    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_youtube_activity_dialog);
        youTubePlayerFragment = (YouTubePlayerView) findViewById(R.id.youtubeplayerfragment);
        youTubePlayerFragment = new YouTubePlayerView(this);
        youTubePlayerFragment.initialize(api_key, this);
        youtubeEdit = (EditText) findViewById(R.id.youtubeText);
        addContentView(youTubePlayerFragment, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        youTubePlayerFragment.setVisibility(View.VISIBLE);
   //     video_id = "LwLABSm0yYc";
            if(youtubeEdit.getText()!=null){
                String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
                Pattern compiledPattern = Pattern.compile(pattern);
                Matcher matcher = compiledPattern.matcher(youtubeEdit.getText());
                if(matcher.find()){
                    video_id= matcher.group();
                }
            }



    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.cueVideo(video_id);


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
