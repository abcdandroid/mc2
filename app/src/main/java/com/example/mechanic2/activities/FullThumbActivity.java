package com.example.mechanic2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.mechanic2.R;
import com.example.mechanic2.adapters.ViewPagerAdapter;
import com.example.mechanic2.app.app;
import com.example.mechanic2.fragments.ShowThumbnailFragment;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

public class FullThumbActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViewPager vpThumbnail;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_tumbnail);
        vpThumbnail = findViewById(R.id.vp_thumbnail);

        String[] linkList = getIntent().getStringArrayExtra("linkList");
        int currentItem = getIntent().getIntExtra("currentItem", 0);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (linkList != null) {
            for (String s : linkList) {
                adapter.addFragment(ShowThumbnailFragment.newInstance(s,null));
            }
        }
        vpThumbnail.setAdapter(adapter);
        vpThumbnail.setCurrentItem(currentItem);
        app.l(linkList.length+"2222222");
        if (linkList.length>1) {
            ExtensiblePageIndicator extensiblePageIndicator =  findViewById(R.id.flexibleIndicator);
            extensiblePageIndicator.initViewPager(vpThumbnail);
        }
    }
}
