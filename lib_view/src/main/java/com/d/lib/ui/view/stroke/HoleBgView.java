package com.d.lib.ui.view.stroke;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.common.util.DimenUtils;

/**
 * 镂空背景
 * Created by D on 2017/3/15.
 */
public class HoleBgView extends View {
    private int mWidth;
    private int mHeight;

    private Rect mRect;
    private RectF mRectF;
    private Paint mPaint;
    private int mOffsetX; // 偏移
    private int mOffsetY; // 偏移
    private int mWithrH;
    private int mWithrW;
    private float mStrokeWidth;

    public HoleBgView(Context context) {
        this(context, null);
    }

    public HoleBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoleBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // 禁用硬件加速
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mOffsetX = DimenUtils.dp2px(context, 136.5f);
        mOffsetY = DimenUtils.dp2px(context, 29.5f);
        mWithrH = DimenUtils.dp2px(context, 35000);
        mWithrW = DimenUtils.dp2px(context, 35014.3f);
        mStrokeWidth = mWithrH * 2 - DimenUtils.dp2px(context, 34.5f);
        mRect = new Rect();
        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setAlpha(0xcc);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int offsetYF = mHeight - mOffsetY;
        int left = mOffsetX - mWithrW;
        int top = offsetYF - mWithrH;
        int right = mOffsetX + mWithrW;
        int bottom = offsetYF + mWithrH;
        mRect.set(left, top, right, bottom);
        mRectF.set(mRect);
        canvas.drawRoundRect(mRectF, mWithrH, mWithrH, mPaint); // 在原有矩形基础上，画成圆角矩形
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
}