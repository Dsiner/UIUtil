package com.d.lib.ui.view.vs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.ui.view.R;

import java.text.DecimalFormat;

/**
 * Use:
 * VSItem vsItemA = new VSItem("A", true); // true: 已选中, false: 未选中
 * VSItem vsItemB = new VSItem("B", false);
 * vsView.setCompareA(vsItemA)
 * vsView.setCompareB(vsItemB)
 * vsView.setPercent(0.85f, true); // param1: 若为50%请传-1; param2: false为只更值不刷新，true为更值并刷新view
 * <p>
 * Created by D on 2017/2/28.
 */
public class VSView extends View {
    /**
     * Math.pow(...) is very expensive, so avoid calling it and create it
     * yourself.
     */
    private static final int POW_10[] = {
            1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
    };
    private int mWidth; // View的宽度
    private int mHeight; // View的高度
    private float mPadding; // 对比条距两端间距
    private float mHPercent; // 对比条高度
    private float mRadius; // 对比条的圆角矩形弧度
    private Rect mRect;
    private RectF mRectF;
    private Paint mPaintA; // A类颜色的画笔
    private Paint mPaintB; // B类颜色的画笔
    private Paint mPaintTxt; // 仅用于绘制文字的画笔
    private Path mPath; // 通用路径
    private float mMargin; // 两圆与对比条的垂直间距
    private float mMarginTxt; // 文字与圆的水平间距
    private OnVSClickListener mListener; // Listener
    private float mPercent = 0.5f; // 对比A项所占百分比 范围0-1
    private int mDIndex = -1; // ActionDown按压时的位置
    private int mUIndex = 0; // ActionUp松开时的位置
    private int mTouchSlop; // 最小视为移动距离
    private float mDX, mDY; // ActionDown的坐标(dx,dy)
    private boolean mDInvaild; // 标志位,点击是否有效（true有效: 点中了圆, false无效: 未点中圆）
    private float mSpaceHalf; // 对比条，中间空隙的一半宽度
    private float mTanW; // 对比条，中间空隙的偏斜宽度
    private Bitmap mBitmapA; // 对比A项正常图片
    private Bitmap mBitmapAP; // 对比A项按压图片
    private Bitmap mBitmapAS; // 对比A项选中图片
    private Bitmap mBitmapAU; // 对比A项未选中图片
    private Bitmap mBitmapB; // 对比B项正常图片
    private Bitmap mBitmapBP; // 对比B项按压图片
    private Bitmap mBitmapBS; // 对比B项选中图片
    private Bitmap mBitmapBU; // 对比B项未选中图片
    private Rect mRectBp; // 仅用于图片Rect
    private VSItem mItemA; // 左项
    private VSItem mItemB; // 右项

    public VSView(Context context) {
        this(context, null);
    }

