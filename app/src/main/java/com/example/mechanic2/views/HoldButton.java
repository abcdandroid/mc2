package com.example.mechanic2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.mechanic2.R;
import com.example.mechanic2.app.app;

public class HoldButton extends androidx.appcompat.widget.AppCompatButton {
    public HoldButton(Context context) {
        super(context);
        setOnClickListener(listener);
    }

    public HoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                app.l("t");
                return true;

            case MotionEvent.ACTION_UP:
                app.l("c");
                performClick();
                return true;
        }
        return false;
    }
    OnClickListener listener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            app.l("AVB9");
        }
    };



    public boolean performClick() {
        super.performClick();
        //launchMissile();
        return true;
    }

    private void launchMissile() {
        app.l("AA");
    }
}
