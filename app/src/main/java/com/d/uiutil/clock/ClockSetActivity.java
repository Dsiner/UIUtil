package com.d.uiutil.clock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.d.lib.ui.view.clock.ClockSetView;
import com.d.uiutil.R;

/**
 * ClockSetActivity
 * Created by D on 2018/8/8.
 */
public class ClockSetActivity extends Activity implements View.OnClickListener {
    private TextView tvHour, tvMinute;
    private ClockSetView csvClockset;
    private int hour, minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockset);
        bindView();
        init();
    }

    private void bindView() {
        tvHour = (TextView) findViewById(R.id.tv_hour);
        tvMinute = (TextView) findViewById(R.id.tv_minute);
        csvClockset = (ClockSetView) findViewById(R.id.csv_clockset);

        tvHour.setOnClickListener(this);
        tvMinute.setOnClickListener(this);
    }

    private void init() {
        csvClockset.setMode(ClockSetView.MODE_HOUR, hour);
        tvHour.setText(getFamatTime(hour));
        tvMinute.setText(getFamatTime(minute));
        csvClockset.setOnSelectListener(new ClockSetView.OnSelectListener() {
            @Override
            public void onSelect(int mode, int value) {
                if (mode == ClockSetView.MODE_HOUR) {
                    hour = value;
                    tvHour.setText(getFamatTime(value));
                } else if (mode == ClockSetView.MODE_MINUTE) {
                    minute = value;
                    tvMinute.setText(getFamatTime(value));
                }
            }
        });
    }

    @NonNull
    private String getFamatTime(int value) {
        return value >= 10 ? String.valueOf(value) : "0" + value;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hour:
                tvHour.setTextColor(Color.parseColor("#ffffff"));
                tvMinute.setTextColor(Color.parseColor("#DFB5B8"));
                csvClockset.setMode(ClockSetView.MODE_HOUR, hour);
                tvHour.setText(getFamatTime(hour));
                tvMinute.setText(getFamatTime(minute));
                break;
            case R.id.tv_minute:
                tvHour.setTextColor(Color.parseColor("#DFB5B8"));
                tvMinute.setTextColor(Color.parseColor("#ffffff"));
                csvClockset.setMode(ClockSetView.MODE_MINUTE, minute);
                tvHour.setText(getFamatTime(hour));
                tvMinute.setText(getFamatTime(minute));
                break;
        }
    }
}