    public VSView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VSView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * format a number properly with the given number of digits
     *
     * @param number the number to format
     * @param digits the number of digits
     */
    public static String formatDecimal(double number, int digits) {
        number = roundNumber((float) number, digits);
        StringBuffer a = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                a.append(".");
            a.append("0");
        }
        DecimalFormat nf = new DecimalFormat("###,###,###,##0" + a.toString());
        String formatted = nf.format(number);
        return formatted;
    }

    public static float roundNumber(float number, int digits) {
        try {
            if (digits == 0) {
                int r0 = (int) Math.round(number);
                return r0;
            } else if (digits > 0) {
                if (digits > 9)
                    digits = 9;
                StringBuffer a = new StringBuffer();
                for (int i = 0; i < digits; i++) {
                    if (i == 0)
                        a.append(".");
                    a.append("0");
                }
                DecimalFormat nf = new DecimalFormat("#" + a.toString());
                String formatted = nf.format(number);
                return Float.valueOf(formatted);
            } else {
                digits = -digits;
                if (digits > 9)
                    digits = 9;
                int r2 = (int) (number / POW_10[digits] + 0.5);
                return r2 * POW_10[digits];
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return number;
        }
    }

    private void init(Context context) {
        int textSize = DimenUtils.dp2px(context, 13);
        mPadding = DimenUtils.dp2px(context, 2);
        mHPercent = DimenUtils.dp2px(context, 3);
        mTanW = DimenUtils.dp2px(context, 0.5f);
        mSpaceHalf = DimenUtils.dp2px(context, 1);
        mMargin = DimenUtils.dp2px(context, 4);
        mMarginTxt = DimenUtils.dp2px(context, 6);
        mRadius = DimenUtils.dp2px(context, 13.5f);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mBitmapA = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        mBitmapAP = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        mBitmapAS = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        mBitmapAU = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        mBitmapB = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        mBitmapBP = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        mBitmapBS = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        mBitmapBU = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);

        mRectBp = new Rect();
        mRect = new Rect();
        mRectF = new RectF();

        mPath = new Path();

        int colorTxt = Color.parseColor("#7c838a");

        mPaintTxt = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTxt.setTextSize(textSize);
        mPaintTxt.setTextAlign(Paint.Align.LEFT);
        mPaintTxt.setColor(colorTxt);
        mPaintTxt.setAlpha(0xbf);

        mPaintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintA.setColor(Color.parseColor("#E3542B"));
        mPaintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintB.setColor(Color.parseColor("#4ABC00"));

        mItemA = new VSItem("", false);
        mItemB = new VSItem("", false);
        calcPercent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHeight == 0 || mWidth == 0) {
            return;
        }
        if (mPercent == 0) {
            drawRoundRect(canvas, mRect, mRectF, mPaintB, mHPercent / 2,
                    mPadding, mHeight - mHPercent, mWidth - mPadding, mHeight);
        } else if (mPercent == 1) {
            drawRoundRect(canvas, mRect, mRectF, mPaintA, mHPercent / 2,
                    mPadding, mHeight - mHPercent, mWidth - mPadding, mHeight);
        } else {
            drawRoundRect(canvas, mRect, mRectF, mPaintA, mHPercent / 2,
                    mPadding, mHeight - mHPercent, mPadding + mHPercent, mHeight);
            drawRoundRect(canvas, mRect, mRectF, mPaintB, mHPercent / 2,
                    mWidth - mHPercent - mPadding, mHeight - mHPercent, mWidth - mPadding, mHeight);

            float offset = (mWidth - mPadding * 2 - mHPercent) * mPercent + mPadding + mHPercent / 2;
            if (offset < mPadding + mHPercent + mSpaceHalf + mTanW) {
                // 限定最小值
                offset = mPadding + mHPercent + mSpaceHalf + mTanW;
            } else if (offset > mWidth - mPadding - mHPercent - mSpaceHalf - mTanW) {
                // 限定最大值
                offset = mWidth - mPadding - mHPercent - mSpaceHalf - mTanW;
            }

            float left = mPadding + mHPercent / 2;
            float right = offset - mSpaceHalf;
            drawPath(canvas, mPath, mPaintA,
                    left, left, right + mTanW, right - +mTanW,
                    mHeight - mHPercent, mHeight, mHeight - mHPercent, mHeight);

            left = offset + mSpaceHalf;
            right = mWidth - mPadding - mHPercent / 2;
            drawPath(canvas, mPath, mPaintB,
                    left + mTanW, left - mTanW, right, right,
                    mHeight - mHPercent, mHeight, mHeight - mHPercent, mHeight);
        }

        float cy = mHeight - mHPercent - mMargin - mRadius;
        drawBitmap(canvas, cy);

        float textHeight = DimenUtils.getTextHeight(mPaintTxt);
        mPaintTxt.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(mItemA.mainText + " " + mItemA.percent, mRadius * 2 + mMarginTxt, cy + textHeight / 2, mPaintTxt);
        mPaintTxt.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(mItemB.mainText + " " + mItemB.percent, mWidth - mRadius * 2 - mMarginTxt, cy + textHeight / 2, mPaintTxt);
    }

    private void drawBitmap(Canvas canvas, float cy) {
        Bitmap bpA = mBitmapA, bpB = mBitmapB;
        if (!mItemA.isChecked && !mItemB.isChecked) {
            // A、B项都未选中
            if (mDInvaild) {
                if (mDIndex == 0) {
                    bpA = mBitmapAP;
                    bpB = mBitmapB;
                } else if (mDIndex == 1) {
                    bpA = mBitmapA;
                    bpB = mBitmapBP;
                }
            }
        } else {
            if (mItemA.isChecked) {
                bpA = mBitmapAS;
                bpB = mBitmapBU;
            } else {
                bpA = mBitmapAU;
                bpB = mBitmapBS;
            }
        }
        mRectBp.set(0, (int) (cy - mRadius), (int) (mRadius * 2), (int) (cy + mRadius));
        canvas.drawBitmap(bpA, null, mRectBp, null);
        mRectBp.set((int) (mWidth - mRadius * 2), (int) (cy - mRadius), mWidth, (int) (cy + mRadius));
        canvas.drawBitmap(bpB, null, mRectBp, null);
    }

    private void drawPath(Canvas canvas, Path path, Paint paint,
                          float ltX, float lbX, float rtX, float rbX,
                          float ltY, float lbY, float rtY, float rbY) {
        path.reset();
        path.moveTo(ltX, ltY);
        path.lineTo(lbX, lbY);
        path.lineTo(rbX, rbY);
        path.lineTo(rtX, rtY);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * Draw圆角矩形
     */
    private void drawRoundRect(Canvas canvas, Rect rect, RectF rectF, Paint paint, float rxy,
                               float left, float top, float right, float bottom) {
        rect.set((int) left, (int) top, (int) right, (int) bottom);
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rxy, rxy, paint);
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
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = event.getX();
                mDY = event.getY();
                float cline = mHeight - mHPercent - mMargin;
                if (eX >= 0 && eX <= mRadius * 2 && eY >= cline - mRadius * 2 && eY <= cline) {
                    mDIndex = 0;
                    mDInvaild = true; // 点击有效
                } else if (eX >= mWidth - mRadius * 2 && eX <= mWidth && eY >= cline - mRadius * 2 && eY <= cline) {
                    mDIndex = 1;
                    mDInvaild = true; // 点击有效
                } else {
                    mDIndex = -1;
                    mDInvaild = false; // 点击无效
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(eX - mDX) > mTouchSlop || Math.abs(eY - mDY) > mTouchSlop) {
                    mDInvaild = false; // 点击无效
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (mDInvaild) {
                    float clineF = mHeight - mHPercent - mMargin;
                    if (eX >= 0 && eX <= mRadius * 2 && eY >= clineF - mRadius * 2 && eY <= clineF) {
                        mUIndex = 0;
                    } else if (eX >= mWidth - mRadius * 2 && eX <= mWidth && eY >= clineF - mRadius * 2 && eY <= clineF) {
                        mUIndex = 1;
                    }
                    if (mUIndex == mDIndex) {
                        if (mListener != null) {
                            if (mUIndex == 0) {
                                mListener.onItemClick(mUIndex, mItemA);
                            } else if (mUIndex == 1) {
                                mListener.onItemClick(mUIndex, mItemB);
                            }
                        }
                    }
                }
                // Reset
                mDX = 0;
                mDY = 0;
                mDIndex = -1;
                mDInvaild = false;
                invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
                // Reset
                mDX = 0;
                mDY = 0;
                mDIndex = -1;
                mDInvaild = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void calcPercent() {
        if (mPercent == -1) {
            mItemA.percent = mItemB.percent = "";
            mPercent = 0.5f;
        } else {
            mItemA.percent = formatDecimal(mPercent * 100, 2) + "%";
            mItemB.percent = formatDecimal(100 - mPercent * 100, 2) + "%";
        }
    }

    public VSView setCompareA(VSItem item) {
        this.mItemA = item;
        return this;
    }

    public VSView setCompareB(VSItem item) {
        this.mItemB = item;
        return this;
    }

    /**
     * 动态刷新-设置对比项A所占百分比并重新绘制-格式为0.00
     */
    public void setPercent(float percentA, boolean isRefresh) {
        mPercent = percentA;
        calcPercent();
        if (isRefresh) {
            invalidate();
        }
    }

    public void setOnVSClickListener(OnVSClickListener l) {
        this.mListener = l;
    }

    public interface OnVSClickListener {
        void onItemClick(int index, VSItem item);
    }
}