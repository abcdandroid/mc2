package com.example.mechanic2.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


public class MyEditText extends androidx.appcompat.widget.AppCompatEditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context) {
        super(context);
        init();
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