package com.d.lib.ui.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.d.lib.ui.R;
import com.d.lib.ui.UILog;
import com.d.lib.ui.UIUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ScrollTab
 * Created by D on 2017/8/25.
 */
public class ScrollTab extends HorizontalScrollView implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private int width;
    private int height;

    private Context context;
    private RectF rectF;
    private Paint paint;

    private int colorStroke;
    private int duration;
    private float weight;
    private float rectRadius;
    private float padding;

    private boolean isAvag;
    private String strTitles;
    private ArrayList<String> titles;
    private ArrayList<View> tabs;
    private int position = 0;
    private boolean isFirst = true;
    private Scroller scroller;
    private ViewPager viewPager;
    private OnTabListener listener;
    private float positionOffset;

    public ScrollTab(Context context) {
        this(context, null);
    }

    public ScrollTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollTab);
        strTitles = typedArray.getString(R.styleable.ScrollTab_stab_titles);
        colorStroke = typedArray.getColor(R.styleable.ScrollTab_stab_color, context.getResources().getColor(R.color.colorA));
        weight = typedArray.getDimension(R.styleable.ScrollTab_stab_weight, UIUtil.dip2px(context, 1));
        rectRadius = typedArray.getDimension(R.styleable.ScrollTab_stab_radius, UIUtil.dip2px(context, 0.5f));
        padding = typedArray.getDimension(R.styleable.ScrollTab_stab_padding, UIUtil.dip2px(context, 5));
        duration = typedArray.getInt(R.styleable.ScrollTab_stab_duration, 200);
        typedArray.recycle();
    }

    private void init(Context context) {
        this.context = context;
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        scroller = new Scroller(context);
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorStroke);

        titles = new ArrayList<>();
        tabs = new ArrayList<>();
        if (!TextUtils.isEmpty(strTitles)) {
            String[] strs = strTitles.split(";");
            titles.addAll(Arrays.asList(strs));
        }
        resetTab();
    }

    private void resetTab() {
        if (titles == null || titles.size() <= 0) {
            return;
        }
        tabs.clear();
        removeAllViews();
        LinearLayout parent = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setLayoutParams(lp);
        fillChild(parent);
        addView(parent);
    }

    private void fillChild(LinearLayout parent) {
        int size = titles.size();
        for (int i = 0; i < size; i++) {
            TabTextView child = new TabTextView(context);
            child.setText(titles.get(i));
            child.notifyData(i == position);
            child.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setTag(i);
            child.setOnClickListener(this);

//            TabViewGroup child = new TabViewGroup(context);
//            child.setText(titles.get(i));
//            child.notifyData(i == position);
//            child.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            child.setTag(i);
//            child.setOnClickListener(this);

            parent.addView(child);
            tabs.add(child);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabs.size() <= 0) {
            return;
        }
        float left = tabs.get(position).getLeft() + padding;
        float right = tabs.get(position).getRight() - padding;
        if (positionOffset > 0 && position < tabs.size() - 1) {
            float nextLeft = tabs.get(position + 1).getLeft() + padding;
            float nextRight = tabs.get(position + 1).getRight() - padding;
            if (positionOffset < 0.5) {
                right = right + (nextRight - right) * positionOffset * 2;
            } else {
                left = left + (nextLeft - left) * (positionOffset - 0.5f) * 2;
                right = nextRight;
            }
        }
        rectF.set(left, height - weight, right, height);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (viewPager == null) {
            position = index;
            positionOffset = 0;
            onChange(index);
            adjustScrollY();
            invalidate();
        }
        if (listener != null) {
            listener.onChange(index, v);
        }
    }

    private void onChange(int position) {
        int size = tabs.size();
        for (int i = 0; i < size; i++) {
            TabView view = (TabView) tabs.get(i);
            view.notifyData(i == position);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.position = position;
        this.positionOffset = positionOffset;
        UILog.d("dsiner_onPageScrolled: position: " + position + " Offset: " + positionOffset);
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        UILog.d("dsiner_onPageSelected: position: " + position + " Offset: " + positionOffset);
        onChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        UILog.d("dsiner_onPageScrollStateChanged: state: " + state);
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            adjustScrollY();
        }
    }

    private void adjustScrollY() {
        View v = tabs.get(position);
        int dr = v.getRight() - (width + getScrollX());
        int dl = getScrollX() - v.getLeft();
        if (dr > 0) {
            smoothScrollBy(dr, 0);
        } else if (dl > 0) {
            smoothScrollBy(-dl, 0);
        }
    }

    public interface OnTabListener {
        void onChange(int position, View v);
    }

    public void setOnTabListener(OnTabListener l) {
        this.listener = l;
    }
}
