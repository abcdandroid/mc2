package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mechanic2.R;
import com.example.mechanic2.app.SharedPrefUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String entranceId = SharedPrefUtils.getStringData("entranceId");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (entranceId != null && entranceId.length() > 0) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else if (entranceId == null) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        }, 2000);


    }
}
