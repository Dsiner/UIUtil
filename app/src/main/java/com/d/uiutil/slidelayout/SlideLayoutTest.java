package com.d.uiutil.slidelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.d.uiutil.R;

/**
 * SlideLayout Test
 * Created by D on 2017/5/19.
 */
public class SlideLayoutTest extends LinearLayout implements View.OnClickListener {
    private SlideLayout slSlide;

    public SlideLayoutTest(Context context) {
        this(context, null);
    }

    public SlideLayoutTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayoutTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_slide_layout_test, this, true);
        slSlide = (SlideLayout) view.findViewById(R.id.sl_slide);
        view.findViewById(R.id.tv_content).setOnClickListener(this);
        view.findViewById(R.id.tv_stick).setOnClickListener(this);
        view.findViewById(R.id.tv_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_content:
                if (slSlide.isOpen()) {
                    slSlide.close();
                }
                Toast.makeText(getContext(), "content", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_stick:
                slSlide.close();
                Toast.makeText(getContext(), "stick", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_delete:
                slSlide.close();
                Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
