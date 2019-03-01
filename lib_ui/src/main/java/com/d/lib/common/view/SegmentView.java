package com.d.lib.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.common.R;
import com.d.lib.common.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SegmentView
 * Created by D on 2018/1/17.
 */
public class SegmentView extends View {
    private int mWidth;
    private int mHeight;

    private Rect mRect;
    private RectF mRectF;
    private Paint mPaintA;
    private Paint mPaintB;
    private int mColorA;
    private int mColorB;

    private List<String> mTitles = new ArrayList<>(); // Variables Titles
    private String mStrTitles;
    private float mTextSize;
    private float mRectRadius;
    private float mDivideWidth;
    private float mPadding; // Variables Background border line width
    private int mHeightText;
    private int mCurIndex = 0;
    private float mDX, mDY;
    private int mDIndex = 0;
    private int mTouchSlop;
    private boolean mIsClickValid;

    private OnSelectedListener mListener;

    public SegmentView(Context context) {
        this(context, null);
    }

    public SegmentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_SegmentView);
        mStrTitles = typedArray.getString(R.styleable.lib_pub_SegmentView_lib_pub_segmentv_titles);
        mColorA = typedArray.getColor(R.styleable.lib_pub_SegmentView_lib_pub_segmentv_colorMain, ContextCompat.getColor(context, R.color.lib_pub_color_main));
        mColorB = typedArray.getColor(R.styleable.lib_pub_SegmentView_lib_pub_segmentv_colorSub, ContextCompat.getColor(context, R.color.lib_pub_color_white));
        mTextSize = typedArray.getDimension(R.styleable.lib_pub_SegmentView_lib_pub_segmentv_textSize, Util.dip2px(context, 14));
        mRectRadius = typedArray.getDimension(R.styleable.lib_pub_SegmentView_lib_pub_segmentv_radius, -1);
        mDivideWidth = typedArray.getDimension(R.styleable.lib_pub_SegmentView_lib_pub_segmentv_divideWidth, Util.dip2px(context, 1));
        mPadding = typedArray.getDimension(R.styleable.lib_pub_SegmentView_lib_pub_segmentv_borderWidth, Util.dip2px(context, 1));
        typedArray.recycle();
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Disabling hardware acceleration
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mRect = new Rect();
        mRectF = new RectF();
        mPaintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintA.setColor(mColorA);
        mPaintA.setTextSize(mTextSize);
        mPaintA.setTextAlign(Paint.Align.CENTER);

        mPaintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintB.setColor(mColorB);
        mPaintB.setTextSize(mTextSize);
        mPaintB.setTextAlign(Paint.Align.CENTER);

        // Get title height px
        mHeightText = (int) Util.getTextHeight(mPaintB);

        if (!TextUtils.isEmpty(mStrTitles)) {
            String[] strs = mStrTitles.split(";");
            mTitles.addAll(Arrays.asList(strs));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTitles == null || mTitles.size() <= 0) {
            return;
        }
        int size = mTitles.size();
        float space = (1f * mWidth) / size / 2;

        // Background
        mRect.set(0, 0, mWidth, mHeight);
        mRectF.set(mRect);
        canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaintA);

        mRect.set((int) mPadding, (int) mPadding, (int) (mWidth - mPadding), (int) (mHeight - mPadding));
        mRectF.set(mRect);
        canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaintB);

        // Slider
        if (mCurIndex == 0) {
            canvas.drawRect(space, 0, space * 2, mHeight, mPaintA);
            mRect.set(0, 0, (int) (space * 2), mHeight);
            mRectF.set(mRect);
            canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaintA);
        } else if (mCurIndex == size - 1) {
            canvas.drawRect(space * (size * 2 - 2), 0, space * (size * 2 - 1), mHeight, mPaintA);
            mRect.set((int) (space * (size * 2 - 2)), 0, mWidth, mHeight);
            mRectF.set(mRect);
            canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaintA);
        } else {
            canvas.drawRect(space * 2 * mCurIndex, 0, space * 2 * (mCurIndex + 1), mHeight, mPaintA);
        }

        int starty = (mHeight + mHeightText) / 2;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                // Draw divide line
                canvas.drawRect(space * 2 * i - mDivideWidth / 2, 0,
                        space * 2 * i + mDivideWidth / 2, mHeight, mPaintA);
            }
            // Draw title
            canvas.drawText(mTitles.get(i), space * 2 * i + space, starty, mCurIndex == i ? mPaintB : mPaintA);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mRectRadius == -1) {
            mRectRadius = (mHeight + 0.5f) / 2;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = eX;
                mDY = eY;
                mDIndex = getIndex(eX, eY);
                mIsClickValid = true;
                return mDIndex != mCurIndex;
            case MotionEvent.ACTION_MOVE:
                if (mIsClickValid && (Math.abs(eX - mDX) > mTouchSlop || Math.abs(eY - mDY) > mTouchSlop)) {
                    mIsClickValid = false;
                }
                return mIsClickValid;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsClickValid || eX < 0 || eX > mWidth || eY < 0 || eY > mHeight) {
                    return false;
                }
                int uIndex = getIndex(eX, eY);
                if (uIndex == mDIndex) {
                    mCurIndex = mDIndex;
                    if (mListener != null) {
                        mListener.onSelected(mCurIndex);
                    }
                    invalidate();
                    return true;
                }
                return false;
        }
        return super.onTouchEvent(event);
    }

    private int getIndex(float eX, float eY) {
        if (eX < 0 || eX > mWidth || eY < 0 || eY > mHeight) {
            return 0;
        }
        int size = mTitles.size();
        int index = (int) (eX / (1f * mWidth / size));
        index = Math.min(index, size - 1);
        index = Math.max(index, 0);
        return index;
    }

    public void setTitles(List<String> ts) {
        if (ts == null) {
            return;
        }
        this.mTitles.clear();
        this.mTitles.addAll(ts);
        invalidate();
    }

    /**
     * Switch current Tab
     *
     * @param index Index
     */
    public void select(int index) {
        if (index < 0 || index > 1) {
            return;
        }
        mCurIndex = index;
        invalidate();
    }

    public interface OnSelectedListener {

        /**
         * @param index Index
         */
        void onSelected(int index);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.mListener = listener;
    }
}