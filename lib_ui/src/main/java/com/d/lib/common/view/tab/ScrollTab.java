package com.d.lib.common.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.d.lib.common.R;
import com.d.lib.common.utils.Util;

import java.util.ArrayList;
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

    private int mWidth;
    private int mHeight;

    private Context mContext;
    private RectF mRectF;
    private Paint mPaint;

    private int mType;
    private boolean mIsAvag;
    private float mPadding; // Item内部左右预留间距
    private String mStrTitles;
    private int mIndicatorType;
    private int mIndicatorColor;
    private float mIndicatorWidth;
    private float mIndicatorWeight;
    private float mIndicatorRadius;
    private float mIndicatorPadding;

    private ArrayList<TabItem> mItems;
    private ArrayList<View> mTabs;
    private int mCount;
    private int mPosition = 0;
    private float mPositionOffset;
    private boolean mIsFirst = true;
    private ViewPager mViewPager;
    private OnTabListener mListener;

    public ScrollTab(Context context) {
        this(context, null);
    }

    public ScrollTab(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_ScrollTab);
        mType = typedArray.getInt(R.styleable.lib_pub_ScrollTab_lib_pub_stab_type, TYPE_VIEW);
        mIsAvag = typedArray.getBoolean(R.styleable.lib_pub_ScrollTab_lib_pub_stab_avag, false);
        mPadding = typedArray.getDimension(R.styleable.lib_pub_ScrollTab_lib_pub_stab_padding, Util.dip2px(context, 12));
        mStrTitles = typedArray.getString(R.styleable.lib_pub_ScrollTab_lib_pub_stab_titles);
        mIndicatorType = typedArray.getInt(R.styleable.lib_pub_ScrollTab_lib_pub_stab_indicatorType, TYPE_INDICATOR_TREND);
        mIndicatorColor = typedArray.getColor(R.styleable.lib_pub_ScrollTab_lib_pub_stab_indicatorColor, ContextCompat.getColor(context, R.color.lib_pub_color_main));
        mIndicatorWidth = typedArray.getDimension(R.styleable.lib_pub_ScrollTab_lib_pub_stab_indicatorWidth, Util.dip2px(context, 30));
        mIndicatorWeight = typedArray.getDimension(R.styleable.lib_pub_ScrollTab_lib_pub_stab_indicatorWeight, Util.dip2px(context, 1));
        mIndicatorRadius = typedArray.getDimension(R.styleable.lib_pub_ScrollTab_lib_pub_stab_indicatorRadius, Util.dip2px(context, 0.5f));
        mIndicatorPadding = typedArray.getDimension(R.styleable.lib_pub_ScrollTab_lib_pub_stab_indicatorPadding, Util.dip2px(context, 5));
        typedArray.recycle();
    }

    private void init(Context context) {
        this.mContext = context;
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setFillViewport(mIsAvag);
        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mIndicatorColor);

        mTabs = new ArrayList<>();
        mItems = new ArrayList<>();
        if (!TextUtils.isEmpty(mStrTitles)) {
            String[] strs = mStrTitles.split(";");
            for (String t : strs) {
                mItems.add(new TabItem(t, ""));
            }
        }
    }

    /**
     * 设置Titles
     */
    public void setTitles(List<String> ts) {
        if (this.mItems != null && ts != null) {
            this.mItems.clear();
            for (String t : ts) {
                this.mItems.add(new TabItem(t, ""));
            }
            if (!mIsFirst) {
                resetTab();
                invalidate();
            }
        }
    }

    private void resetTab() {
        if (mItems == null || mItems.size() <= 0 || mWidth <= 0) {
            return;
        }
        mIsFirst = false;
        mCount = mItems.size();
        mTabs.clear();
        removeAllViews();
        LinearLayout parent = new LinearLayout(mContext);
        LayoutParams lp = new LayoutParams(mIsAvag ? LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setLayoutParams(lp);
        for (int i = 0; i < mCount; i++) {
            View child = getTabView(i);
            parent.addView(child);
            mTabs.add(child);
        }
        addView(parent);
    }

    private View getTabView(int i) {
        View child;
        if (mType == TYPE_VIEW) {
            child = new TabTextView(mContext);
        } else {
            child = new TabViewGroup(mContext);
        }
        ((TabView) child).setText(mItems.get(i).title);
        ((TabView) child).setNumber(mItems.get(i).text, TextUtils.isEmpty(mItems.get(i).text) ? GONE : VISIBLE);
        if (!mIsAvag) {
            ((TabView) child).setPadding((int) mPadding);
        }
        ((TabView) child).notifyData(i == mPosition);
        child.setLayoutParams(new LinearLayout.LayoutParams(mIsAvag ? mWidth / (mCount > 0 ? mCount : 1) : ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        child.setTag(i);
        child.setOnClickListener(this);
        return child;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mCount <= 0 || mPosition < 0 || mPosition > mCount - 1) {
            return;
        }
        if (mIndicatorType == TYPE_INDICATOR_TREND) {
            float left = mTabs.get(mPosition).getLeft() + mIndicatorPadding;
            float right = mTabs.get(mPosition).getRight() - mIndicatorPadding;
            if (mPosition < mCount - 1) {
                float nextLeft = mTabs.get(mPosition + 1).getLeft() + mIndicatorPadding;
                float nextRight = mTabs.get(mPosition + 1).getRight() - mIndicatorPadding;
                if (mPositionOffset < 0.5) {
                    right = right + (nextRight - right) * mPositionOffset * 2;
                } else {
                    left = left + (nextLeft - left) * (mPositionOffset - 0.5f) * 2;
                    right = nextRight;
                }
            }
            mRectF.set(left, mHeight - mIndicatorWeight, right, mHeight);
        } else if (mIndicatorType == TYPE_INDICATOR_TRANSLATION) {
            float left = mTabs.get(mPosition).getLeft();
            float right = mTabs.get(mPosition).getRight();
            float middle = left + (right - left) / 2;
            if (mPosition < mCount - 1) {
                float nextLeft = mTabs.get(mPosition + 1).getLeft();
                float nextRight = mTabs.get(mPosition + 1).getRight();
                float nextMiddle = nextLeft + (nextRight - nextLeft) / 2;
                middle = middle + (nextMiddle - middle) * mPositionOffset;
            }
            left = middle - mIndicatorWidth / 2;
            right = middle + mIndicatorWidth / 2;
            mRectF.set(left, mHeight - mIndicatorWeight, right, mHeight);
        } else {
            float left = mTabs.get(mPosition).getLeft();
            float right = mTabs.get(mPosition).getRight();
            float middle = left + (right - left) / 2;
            left = middle - mIndicatorWidth / 2;
            right = middle + mIndicatorWidth / 2;
            mRectF.set(left, mHeight - mIndicatorWeight, right, mHeight);
        }
        canvas.drawRoundRect(mRectF, mIndicatorRadius, mIndicatorRadius, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mIsFirst) {
            resetTab();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (mViewPager == null) {
            mPosition = index;
            mPositionOffset = 0;
            onChange(index);
            adjustScrollY(index);
            invalidate();
        }
        if (mListener != null) {
            mListener.onChange(index, v);
        }
    }

    private void onChange(int position) {
        for (int i = 0; i < mCount; i++) {
            TabView view = (TabView) mTabs.get(i);
            view.notifyData(i == position);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * 设置红点
     */
    public void setNumber(int position, String text, int visibility) {
        if (position < 0 || position > mItems.size() - 1) {
            return;
        }
        mItems.get(position).text = text;
        if (position < 0 || position > mCount - 1) {
            return;
        }
        TabView view = (TabView) mTabs.get(position);
        view.setNumber(text, visibility);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mIndicatorType != TYPE_INDICATOR_NONE) {
            this.mPosition = position;
            this.mPositionOffset = positionOffset;
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        onChange(position);
        adjustScrollY(position);
        if (mIndicatorType == TYPE_INDICATOR_NONE) {
            this.mPosition = position;
            invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void adjustScrollY(int position) {
        if (mIsAvag) {
            return;
        }
        View v = mTabs.get(position);
        int dr = v.getRight() - (mWidth + getScrollX());
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
        this.mListener = l;
    }
}
