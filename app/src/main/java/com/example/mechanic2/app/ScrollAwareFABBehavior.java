package com.example.mechanic2.app;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {
    private static final String TAG = "ScrollAwareFABBehavior";

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
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


        if (target instanceof SwipeRefreshLayout) {
            SwipeRefreshLayout swipeRefreshLayout = ((SwipeRefreshLayout) target);
            app.l(swipeRefreshLayout.getChildAt(0).getClass().getName());


            RecyclerView recyclerView = ((RecyclerView) swipeRefreshLayout.getChildAt(0));
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == 0) child.show();
                    else child.hide();
                }
            });/**/
        } else {
            RecyclerView recyclerView = ((RecyclerView) target);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == 0) child.show();
                    else child.hide();
                }
            });/**/
        }
    }


}
