package com.example.mechanic2.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class MyAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {


    public MyAutoCompleteTextView(Context context) {
        super(context);init();
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);init();
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init();
    }

    private void init()
    {
        if (!isInEditMode())
        {
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"b.ttf");
            this.setTypeface(tf);
        }


    }




}