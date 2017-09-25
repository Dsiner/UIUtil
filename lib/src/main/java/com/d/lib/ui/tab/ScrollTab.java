package com.d.lib.ui.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.d.lib.ui.R;
import com.d.lib.ui.UILog;
import com.d.lib.ui.UIUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ScrollTab
 * Created by D on 2017/8/25.
 */
public class ScrollTab extends HorizontalScrollView implements View.OnClickListener, ViewPager.OnPageChangeListener {
    /**
     * TAB类型
     */
    private final int TYPE_VIEW = 0;
    private final int TYPE_VIEW_GROUP = 1;

    /**
     * 指示器类型
     */
    private final int TYPE_INDICATOR_TREND = 0;
    private final int TYPE_INDICATOR_TRANSLATION = 1;
    private final int TYPE_INDICATOR_NONE = 2;

    private int width;
    private int height;

    private Context context;
    private RectF rectF;
    private Paint paint;

    private int type;
    private boolean isAvag;
    private String strTitles;
    private int indicatorType;
    private int indicatorColor;
    private float indicatorWidth;
    private float indicatorWeight;
    private float indicatorRadius;
    private float indicatorPadding;

    private ArrayList<String> titles;
    private ArrayList<View> tabs;
    private int withTab;//tab宽度
    private int count;
    private int position = 0;
    private float positionOffset;
    private boolean isFirst = true;
    private ViewPager viewPager;
    private OnTabListener listener;

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
        type = typedArray.getInt(R.styleable.ScrollTab_stab_type, TYPE_VIEW);
        isAvag = typedArray.getBoolean(R.styleable.ScrollTab_stab_avag, false);
        strTitles = typedArray.getString(R.styleable.ScrollTab_stab_titles);
        indicatorType = typedArray.getInt(R.styleable.ScrollTab_stab_indicator_type, TYPE_INDICATOR_TREND);
        indicatorColor = typedArray.getColor(R.styleable.ScrollTab_stab_indicator_color, ContextCompat.getColor(context, R.color.colorA));
        indicatorWidth = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_width, UIUtil.dip2px(context, 30));
        indicatorWeight = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_weight, UIUtil.dip2px(context, 1));
        indicatorRadius = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_radius, UIUtil.dip2px(context, 0.5f));
        indicatorPadding = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_padding, UIUtil.dip2px(context, 5));
        typedArray.recycle();
    }

    private void init(Context context) {
        this.context = context;
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(indicatorColor);

        tabs = new ArrayList<>();
        titles = new ArrayList<>();
        if (!TextUtils.isEmpty(strTitles)) {
            String[] strs = strTitles.split(";");
            setTitles(Arrays.asList(strs));
        }
    }

    /**
     * 设置titles
     */
    public void setTitles(List<String> ts) {
        if (this.titles != null && ts != null) {
            this.titles.clear();
            this.titles.addAll(ts);
            if (!isFirst) {
                resetTab();
                invalidate();
            }
        }
    }

    private void resetTab() {
        if (titles == null || titles.size() <= 0) {
            count = 0;
            return;
        }
        count = titles.size();
        withTab = width / (count > 0 ? count : 1);
        withTab = isAvag ? withTab : ViewGroup.LayoutParams.WRAP_CONTENT;
        tabs.clear();
        removeAllViews();
        LinearLayout parent = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(isAvag ? width : LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setLayoutParams(lp);
        for (int i = 0; i < count; i++) {
            View child = getTabView(i);
            parent.addView(child);
            tabs.add(child);
        }
        addView(parent);
    }

    private View getTabView(int i) {
        if (type == TYPE_VIEW) {
            TabTextView child = new TabTextView(context);
            child.setText(titles.get(i));
            child.notifyData(i == position);
            child.setLayoutParams(new LinearLayout.LayoutParams(withTab, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setTag(i);
            child.setOnClickListener(this);
            return child;
        } else {
            TabViewGroup child = new TabViewGroup(context);
            child.setText(titles.get(i));
            child.notifyData(i == position);
            child.setLayoutParams(new LinearLayout.LayoutParams(withTab, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setTag(i);
            child.setOnClickListener(this);
            return child;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || count <= 0 || position < 0 || position > count - 1) {
            return;
        }
        if (indicatorType == TYPE_INDICATOR_TREND) {
            float left = tabs.get(position).getLeft() + indicatorPadding;
            float right = tabs.get(position).getRight() - indicatorPadding;
            if (position < count - 1) {
                float nextLeft = tabs.get(position + 1).getLeft() + indicatorPadding;
                float nextRight = tabs.get(position + 1).getRight() - indicatorPadding;
                if (positionOffset < 0.5) {
                    right = right + (nextRight - right) * positionOffset * 2;
                } else {
                    left = left + (nextLeft - left) * (positionOffset - 0.5f) * 2;
                    right = nextRight;
                }
            }
            rectF.set(left, height - indicatorWeight, right, height);
        } else if (indicatorType == TYPE_INDICATOR_TRANSLATION) {
            float left = tabs.get(position).getLeft();
            float right = tabs.get(position).getRight();
            float middle = left + (right - left) / 2;
            if (position < count - 1) {
                float nextLeft = tabs.get(position + 1).getLeft();
                float nextRight = tabs.get(position + 1).getRight();
                float nextMiddle = nextLeft + (nextRight - nextLeft) / 2;
                middle = middle + (nextMiddle - middle) * positionOffset;
            }
            left = middle - indicatorWidth / 2;
            right = middle + indicatorWidth / 2;
            rectF.set(left, height - indicatorWeight, right, height);
        } else {
            if (positionOffset == 0) {
                float left = tabs.get(position).getLeft();
                float right = tabs.get(position).getRight();
                float middle = left + (right - left) / 2;
                left = middle - indicatorWidth / 2;
                right = middle + indicatorWidth / 2;
                rectF.set(left, height - indicatorWeight, right, height);
            }
        }
        canvas.drawRoundRect(rectF, indicatorRadius, indicatorRadius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (isFirst) {
            resetTab();
            isFirst = false;
        }
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
        for (int i = 0; i < count; i++) {
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
        UILog.d("dsiner_onPageScrolled: position: " + position + " Offset: " + positionOffset);
        this.position = position;
        this.positionOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        UILog.d("dsiner_onPageSelected: position: " + position + " Offset: " + positionOffset);
        invalidate();
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
        if (isAvag) {
            return;
        }
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
