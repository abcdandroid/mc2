package com.example.mechanic2.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Measure;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;
import com.example.mechanic2.views.RecorderVisualizerView;
import com.google.android.material.appbar.AppBarLayout;
import com.j256.ormlite.stmt.query.In;
import com.nightonke.wowoviewpager.Animation.ViewAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoPositionAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoRotationAnimation;
import com.nightonke.wowoviewpager.Enum.Ease;
import com.nightonke.wowoviewpager.WoWoViewPager;
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter;
import com.tyorikan.voicerecordingvisualizer.RecordingSampler;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.itangqi.waveloadingview.WaveLoadingView;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class TestActivity extends AppCompatActivity {

/*
    Button getLocationBtn;
    TextView locationText;


    public static final int REPEAT_INTERVAL = 40;
    LocationManager locationManager;
    MediaPlayer mediaPlayer;*/

    private AppBarLayout appbar;
    private MotionLayout ml;

    private Handler mHandler = new Handler();
    private Handler handler = new Handler(); // Handler for updating the
    boolean mStartRecording = true;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn = (Button) findViewById(R.id.btn);

       /* appbar = findViewById(R.id.appbar);
        ml = findViewById(R.id.ml);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float seekPosition = -verticalOffset / (float)appBarLayout.getTotalScrollRange();
                ml.setProgress(seekPosition);
            }
        });
*/
        View.OnClickListener onClickListener = v -> {
            PackageManager pm = getPackageManager();
            String url = "https://api.whatsapp.com/send?phone=+989365487593";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        };




        btn.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            app.l("A");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }


    boolean isRecording;
/*
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
    }*/


}
