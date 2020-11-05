package com.app.root.globalpunjabtv;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.github.ybq.android.spinkit.SpinKitView;

public class LiveTvActivity extends AppCompatActivity {
    VideoView videoView;

SpinKitView spin_kit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tv);
        videoView = findViewById(R.id.videoview);
        spin_kit = findViewById(R.id.spin_kit);

        spin_kit.setVisibility(View.VISIBLE);



        //  Uri uri = Uri.parse("/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4");

        Uri u = Uri.parse("https://wowzaprod207-i.akamaihd.net/hls/live/827936/2eba5b83/playlist.m3u8");
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(u);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                spin_kit.setVisibility(View.GONE);
            }
        });





    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
      //  startActivity(new Intent(LiveTvActivity.this,Home.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }

}
