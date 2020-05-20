package com.example.mechanic2.interfaces;

import android.view.View;

import com.example.mechanic2.models.AdminMedia;

public interface OnClickListener {
    void onDownloadStateClick(AdminMedia adminMedia, View viewHolder);
    void onDownloadCompleteClick(AdminMedia adminMedia, View viewHolder);
}
