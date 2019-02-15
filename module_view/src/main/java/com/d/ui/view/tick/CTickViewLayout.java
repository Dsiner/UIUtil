package com.d.ui.view.tick;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.d.lib.ui.view.tick.CTickView;
import com.d.lib.ui.view.tick.STickView;
import com.d.ui.view.R;

/**
 * CTickView Test
 * Created by D on 2017/4/20.
 */
public class CTickViewLayout extends LinearLayout implements View.OnClickListener {
    private CTickView cTickView;
    private STickView sTickView;

    public CTickViewLayout(Context context) {
        super(context);
        init(context);
    }

    public CTickViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public CTickViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_ctick, this, true);
        cTickView = (CTickView) view.findViewById(R.id.ctv_tick);
        sTickView = (STickView) view.findViewById(R.id.stv_tick);
        view.findViewById(R.id.btn_ctv_start).setOnClickListener(this);
        view.findViewById(R.id.btn_ctv_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.btn_ctv_start) {
            cTickView.start();
            sTickView.start();
        } else if (resId == R.id.btn_ctv_stop) {
            cTickView.stop();
            sTickView.stop();
        }
    }
}
