package com.d.ui.view.clock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.view.clock.ClockSetView;
import com.d.ui.view.R;

/**
 * ClockSetActivity
 * Created by D on 2018/8/8.
 */
public class ClockSetActivity extends Activity implements View.OnClickListener {
    private TextView tv_hour, tv_minute;
    private ClockSetView csv_clockset;
    private int mHour, mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockset);
        bindView();
        init();
    }

    private void bindView() {
        tv_hour = ViewHelper.findViewById(this, R.id.tv_hour);
        tv_minute = ViewHelper.findViewById(this, R.id.tv_minute);
        csv_clockset = ViewHelper.findViewById(this, R.id.csv_clockset);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left,
                R.id.tv_hour, R.id.tv_minute);
    }

    private void init() {
        csv_clockset.setMode(ClockSetView.MODE_HOUR, mHour);
        tv_hour.setText(getFormatTime(mHour));
        tv_minute.setText(getFormatTime(mMinute));
        csv_clockset.setOnSelectListener(new ClockSetView.OnSelectListener() {
            @Override
            public void onSelect(int mode, int value) {
                if (mode == ClockSetView.MODE_HOUR) {
                    mHour = value;
                    tv_hour.setText(getFormatTime(value));
                } else if (mode == ClockSetView.MODE_MINUTE) {
                    mMinute = value;
                    tv_minute.setText(getFormatTime(value));
                }
            }
        });
    }

    @NonNull
    private String getFormatTime(int value) {
        return value >= 10 ? String.valueOf(value) : "0" + value;
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (R.id.iv_title_left == resId) {
            finish();

        } else if (R.id.tv_hour == resId) {
            tv_hour.setTextColor(Color.parseColor("#ffffff"));
            tv_minute.setTextColor(Color.parseColor("#DFB5B8"));
            csv_clockset.setMode(ClockSetView.MODE_HOUR, mHour);
            tv_hour.setText(getFormatTime(mHour));
            tv_minute.setText(getFormatTime(mMinute));

        } else if (R.id.tv_minute == resId) {
            tv_hour.setTextColor(Color.parseColor("#DFB5B8"));
            tv_minute.setTextColor(Color.parseColor("#ffffff"));
            csv_clockset.setMode(ClockSetView.MODE_MINUTE, mMinute);
            tv_hour.setText(getFormatTime(mHour));
            tv_minute.setText(getFormatTime(mMinute));
        }
    }
}
