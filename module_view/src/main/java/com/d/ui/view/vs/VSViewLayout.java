package com.d.ui.view.vs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.d.lib.ui.view.vs.VSItem;
import com.d.lib.ui.view.vs.VSView;
import com.d.ui.view.R;

/**
 * VSView Test
 * Created by D on 2017/4/20.
 */
public class VSViewLayout extends LinearLayout implements VSView.OnVSClickListener {
    private Context context;
    private VSView vsv_vs;

    public VSViewLayout(Context context) {
        super(context);
        init(context);
    }

    public VSViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VSViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vs, this, true);
        vsv_vs = (VSView) view.findViewById(R.id.vsv_vs);
        initVS();
    }

    private void initVS() {
        VSItem vsA = new VSItem("A", false);
        VSItem vsB = new VSItem("B", false);
        vsv_vs.setCompareA(vsA).setCompareB(vsB).setPercent(-1, false);
        vsv_vs.setOnVSClickListener(this);
    }

    @Override
    public void onItemClick(int index, VSItem item) {
        if (index == 0) {
            vsv_vs.setPercent(0.7f, true);
        } else {
            vsv_vs.setPercent(0.3f, true);
        }
        Toast.makeText(context, "Selected: " + item.mainText, Toast.LENGTH_SHORT).show();
    }
}
