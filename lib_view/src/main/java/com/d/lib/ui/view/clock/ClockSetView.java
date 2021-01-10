package com.d.lib.ui.view.clock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.ui.view.R;

/**
 * ClockSetView
 * Created by D on 2018/8/7.
 */
public class ClockSetView extends View {
    public final static int MODE_HOUR = 0;
    public final static int MODE_MINUTE = 1;

    private final String[] HOURS_AM = new String[]{"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private final String[] HOURS_PM = new String[]{"00", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private final String[] MINUTE = new String[]{"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};

    private int mWidth;
    private int mHeight;

    private int mMode = MODE_HOUR;
    private boolean mShift;

    private Paint mPaint;
    private Xfermode[] mXfermodes;
    private int mColorBg, mColorMain, mColorSub, mColorFocusMain, mColorFocusSub, mColorIndicator;
    private float mTextMainSize, mTextSubSize;
    private float mRadiusBig, mRadiusSmall;
    private float mIndicatorWidth;
    private float mPadding;
    private float mTextHeight, mTextHeightMain, mTextHeightSub;
    private int mSweepAngle = 90;
    private OnSelectListener mListener;

    public ClockSetView(Context context) {
        this(context, null);
    }

    public ClockSetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockSetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_ClockSetView);
        mColorBg = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_background, Color.parseColor("#FAFAFA"));
        mColorMain = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_mainColor, Color.parseColor("#000000"));
        mColorSub = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_subColor, Color.parseColor("#707070"));
        mColorFocusMain = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_mainFoucusColor, Color.parseColor("#ffffff"));
        mColorFocusSub = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_subFoucusColor, Color.parseColor("#DFB5B8"));
        mColorIndicator = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_indicatorColor, Color.parseColor("#DC4339"));
        mTextMainSize = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_mainTextSize, DimenUtils.dp2px(context, 16f));
        mTextSubSize = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_subTextSize, DimenUtils.dp2px(context, 12f));
        mRadiusBig = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_radiusBig, DimenUtils.dp2px(context, 18f));
        mRadiusSmall = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_radiusSmall, DimenUtils.dp2px(context, 3));
        mIndicatorWidth = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_indicatorWidth, DimenUtils.dp2px(context, 2));
        mPadding = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_padding, DimenUtils.dp2px(context, 3));
        typedArray.recycle();
    }

    private void init(Context context) {
        mXfermodes = new Xfermode[]{new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)};
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColorMain);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSubSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mTextHeightSub = DimenUtils.getTextHeight(mPaint);
        mPaint.setTextSize(mTextMainSize);
        mTextHeightMain = DimenUtils.getTextHeight(mPaint);
        mTextHeight = mTextHeightMain;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColorBg);
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, Math.min(mWidth, mHeight) / 2f, mPaint);
        if (mMode == 0) {
            mPaint.setColor(mColorSub);
            mPaint.setTextSize(mTextSubSize);
            mTextHeight = mTextHeightSub;
            drawDial(canvas, Math.min(mWidth, mHeight) / 2f - mRadiusBig * 3f - mPadding, mShift, HOURS_PM);
            mPaint.setColor(mColorMain);
            mPaint.setTextSize(mTextMainSize);
            mTextHeight = mTextHeightMain;
            drawDial(canvas, Math.min(mWidth, mHeight) / 2f - mRadiusBig - mPadding, !mShift, HOURS_AM);
        } else {
            mPaint.setColor(mColorMain);
            mPaint.setTextSize(mTextMainSize);
            mTextHeight = mTextHeightMain;
            drawDial(canvas, Math.min(mWidth, mHeight) / 2f - mRadiusBig - mPadding, true, MINUTE);
        }
    }

    private void drawDial(Canvas canvas, float radius, boolean withC, String[] dials) {
        canvas.save();
        canvas.translate(mWidth / 2f, mHeight / 2f);
        mPaint.setXfermode(null);
        for (int i = 0; i < dials.length; i++) {
            final float x = (float) (radius * Math.sin(Math.PI / 6 * i));
            final float y = (float) (-radius * Math.cos(Math.PI / 6 * i)) + mTextHeight / 2f;
            canvas.drawText(dials[i], x, y, mPaint);
        }

        if (withC) {
            // Draw indicator
            canvas.rotate(mSweepAngle);
            mPaint.setColor(mColorIndicator);
            final float left = -mIndicatorWidth / 2;
            final float top = 0;
            final float right = mIndicatorWidth / 2;
            final float bottom = -radius;
            canvas.drawRect(left, top, right, bottom, mPaint);
            // Draw small circle
            canvas.drawCircle(0, 0, mRadiusSmall, mPaint);
            canvas.restore();

            // Draw big circle
            final int sc = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
            canvas.translate(mWidth / 2f, mHeight / 2f);
            canvas.rotate(mSweepAngle);
            canvas.drawCircle(0, -radius, mRadiusBig, mPaint);

            canvas.rotate(-mSweepAngle);
            // Draw shadow
            mPaint.setXfermode(mXfermodes[1]);
            mPaint.setColor(mMode == 0 && mShift ? mColorFocusSub : mColorFocusMain);
            for (int i = 0; i < dials.length; i++) {
                final float x = (float) (radius * Math.sin(Math.PI / 6 * i));
                final float y = (float) (-radius * Math.cos(Math.PI / 6 * i)) + mTextHeight / 2f;
                canvas.drawText(dials[i], x, y, mPaint);
            }

            if (mSweepAngle % 30 != 0) {
                // Draw big center circle
                canvas.rotate(mSweepAngle);
                canvas.drawCircle(0, -radius, mRadiusSmall * 0.618f, mPaint);
            }
            canvas.restoreToCount(sc);
        } else {
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float eX = event.getX();
        final float eY = event.getY();
        final float cX = eX - mWidth / 2f;
        final float cY = -eY + mHeight / 2f;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final boolean oldShift = mShift;
                final int oldSweepAngle = mSweepAngle;
                mSweepAngle = (int) Math.toDegrees(Math.atan(cX / cY));
                if (cX == 0 && cY > 0) {
                    mSweepAngle = 0;
                } else if (cX == 0 && cY < 0) {
                    mSweepAngle = 180;
                } else if (cX > 0 && cY == 0) {
                    mSweepAngle = 90;
                } else if (cX < 0 && cY == 0) {
                    mSweepAngle = 270;
                } else if (cX > 0 && cY < 0) {
                    mSweepAngle += 180;
                } else if (cX < 0 && cY < 0) {
                    mSweepAngle += 180;
                } else if (cX < 0 && cY > 0) {
                    mSweepAngle += 360;
                }
                mSweepAngle = Math.max(0, mSweepAngle);
                mSweepAngle = Math.min(360, mSweepAngle);
                final int piece = mMode == MODE_MINUTE ? 6 : 30;
                mSweepAngle = (mSweepAngle + piece / 2) / piece * piece;
                if (mSweepAngle >= 360) {
                    mSweepAngle = 0;
                }

                float r = (float) Math.sqrt(cX * cX + cY * cY);
                mShift = r <= Math.min(mWidth, mHeight) / 2f - mRadiusBig * 2f;
                if (mShift != oldShift || mSweepAngle != oldSweepAngle) {
                    if (mListener != null) {
                        int index = ExChange.angle2Index(mMode, mShift, mSweepAngle);
                        mListener.onSelect(mMode, ExChange.index2Value(mMode, index));
                    }
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setMode(int mode, @IntRange(from = 0, to = 59) int index) {
        this.mMode = mode;
        if (mode == MODE_HOUR) {
            index = Math.max(0, index);
            index = Math.min(23, index);
            this.mShift = index == 0 || index > 12;
        }
        this.mSweepAngle = ExChange.index2Angle(mode, index);
        this.invalidate();
    }

    public void setOnSelectListener(OnSelectListener l) {
        this.mListener = l;
    }

    public interface OnSelectListener {
        void onSelect(int mode, int value);
    }

    static class ExChange {

        static int index2Angle(int mode, int index) {
            final int piece = mode == MODE_MINUTE ? 6 : 30;
            return index * piece % 360;
        }

        static int angle2Index(int mode, boolean shift, int angle) {
            final int piece = mode == MODE_MINUTE ? 6 : 30;
            int index = angle / piece;
            if (mode == MODE_HOUR) {
                if (shift) {
                    if (index == 0) {
                        index = 0;
                    } else {
                        index += 12;
                    }
                } else {
                    if (index == 0) {
                        index = 12;
                    }
                }
            }
            return index;
        }

        static int value2Index(int mode, int value) {
            if (mode == MODE_HOUR) {
                return value;
            } else if (mode == MODE_MINUTE) {
                return value;
            }
            return value;
        }

        static int index2Value(int mode, int index) {
            return index;
        }
    }
}
