package com.d.lib.common.view.btb;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.d.lib.common.R;
import com.d.lib.common.component.lv.CommonHolder;
import com.d.lib.common.utils.Util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * BottomThirdBar
 * Created by D on 2018/7/11.
 */
public class BottomThirdBar extends ViewGroup implements View.OnClickListener {
    private final static int ROW_COUNT = 4;
    private final float mLoadFactor = 1.75f;
    private final int mDuration = 1000;

    private int mWidth;
    private int mHeight;

    private Context mContext;
    private Scroller mScroller;
    private Adapter mAdapter;
    private SparseArray<View> mItemViews;
    private List<List<View>> mPageViews;
    private int mWidthNext;
    private int mLeftBorder, mRightBorder;

    public BottomThirdBar(Context context) {
        super(context);
        init(context);
    }

    public BottomThirdBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomThirdBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mScroller = new Scroller(context);
        this.mItemViews = new SparseArray<>();
        this.mPageViews = new ArrayList<>();
        this.mWidthNext = Util.dip2px(context, 36);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        removeAllViews();
        mPageViews.clear();
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

        final boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;

        if (size <= 0 || measureMatchParentChildren) {
            // Not support mode
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        if (mAdapter != null) {
            float weight = 0;
            List<View> views = null;
            for (int i = 0; i < size; i++) {
                if (views == null || weight >= 1
                        || i + 1 < size && weight + mAdapter.getItem(i + 1).weight > 1) {
                    weight = 0;
                    views = new ArrayList<>();
                    mPageViews.add(views);
                }
                View child = mAdapter.getView(i, mItemViews.get(i), this);
                mItemViews.put(i, child);
                views.add(child);
                Item item = mAdapter.getItem(i);
                weight += item.weight;
            }
        }

        final int childHeight = Math.max(0, getMeasuredHeight());
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight,
                MeasureSpec.EXACTLY);
        for (int i = 0; i < mPageViews.size(); i++) {
            if (i > 0) {
                final int page = i - 1;
                // Left or Right
                final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidthNext,
                        MeasureSpec.EXACTLY);
                View right = LayoutInflater.from(mContext).inflate(getRightLayout(), null);
                right.setTag(Integer.valueOf(page));
                right.setOnClickListener(this);
                addChild(this, right);
                right.measure(childWidthMeasureSpec, childHeightMeasureSpec);

                View left = LayoutInflater.from(mContext).inflate(getLeftLayout(), null);
                left.setTag(Integer.valueOf(-(page + 1)));
                left.setOnClickListener(this);
                addChild(this, left);
                left.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }

            List<View> views = mPageViews.get(i);
            for (int j = 0; j < views.size(); j++) {
                addChild(this, views.get(j));
            }
        }

        if (mPageViews.size() <= 1) {
            final int count = mPageViews.get(0).size();
            float totalWeight = 0;
            for (int i = 0; i < count; i++) {
                totalWeight += mAdapter.getItem(i).weight;
            }
            totalWeight = totalWeight != 0 ? totalWeight : 1;
            for (int i = 0; i < count; i++) {
                final int itemWith = (int) (1f * mWidth * mAdapter.getItem(i).weight / totalWeight);
                // Measure size for each child view in the ViewGroup
                final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWith,
                        MeasureSpec.EXACTLY);
                mPageViews.get(0).get(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        } else {
            for (int i = 0, index = 0; i < mPageViews.size(); i++) {
                List<View> views = mPageViews.get(i);
                for (int j = 0; j < views.size(); j++, index++) {
                    int itemWith = i == 0 || i == mPageViews.size() - 1 ?
                            (int) (1f * (mWidth - mWidthNext) * mAdapter.getItem(index).weight)
                            : (int) (1f * (mWidth - mWidthNext * 2) * mAdapter.getItem(index).weight);
                    // Measure size for each child view in the ViewGroup
                    final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWith,
                            MeasureSpec.EXACTLY);
                    views.get(j).measure(childWidthMeasureSpec, childHeightMeasureSpec);
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        if (count <= 0) {
            return;
        }

        int left = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            // Layout horizontally for each child view in the ViewGroup
            child.layout(left, 0, left + width, height);
            left += width;
        }
        // Initialize left and right boundary values
        mLeftBorder = getChildAt(0).getLeft();
        mRightBorder = getChildAt(count - 1).getRight();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private void smoothScrollTo(int page) {
        int dstX = page >= 0 ? mWidth * (page + 1) : mWidth * (-page - 1);
        int offset = dstX - getScrollX();
        mScroller.startScroll(getScrollX(), 0, offset, 0, mDuration);
        invalidate();
    }

    private int getLeftLayout() {
        return R.layout.lib_pub_adapter_btb_left;
    }

    private int getRightLayout() {
        return R.layout.lib_pub_adapter_btb_right;
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

    private void reset() {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        scrollTo(0, 0);
        requestLayout();
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(new BottomThirdAdapter.DataSetObserver() {
            @Override
            public void notifyChanged() {
                reset();
            }
        });
        reset();
    }

    /**
     * Create or update list
     *
     * @param datas Datas
     * @param l     ClickListener
     */
    public void create(List<Item> datas, OnClickListener l) {
        if (mAdapter == null) {
            mAdapter = new BottomThirdBar.Adapter(mContext, datas, R.layout.lib_pub_adapter_btb);
            setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(datas);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setOnClickListener(l);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof Integer) {
            int page = (int) v.getTag();
            smoothScrollTo(page);
        }
    }

    public static class Adapter extends BottomThirdAdapter<Item> {
        private OnClickListener listener;

        public Adapter(Context context, List<Item> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        public void setOnClickListener(OnClickListener l) {
            this.listener = l;
        }

        @Override
        public void convert(final int position, CommonHolder holder, final Item item) {
            holder.setText(R.id.tv_btb_tips, item.content);
            holder.setImageResource(R.id.iv_btb_icon, item.res);
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(position, item);
                    }
                }
            });
        }
    }

    public static class Item {

        /**
         * Define type here
         */
        public final static int TYPE_SHARE = 0;
        public final static int TYPE_DOWNLOAD = 1;
        public final static int TYPE_MOVE = 2;
        public final static int TYPE_DELETE = 3;
        public final static int TYPE_RENAME = 4;
        public final static int TYPE_INFO = 5;
        public final static int TYPE_TRANS_TO_FAKIT = 6;

        @IntDef({TYPE_SHARE, TYPE_DOWNLOAD, TYPE_MOVE,
                TYPE_DELETE, TYPE_RENAME, TYPE_INFO,
                TYPE_TRANS_TO_FAKIT})
        @Target({ElementType.PARAMETER})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {

        }

        public int type;
        public int res;
        public String content;
        public float weight;

        public Item(@Type int type, @DrawableRes int res, String content) {
            this(type, res, content, 0.25f);
        }

        public Item(@Type int type, @DrawableRes int res, String content,
                    @FloatRange(from = 0f, to = 1f) float weight) {
            this.type = type;
            this.res = res;
            this.content = content;
            this.weight = weight;
        }
    }

    public interface OnClickListener {
        void onClick(int position, Item item);
    }
}
