package com.example.mechanic2.activities;


import android.icu.util.Measure;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;
import com.nightonke.wowoviewpager.Animation.ViewAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoPositionAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoRotationAnimation;
import com.nightonke.wowoviewpager.Enum.Ease;
import com.nightonke.wowoviewpager.WoWoViewPager;
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TestActivity extends AppCompatActivity {

    private NewEntranceActivity.USER_TYPE user_type;

    private ImageView gear;
    SweetAlertDialog sweetAlertDialogSendType;
    WoWoViewPager wowo;
    WoWoViewPager wowoGear;
    private ImageView imgMechanic;
    public int imgHeight;
    public int imgHeightNormal;
    private ImageView imgNormal;

    private LinearLayout txtNormal1;
    private LinearLayout txtNormal2;
    private LinearLayout txtNormal3;
    int wowoHeight;
    private LinearLayout txtMechanic1;
    private LinearLayout txtMechanic2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_good);



    }


}
