package com.example.mechanic2.activities;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mechanic2.R;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;


public class VideoActivity extends AppCompatActivity {


    VideoView videoView;
    int currentTime;
    String path;
    int id;
    int duration;
    int seekTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);
        videoView=findViewById(R.id.videoView);
        path = getIntent().getExtras().getString("path");
        id = getIntent().getExtras().getInt("id");
        app.t(id + "");

        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(path));
        duration = mp.getDuration();
        mp.release();

        videoView.setVideoPath(path);
        //app.t(videoView.getDuration()+"%%");


        if (duration == SharedPrefUtils.getIntData("currentTime_id:" + id)) {
            seekTime = 0;
            SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("currentTime_id:" + id);
        } else seekTime = SharedPrefUtils.getIntData("currentTime_id:" + id);


        videoView.seekTo(seekTime);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
        app.l("onCreate" + videoView.getDuration());
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.l("onResume" + videoView.getDuration());
        //videoView.seekTo(SharedPrefUtils.getIntData("currentTime_id:" + id));
        //app.t(currentTime+"onResume");

    }


    @Override
    protected void onStart() {
        super.onStart();
        app.l("onStart" + videoView.getDuration());

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        currentTime = videoView.getCurrentPosition();
        SharedPrefUtils.saveData("currentTime_id:" + id, currentTime);
        app.l("onConfigurationChanged" + videoView.getDuration());

    }


    @Override
    protected void onPause() {
        super.onPause();
        currentTime = videoView.getCurrentPosition();
        SharedPrefUtils.saveData("currentTime_id:" + id, currentTime);
        app.l("onPause" + videoView.getDuration());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        app.l("onRestart" + videoView.getDuration());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //SharedPrefUtils.saveData("currentTime_id:" + id , 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.t(duration + "???" + SharedPrefUtils.getIntData("currentTime_id:" + id));
        if (duration == SharedPrefUtils.getIntData("currentTime_id:" + id)) {
            seekTime = 0;
            SharedPrefUtils.getSharedPrefEditor(SharedPrefUtils.PREF_APP).remove("currentTime_id:" + id).apply();
            app.t("removed");
        }
    }
}