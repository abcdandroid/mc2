package com.example.mechanic2.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.example.mechanic2.R;


public class ExoVideoActivity extends AppCompatActivity {


    VideoView videoView;
    int currentTime;
    String path;
    int id;
    int duration;
    int seekTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView);
        path = getIntent().getExtras().getString(getString(R.string.pth));
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        videoView.pause();
        videoView.release();
        super.onBackPressed();
    }


}