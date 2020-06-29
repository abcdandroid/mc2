package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends AppCompatActivity {

    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String entranceId = SharedPrefUtils.getStringData("entranceId");
        app.l(entranceId+"fggsdgf");
        if (!entranceId.equals("-1")) {
            Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent1);
        } else   {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
    }




}
