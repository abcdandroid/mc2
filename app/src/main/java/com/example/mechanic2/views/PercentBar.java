package com.example.mechanic2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mechanic2.R;

public class PercentBar extends ConstraintLayout {


    public float percentDone = 0.0f;






    Context context;
    AttributeSet attrs;


    public PercentBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void setPercentDone(float percentDone) {
        this.percentDone = percentDone;
        removeAllViews();
        init(context, attrs);
        invalidate();
    }


    public float getPercentDone() {
        return percentDone;
    }


    public void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.attrs = attrs;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentBar);
        View v = inflate(context, R.layout.custom_percent_bar, this);

        TextView showPercent = v.findViewById(R.id.showPercent);
        showPercent.setText(String.valueOf((int) (percentDone * 100)).concat("%"));
        LayoutParams params = (LayoutParams) showPercent.getLayoutParams();
        params.horizontalBias = percentDone;
        showPercent.setLayoutParams(params);




        View done = v.findViewById(R.id.done);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) done.getLayoutParams();
        params2.weight = percentDone;
        done.setLayoutParams(params2);

        View remain = v.findViewById(R.id.remain);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) remain.getLayoutParams();
        params3.weight = 1 - percentDone;
        remain.setLayoutParams(params3);

        typedArray.recycle();
    }
}
