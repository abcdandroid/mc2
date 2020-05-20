package com.example.mechanic2.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.app.app;
import com.example.mechanic2.interfaces.OnTagCrossClickListener;

public class TagView extends RelativeLayout {
    Context context;
    AttributeSet attrs;
    private String tag;
    ImageView removeTag;
    OnTagCrossClickListener onTagCrossClickListener;

    public void setOnTagCrossClickListener(OnTagCrossClickListener onTagCrossClickListener) {
        this.onTagCrossClickListener = onTagCrossClickListener;
    }

    public OnTagCrossClickListener getOnTagCrossClickListener() {
        return onTagCrossClickListener;
    }

    public TagView(Context context) {
        super(context);
        init(context, null);
    }


    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    public void setTagTitle(String title) {
        this.tag = title;
        removeAllViews();
        init(context, attrs);
        invalidate();
    }

    public String getTagTitle() {
        return this.tag;
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.attrs = attrs;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        View v = inflate(context, R.layout.view_tag, this);

        TextView tagTitle = v.findViewById(R.id.tag_title);
        tagTitle.setText(tag);
        ImageView removeTag = v.findViewById(R.id.remove_tag);


        typedArray.recycle();
    }


}
