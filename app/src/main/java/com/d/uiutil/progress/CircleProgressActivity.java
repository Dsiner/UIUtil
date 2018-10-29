package com.d.uiutil.progress;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.d.lib.ui.view.progress.CircleProgressBar;
import com.d.uiutil.R;

/**
 * CircleProgressActivity
 * Created by D on 2017/11/1.
 */
public class CircleProgressActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_circle);
        initView();
    }

    private void initView() {
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
