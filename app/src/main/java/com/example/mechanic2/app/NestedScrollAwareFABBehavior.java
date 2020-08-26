package com.example.mechanic2.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class NestedScrollAwareFABBehavior extends FloatingActionButton.Behavior {
    private static final String TAG = "ScrollAwareFABBehavior";

    public NestedScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }


    @Override
    public boolean layoutDependsOn(@NotNull CoordinatorLayout parent, @NotNull FloatingActionButton child, @NotNull View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public void onNestedScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull FloatingActionButton child, @NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, type, consumed);

        NestedScrollView target1 = (NestedScrollView) target;
        target1.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {

                app.l("asssdd+i3BBB" + i3);
                /*
                app.l("asssdd+i" + i);
                app.l("asssdd+i1NNN" + i1);dy
                app.l("asssdd+i2" + i2);
                */
            }
        });


    }


}
