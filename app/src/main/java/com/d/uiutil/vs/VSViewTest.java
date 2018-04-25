package com.d.uiutil.vs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.d.lib.ui.view.vs.VSItem;
import com.d.lib.ui.view.vs.VSView;
import com.d.uiutil.R;

/**
 * VSView Test
 * Created by D on 2017/4/20.
 */
public class VSViewTest extends LinearLayout implements VSView.OnVSItemClickListen {
    private Context context;
    private VSView vsView;

    public VSViewTest(Context context) {
        this(context, null);
    }

    public VSViewTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VSViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vs, this, true);
        vsView = (VSView) view.findViewById(R.id.vsv_vs);
        initVS();
    }

    private void initVS() {
        VSItem vsA = new VSItem("A", false);
        VSItem vsB = new VSItem("B", false);
        vsView.setStrCompareA(vsA).setStrCompareB(vsB).setPercent(-1, false);
        vsView.setOnVSItemSelectListener(this);
    }

    @Override
    public void onItemClick(int index, VSItem iitem) {
        if (index == 0) {
            vsView.setPercent(0.7f, true);
        } else {
            vsView.setPercent(0.3f, true);
        }
        Toast.makeText(context, "onSelected:" + iitem.mainText, Toast.LENGTH_SHORT).show();
    }
}
