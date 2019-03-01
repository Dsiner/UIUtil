package com.d.lib.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.d.lib.common.R;

@SuppressLint("AppCompatCustomView")
public class BadgeView extends TextView {
    private int mWidth;
    private int mHeight;

    private int mColor;
    private boolean mCircle;
    private float mCorner;
    private int mMax;
    private Rect mRect;
    private RectF mRectF;
    private Paint mPaint;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_BadgeView);
        mColor = typedArray.getColor(R.styleable.lib_pub_BadgeView_lib_pub_badgev_color, getResources().getColor(R.color.lib_pub_color_red));
        mCircle = typedArray.getBoolean(R.styleable.lib_pub_BadgeView_lib_pub_badgev_circle, true);
        mCorner = typedArray.getDimension(R.styleable.lib_pub_BadgeView_lib_pub_badgev_radius, -1);
        mMax = typedArray.getInteger(R.styleable.lib_pub_BadgeView_lib_pub_badgev_max, -1);
        typedArray.recycle();
    }

    private void init(Context context) {
        setWillNotDraw(false);
        mRect = new Rect();
        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mCircle || getMeasuredWidth() > getMeasuredHeight() + 1) {
            mRect.set(0, 0, mWidth, mHeight);
            mRectF.set(mRect);
            canvas.drawRoundRect(mRectF, mCorner, mCorner, mPaint);
        } else {
            canvas.drawCircle(mWidth / 2f, mHeight / 2f, mHeight / 2f, mPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if (mCorner == -1) {
            mCorner = (mHeight + 0.5f) / 2;
        }
    }

    public final void setTextAdj(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mMax > 0) {
            try {
                int count = Integer.valueOf(text.toString());
                if (count > mMax) {
                    text = mMax + "+";
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        setText(text);
    }
}
