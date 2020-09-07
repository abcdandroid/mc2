package com.example.mechanic2.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;


public class MyButton extends androidx.appcompat.widget.AppCompatButton {

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButton(Context context) {
        super(context);
        init();
    }



    private void init()
    {
        if (!isInEditMode())
        {
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"fonts/b.ttf");
            this.setTypeface(tf);
        }


    }




}