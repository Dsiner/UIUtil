package com.d.lib.common.view.btb;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.d.lib.common.R;
import com.d.lib.common.component.lv.CommonHolder;
import com.d.lib.common.utils.Util;

import java.util.List;

/**
 * BottomThirdBar
 * Created by D on 2018/7/11.
 */
public class BottomThirdBar extends ViewGroup implements View.OnClickListener {
    private int width;
    private int height;

    private final int sizeRow = 4;
    private final float loadFactor = 1.75f;
    private final int duration = 1000;

    private Context context;
    private Scroller scroller;
    private Adapter adapter;
    private SparseArray<View> itemViews;
    private int widthNext;
    private int leftBorder, rightBorder;

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
        this.context = context;
        this.scroller = new Scroller(context);
        this.itemViews = new SparseArray<View>();
        this.widthNext = Util.dip2px(context, 26);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        removeAllViews();
        int recyclers = itemViews.size();
        int size = adapter != null ? adapter.getCount() : 0;
        if (size <= 0) {
            itemViews.clear();
        } else if (size + (int) (sizeRow * loadFactor) < recyclers) {
            for (int i = size + (int) (sizeRow * loadFactor); i < recyclers; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    itemViews.removeAt(i);
                } else {
                    itemViews.delete(i);
                }
            }
        }

        if (adapter != null) {
            if (size <= sizeRow) {
                for (int i = 0; i < size; i++) {
                    View child = adapter.getView(i, itemViews.get(i), this);
                    itemViews.put(i, child);
                    addView(child);
                }
            } else {
                for (int i = 0; i < size; i++) {
                    if (i + 1 > sizeRow && (i + 1) % sizeRow == 1) {
                        int page = i / sizeRow - 1;
                        View right = LayoutInflater.from(context).inflate(getRightLayout(), null);
                        right.setTag(Integer.valueOf(page));
                        right.setOnClickListener(this);
                        addView(right);

                        View left = LayoutInflater.from(context).inflate(getLeftLayout(), null);
                        left.setTag(Integer.valueOf(-(page + 1)));
                        left.setOnClickListener(this);
                        addView(left);
                    }
                    View child = adapter.getView(i, itemViews.get(i), this);
                    itemViews.put(i, child);
                    addView(child);
                }
            }
        }

        int count = getChildCount();

        final boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;

        if (count <= 0 || measureMatchParentChildren) {
            // Not support mode
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(width, height);
            return;
        }

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        int itemWith;
        final int childHeight = Math.max(0, getMeasuredHeight());
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight,
                MeasureSpec.EXACTLY);
        if (count <= sizeRow) {
            itemWith = (int) (1f * width / count);
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                // Measure size for each child view in the ViewGroup
                final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWith,
                        MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        } else {
            itemWith = (int) (1f * (width - widthNext) / sizeRow);
            for (int i = 0, index = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getTag() != null && child.getTag() instanceof Integer) {
                    // Left or Right
                    final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthNext,
                            MeasureSpec.EXACTLY);
                    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                    continue;
                }
                if (index + 1 > sizeRow && (index + 1) % sizeRow == 1) {
                    if (index + sizeRow >= size) {
                        itemWith = (int) (1f * (width - widthNext) / sizeRow);
                    } else {
                        itemWith = (int) (1f * (width - widthNext * 2) / sizeRow);
                    }
                }
                // Measure size for each child view in the ViewGroup
                final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWith,
                        MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                index++;
            }
        }

        setMeasuredDimension(width, height);
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
        leftBorder = getChildAt(0).getLeft();
        rightBorder = getChildAt(count - 1).getRight();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    private void smoothScrollTo(int page) {
        int dstX = page >= 0 ? width * (page + 1) : width * (-page - 1);
        int offset = dstX - getScrollX();
        scroller.startScroll(getScrollX(), 0, offset, 0, duration);
        invalidate();
    }

    private int getLeftLayout() {
        return R.layout.lib_pub_adapter_btb_left;
    }

    private int getRightLayout() {
        return R.layout.lib_pub_adapter_btb_right;
    }

    private void reset() {
        if (!scroller.isFinished()) {
            scroller.forceFinished(true);
        }
        scrollTo(0, 0);
        requestLayout();
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        this.adapter.registerDataSetObserver(new BottomThirdAdapter.DataSetObserver() {
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
        if (adapter == null) {
            adapter = new Adapter(context, datas, R.layout.lib_pub_adapter_btb);
            setAdapter(adapter);
        } else {
            adapter.setDatas(datas);
            adapter.notifyDataSetChanged();
        }
        adapter.setOnClickListener(l);
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

        public int type;
        public int res;
        public String content;

        public Item(int type, @DrawableRes int res, String content) {
            this.type = type;
            this.res = res;
            this.content = content;
        }
    }

    public interface OnClickListener {
        void onClick(int position, Item item);
    }
}
