package com.example.mechanic2.interfaces;

import android.os.Parcelable;
import android.view.View;

import java.io.Serializable;

public interface OnViewPagerClickListener extends Parcelable {
    void onViewPagerClick(View view);
}
