package com.d.lib.common.view.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.common.R;
import com.d.lib.common.utils.Util;

/**
 * TabTextView
 * Created by D on 2017/8/25.
 */
public class TabTextView extends View implements TabView {
    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private String mText = "title";
    private float mTextHeight;

    /**
     * Define
     */
    private int mTextSize; // Title文字大小
    private int mTextColor; // Title文字颜色
    private int mTextColorFocus; // Title文字颜色
    private int mPadding; // Title文字左右预留间距

    public TabTextView(Context context) {
        this(context, null);
    }

    public TabTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTextSize = Util.dip2px(context, 15);
        mTextColor = ContextCompat.getColor(context, R.color.lib_pub_color_gray);
        mTextColorFocus = ContextCompat.getColor(context, R.color.lib_pub_color_main);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        mTextHeight = Util.getTextHeight(mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = mWidth / 2f;
        float y = mHeight / 2f + mTextHeight / 2f;
        canvas.drawText(mText, x, y, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            mWidth = Util.getTextWidth(mText, mPaint) + mPadding * 2;
        }
        mHeight = getDefaultSize(getSuggestedMinimumWidth(), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public void setText(String text) {
        this.mText = text;
        requestLayout();
    }

    @Override
    public void setPadding(int padding) {
        this.mPadding = padding;
        requestLayout();
    }

    @Override
    public void setNumber(String text, int visibility) {

    }

    @Override
    public void notifyData(boolean focus) {
        this.mPaint.setColor(focus ? mTextColorFocus : mTextColor);
        invalidate();
    }

    @Override
    public void onScroll(float factor) {

    }
}
