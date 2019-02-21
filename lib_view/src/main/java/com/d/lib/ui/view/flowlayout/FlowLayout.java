package com.d.lib.ui.view.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.ui.view.R;

/**
 * FlowLayout
 * Created by D on 2018/7/11.
 */
public class FlowLayout extends ViewGroup {
    private final static int ROW_COUNT = 4;
    private final float mLoadFactor = 1.75f;

    private int mWidth;
    private int mHeight;

    private float mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;
    private float mDividerWidth, mDividerHeight;

    private FlowLayoutAdapter mAdapter;
    private SparseArray<View> mItemViews;

    public FlowLayout(Context context) {
        super(context);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_FlowLayout);
        float padding = typedArray.getDimension(R.styleable.lib_ui_view_FlowLayout_lib_ui_view_flow_padding, 0);
        mPaddingLeft = typedArray.getDimension(R.styleable.lib_ui_view_FlowLayout_lib_ui_view_flow_paddingLeft, padding);
        mPaddingTop = typedArray.getDimension(R.styleable.lib_ui_view_FlowLayout_lib_ui_view_flow_paddingTop, padding);
        mPaddingRight = typedArray.getDimension(R.styleable.lib_ui_view_FlowLayout_lib_ui_view_flow_paddingRight, padding);
        mPaddingBottom = typedArray.getDimension(R.styleable.lib_ui_view_FlowLayout_lib_ui_view_flow_paddingBottom, padding);
        mDividerWidth = typedArray.getDimension(R.styleable.lib_ui_view_FlowLayout_lib_ui_view_flow_dividerWidth, 0);
        mDividerHeight = typedArray.getDimension(R.styleable.lib_ui_view_FlowLayout_lib_ui_view_flow_dividerHeight, 0);
        typedArray.recycle();
    }

    private void init() {
        this.mItemViews = new SparseArray<>();
    }

    private void reset() {
        removeAllViews();
        int recyclers = mItemViews.size();
        int size = mAdapter != null ? mAdapter.getCount() : 0;
        if (size <= 0) {
            mItemViews.clear();
        } else if (size + (int) (ROW_COUNT * mLoadFactor) < recyclers) {
            for (int i = size + (int) (ROW_COUNT * mLoadFactor); i < recyclers; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mItemViews.removeAt(i);
                } else {
                    mItemViews.delete(i);
                }
            }
        }

        for (int i = 0; i < size; i++) {
            View child = mAdapter.getView(i, mItemViews.get(i), this);
            mItemViews.put(i, child);
            addChild(this, child);
        }
        requestLayout();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(lp);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        final boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY;

        if (count <= 0 || measureMatchParentChildren) {
            // Not support mode
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
            return;
        }

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int left = (int) mPaddingLeft;
        int top = (int) mPaddingTop;
        int maxHeightRow = 0;
        for (int i = 0; i < count; i++) {
            // Measure size for each child view in the ViewGroup
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            final int childWidthMeasureSpec;
            if (lp.width == LayoutParams.MATCH_PARENT) {
                final int width = Math.max(0, MeasureSpec.getSize(widthMeasureSpec));
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (width - mPaddingLeft - mPaddingRight),
                        MeasureSpec.EXACTLY);
            } else {
                childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                        (int) (mPaddingLeft + mPaddingRight), lp.width);
            }

            final int childHeightMeasureSpec;
            if (lp.height == LayoutParams.MATCH_PARENT) {
                final int height = Math.max(0, MeasureSpec.getSize(heightMeasureSpec));
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (height - mPaddingTop - mPaddingBottom),
                        MeasureSpec.EXACTLY);
            } else {
                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                        (int) (mPaddingTop + mPaddingBottom), lp.height);
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            if (!(left == (int) mPaddingLeft && width == mWidth - mPaddingLeft - mPaddingRight)
                    && left + width + mDividerWidth > mWidth - mPaddingRight) {
                left = (int) mPaddingLeft;
                top += maxHeightRow + mDividerHeight;
                maxHeightRow = 0;
            }
            left += width + mDividerWidth;
            maxHeightRow = Math.max(maxHeightRow, height);
        }
        final int totalHeight = (int) (top + maxHeightRow + mPaddingBottom);

        mHeight = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY ? totalHeight
                : MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        if (count <= 0) {
            return;
        }

        int left = (int) mPaddingLeft;
        int top = (int) mPaddingTop;
        int maxHeightRow = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            if (!(left == (int) mPaddingLeft && width == mWidth - mPaddingLeft - mPaddingRight)
                    && left + width + mDividerWidth > mWidth - mPaddingRight) {
                left = (int) mPaddingLeft;
                top += maxHeightRow + mDividerHeight;
                maxHeightRow = 0;
            }
            // Layout horizontally for each child view in the ViewGroup
            child.layout(left, top, left + width, top + height);
            left += width + mDividerWidth;
            maxHeightRow = Math.max(maxHeightRow, height);
        }
    }

    private void addChild(ViewGroup group, View child) {
        if (group == null || child == null) {
            return;
        }
        if (child.getParent() != null) {
            ((ViewGroup) child.getParent()).removeView(child);
        }
        group.addView(child);
    }

    public void setAdapter(FlowLayoutAdapter adapter) {
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(new FlowLayoutAdapter.DataSetObserver() {
            @Override
            public void notifyChanged() {
                reset();
            }
        });
        reset();
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
