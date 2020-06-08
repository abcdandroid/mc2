package com.example.mechanic2.activities;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Measure;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;
import com.j256.ormlite.stmt.query.In;
import com.nightonke.wowoviewpager.Animation.ViewAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoPositionAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoRotationAnimation;
import com.nightonke.wowoviewpager.Enum.Ease;
import com.nightonke.wowoviewpager.WoWoViewPager;
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter;

import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.itangqi.waveloadingview.WaveLoadingView;

public class TestActivity extends AppCompatActivity {


    Button getLocationBtn;
    TextView locationText;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_mechanic);

/*        getLocationBtn = (Button) findViewById(R.id.getLocationBtn);
        locationText = (TextView) findViewById(R.id.locationText);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }


        *//* *//*
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });*/
    }

    void getLocation() {
        app.l("AAA");
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.001f, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String text1 = "Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude();
                    locationText.setText(text1);
                    app.l(text1);

                    try {
                        Geocoder geocoder = new Geocoder(TestActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String text = locationText.getText() + "\n" + addresses.get(0).getAddressLine(0) + ", " +
                                addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2);
                        locationText.setText(text);
                        app.l(addresses.size() + "size");
                        app.l("AAA");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    app.l(status + "BBB");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    app.l(provider + "CCC");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(TestActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            app.l("DD");
            e.printStackTrace();
        }
    }


}
