package com.example.mechanic2.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.SharedPrefUtils;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.AdminFragment;
import com.example.mechanic2.fragments.MainPageFragment;
import com.example.mechanic2.fragments.MechanicFragment;
import com.example.mechanic2.fragments.QuestionFragment;
import com.example.mechanic2.fragments.StoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    ViewPager viewpager;
    BottomNavigationView bottomNavigationView;
    boolean searchPressed;

    boolean isFabPressed;
    private SweetAlertDialog sweetAlertDialog;

    //پرداخت مبلغ فونت ایران سنس به کار رفته
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        viewpager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (checkPermission())
            myAction();

    }


    private void myAction() {
        app.l("type is " + SharedPrefUtils.getIntData("type"));

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setConfirmText("شکیبا باشید");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("mpd"));
        LocalBroadcastManager.getInstance(this).registerReceiver(dataReceiver, new IntentFilter("dataCount"));

        app.l("main" + isFabPressed);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MechanicFragment());
        adapter.addFragment(new AdminFragment());
        adapter.addFragment(new QuestionFragment());
        adapter.addFragment(new StoreFragment());
        adapter.addFragment(new MainPageFragment());
        viewpager.setOffscreenPageLimit((adapter.getCount() > 1 ? adapter.getCount() - 1 : 1));
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(4);
        bottomNavigationView.setSelectedItemId(R.id.main_page);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.mechanics);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.questions);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.store);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.main_page);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.mechanics:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.home:
                    viewpager.setCurrentItem(1);
                    break;
                case R.id.questions:
                    viewpager.setCurrentItem(2);
                    break;
                case R.id.store:
                    viewpager.setCurrentItem(3);
                    break;
                case R.id.main_page:
                    viewpager.setCurrentItem(4);
                    break;
            }
            return false;
        });
    }

    private Boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

            if (result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
            }
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                myAction();
            } else {
                app.t("not granted");
            }
        } else if (requestCode == MechanicFragment.REQUEST_CODE) {
            Intent intent = new Intent("forGps")  /**/;
            LocalBroadcastManager.getInstance(Application.getContext()).sendBroadcast(intent);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("field", 0) == 3) {
                viewpager.setCurrentItem(3);
                bottomNavigationView.setSelectedItemId(R.id.store);
            } else if (intent.getIntExtra("field", 0) == 1) {
                viewpager.setCurrentItem(0);
                bottomNavigationView.setSelectedItemId(R.id.mechanics);
            } else if (intent.getIntExtra("field", 0) == 2) {
                viewpager.setCurrentItem(2);
                bottomNavigationView.setSelectedItemId(R.id.questions);
            } else if (intent.getIntExtra("field", 0) == 4) {
                viewpager.setCurrentItem(1);
                bottomNavigationView.setSelectedItemId(R.id.home);
            }


        }
    };
    int dataCount;
    BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            app.l("fggsdgf3");
            dataCount++;
            if (dataCount == 5) {
                sweetAlertDialog.dismissWithAnimation();
            }
            app.l("dataaaccc" + dataCount + intent.getStringExtra("ref"));
        }
    };


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(Application.getContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(Application.getContext()).unregisterReceiver(dataReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
