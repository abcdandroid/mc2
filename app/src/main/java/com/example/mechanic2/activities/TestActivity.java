package com.example.mechanic2.activities;


import android.app.Activity;
import android.graphics.Color;
import android.icu.util.Measure;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.itangqi.waveloadingview.WaveLoadingView;

public class TestActivity extends Activity {






    private SeekBar seekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

}
