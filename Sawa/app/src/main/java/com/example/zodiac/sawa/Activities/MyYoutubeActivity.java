package com.example.zodiac.sawa.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

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

    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";
    public String video_id;
    String log = "";
    int youtubeFlag = 0;
    String youtubeBundleSong;
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
      //  youTubePlayerFragment = new YouTubePlayerView();
        youTubePlayerFragment = new YouTubePlayerView(this);
        youtubeEdit = (EditText) findViewById(R.id.youtubeText);
        Bundle b = getIntent().getExtras();
        int Id = -1; // or other values
        if (b != null) {
            youtubeBundleSong = b.getString("youtubeSongUrl");
            youtubeEdit.setText(youtubeBundleSong);
        }

        if (youtubeEdit.getText().toString() != null) {
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(youtubeEdit.getText());
            if (matcher.find()) {
                video_id = matcher.group();
            }
        }

        youTubePlayerFragment.initialize(api_key, this);
        addContentView(youTubePlayerFragment, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        youTubePlayerFragment.setVisibility(View.VISIBLE);
        youtubeFlag=0;
        youtubeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }
            @Override
            public void afterTextChanged(Editable editable) {

                String pattern = "https://m.youtube.com/watch?v=";
                String pattern1 = "https://www.youtube.com/watch?v=";
                String s = String.valueOf(youtubeEdit.getText());
                if(s.trim().equals("")){
                  //
                    youTubePlayerFragment.setVisibility(View.GONE);
                    ((ViewGroup)youTubePlayerFragment.getParent()).removeView(youTubePlayerFragment);
                }
                else {
                    int i = s.indexOf(pattern);
                    int j = s.indexOf(pattern1);
                    Log.d("youtubeFlag", "i " + i + "j " + j + "Falg " + youtubeFlag);

                    if ((i >= 0 && youtubeFlag == 0) || (j >= 0 && youtubeFlag == 0)) {
                        String[] split = s.split("v=");
                        video_id = split[1];
                        youTubePlayerFragment.initialize(api_key, MyYoutubeActivity.this);
                        youTubePlayerFragment.setVisibility(View.VISIBLE);
                        Log.d("YoutubeActivity", " " + video_id + " " + i + " " + j);
                        //  youTubePlayerFragment.cut
                        youtubeFlag = 1;
                    }

                }
            }
        });


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
