package com.d.ui.view.progress;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.d.lib.ui.view.progress.CircleProgressBar;
import com.d.ui.view.R;

/**
 * CircleProgressTest
 * Created by D on 2017/11/1.
 */
public class CircleProgressLayout extends LinearLayout {

    public CircleProgressLayout(Context context) {
        super(context);
        init(context);
    }

    public CircleProgressLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CircleProgressLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_progress_circle, this, true);
        bindView();
    }

    private void bindView() {
        final CircleProgressBar[] cpbCircles = new CircleProgressBar[]{
                (CircleProgressBar) findViewById(R.id.cpb_progress_progress),
                (CircleProgressBar) findViewById(R.id.cpb_progress_pending),
                (CircleProgressBar) findViewById(R.id.cpb_progress_error)};
        cpbCircles[0].setState(CircleProgressBar.STATE_PROGRESS).progress(0.36f);
        cpbCircles[1].setState(CircleProgressBar.STATE_PENDING);
        cpbCircles[2].setState(CircleProgressBar.STATE_ERROR);
        cpbCircles[1].setOnClickListener(new CircleProgressBar.OnClickListener() {
            @Override
            public void onRestart() {
                cpbCircles[1].setState(CircleProgressBar.STATE_PROGRESS).progress(0.36f);
            }

            @Override
            public void onResume() {
                cpbCircles[1].setState(CircleProgressBar.STATE_PROGRESS);
            }

            @Override
            public void onPause() {
                cpbCircles[1].setState(CircleProgressBar.STATE_PENDING);
            }
        });
        cpbCircles[2].setOnClickListener(new CircleProgressBar.OnClickListener() {
            public void onRestart() {
                cpbCircles[2].setState(CircleProgressBar.STATE_PROGRESS).progress(0.36f);
            }

            @Override
            public void onResume() {
                cpbCircles[2].setState(CircleProgressBar.STATE_PROGRESS);
            }

            @Override
            public void onPause() {
                cpbCircles[2].setState(CircleProgressBar.STATE_PENDING);
            }
        });
    }
}
