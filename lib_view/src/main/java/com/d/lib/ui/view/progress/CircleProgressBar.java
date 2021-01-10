package com.d.lib.ui.view.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.common.widget.roundedimageview.RoundedImageView;
import com.d.lib.ui.view.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CircleProgressBar
 * Created by D on 2018/3/30.
 */
public class CircleProgressBar extends FrameLayout implements View.OnClickListener {
    public final static int STATE_PROGRESS = 0;
    public final static int STATE_PENDING = 1;
    public final static int STATE_ERROR = 2;

    /**
     * [0]: Progress
     * [1]: Pendding
     * [2]: Error
     */
    private final int[] RES_IDS = new int[]{-1, -1, -1};
    private Point[] mPointsA, mPointsB, mPointsC;

    private int mWidth, mHeight;

    private Context mContext;
    private Paint mGPaint;
    private Path mGPath;

    private Paint mPaint;
    private Rect mRect;
    private RectF mRectF;
    private int mColorCircle, mColorArc;
    private int mDiameter;
    private int mSpace;
    private int[] mPaddingIcon;
    private float mStrokeWidth;

    private float mFactor;

    private int mState = STATE_PROGRESS;
    private boolean mIsFirst = true;
    private Request mRequest;
    private RoundedImageView mIvThumb, mIvAlpha, mIvState;
    private OnClickListener mListener;

    public CircleProgressBar(@NonNull Context context) {
        this(context, null);
    }

    public CircleProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, @SuppressWarnings("unused") AttributeSet attrs) {
        mColorCircle = Color.parseColor("#33008577");
        mColorArc = Color.parseColor("#008577");
        mSpace = DimenUtils.dp2px(context, 2.5f);
        mPaddingIcon = new int[]{DimenUtils.dp2px(context, 21.5f),
                DimenUtils.dp2px(context, 20f)};
    }

    private void init(Context context) {
        this.mContext = context;
        this.setWillNotDraw(false);
        mStrokeWidth = DimenUtils.dp2px(context, 3.5f);
        LayoutParams lp;
        mIvThumb = new RoundedImageView(context);
        mIvThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mIvThumb, lp);

        mIvAlpha = new RoundedImageView(context);
        mIvAlpha.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvAlpha.setImageResource(R.color.lib_pub_color_trans);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mIvAlpha, lp);

        mIvState = new RoundedImageView(context);
        mIvState.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mIvState, lp);
        setOnClickListener(this);

        mRequest = new Request();
        mGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGPaint.setColor(mColorCircle);
        mGPaint.setStyle(Paint.Style.FILL);
        mGPath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColorCircle);
        mPaint.setStrokeWidth(mSpace);
        mPaint.setStyle(Paint.Style.STROKE);

        mRect = new Rect();
        mRectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw(canvas, mState);
        mRect.set((int) ((mWidth - mDiameter) / 2f + mSpace / 2f),
                (int) ((mHeight - mDiameter) / 2f + mSpace / 2f),
                mWidth - (int) ((mWidth - mDiameter) / 2f + mSpace / 2f),
                mHeight - (int) ((mHeight - mDiameter) / 2f + mSpace / 2f));
        mRectF.set(mRect);
        mPaint.setColor(mColorCircle);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(mColorArc);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(mRectF, -90, mFactor, false, mPaint);
    }

    private void draw(@NonNull Canvas canvas, @State int state) {
        switch (state) {
            case STATE_PROGRESS:
                draw(canvas, mColorArc, getPoints(0));
                draw(canvas, mColorArc, getPoints(1));
                break;
            case STATE_PENDING:
                draw(canvas, mColorArc, getPoints(2));
                break;
            case STATE_ERROR:
                mGPaint.setColor(mColorArc);
                canvas.translate(mWidth / 2, mHeight / 2);
                canvas.rotate(45);

                float factorB = 0.51f;
                mRect.set((int) (-mWidth / 2f * factorB), (int) (-mStrokeWidth / 2f),
                        (int) (mWidth / 2f * factorB), (int) (mStrokeWidth / 2f));
                mRectF.set(mRect);
                canvas.drawRect(mRectF, mGPaint);

                canvas.rotate(90);
                canvas.drawRect(mRectF, mGPaint);
                canvas.rotate(-135);
                canvas.translate(-mWidth / 2, -mHeight / 2);
                break;
        }
    }

    @NonNull
    private Point[] getPoints(@IntRange(from = 0, to = 2) int type) {
        switch (type) {
            case 0:
                if (mPointsA == null) {
                    float factorA = 0.56f;
                    float startX = mWidth / 2f - mStrokeWidth * 1.63f;
                    float startY = mHeight / 2f * factorA;
                    mPointsA = new Point[]{new Point((int) (startX), (int) (startY)),
                            new Point((int) (startX), (int) (mHeight - startY)),
                            new Point((int) (startX + mStrokeWidth), (int) (mHeight - startY)),
                            new Point((int) (startX + mStrokeWidth), (int) (startY))};
                }
                return mPointsA;
            case 1:
                if (mPointsB == null) {
                    float factorA = 0.56f;
                    float startX = mWidth / 2f + mStrokeWidth * 0.63f;
                    float startY = mHeight / 2f * factorA;
                    mPointsB = new Point[]{new Point((int) (startX), (int) (startY)),
                            new Point((int) (startX), (int) (mHeight - startY)),
                            new Point((int) (startX + mStrokeWidth), (int) (mHeight - startY)),
                            new Point((int) (startX + mStrokeWidth), (int) (startY))};
                }
                return mPointsB;
            case 2:
            default:
                if (mPointsC == null) {
                    mPointsC = new Point[]{new Point((int) (mWidth * (0.33f + 0.05f)), (int) (mHeight * 0.25f)),
                            new Point((int) (mWidth * (0.66f + 0.05f)), (int) (mHeight * 0.5f)),
                            new Point((int) (mWidth * (0.33f + 0.05f)), (int) (mHeight * 0.75f))};
                }
                return mPointsC;
        }
    }

    private void draw(@NonNull Canvas canvas, @ColorInt int color, @NonNull Point... points) {
        mGPaint.setColor(color);
        mGPath.reset();
        boolean first = true;
        for (Point p : points) {
            if (first) {
                first = false;
                mGPath.moveTo(p.x, p.y);
                continue;
            }
            mGPath.lineTo(p.x, p.y);
        }
        mGPath.close();
        canvas.drawPath(mGPath, mGPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mDiameter = Math.min(mWidth, mHeight);
        mPaddingIcon[0] = (int) (mDiameter * 21.5f / 60f);
        mPaddingIcon[1] = (int) (mDiameter * 20f / 60f);
        mIvThumb.setPadding((int) ((mWidth - mDiameter) / 2f + mSpace), (int) ((mHeight - mDiameter) / 2f + mSpace),
                (int) ((mWidth - mDiameter) / 2f + mSpace), (int) ((mHeight - mDiameter) / 2f + mSpace));
        mIvAlpha.setPadding((int) ((mWidth - mDiameter) / 2f + mSpace), (int) ((mHeight - mDiameter) / 2f + mSpace),
                (int) ((mWidth - mDiameter) / 2f + mSpace), (int) ((mHeight - mDiameter) / 2f + mSpace));
        setMeasuredDimension(mWidth, mHeight);
        if (mIsFirst && mDiameter > 0) {
            mIsFirst = false;
            setState(mState);
        }
    }

    @Override
    public void onClick(View v) {
        switch (mState) {
            case STATE_PROGRESS:
                if (mListener != null) {
                    mListener.onPause();
                }
                break;
            case STATE_PENDING:
                if (mListener != null) {
                    mListener.onResume();
                }
                break;
            case STATE_ERROR:
                if (mListener != null) {
                    mListener.onRestart();
                }
                break;
        }
    }

    public Request setState(int state) {
        if (state < STATE_PROGRESS || state > STATE_ERROR) {
            return mRequest;
        }
        this.mState = state;
        switch (state) {
            case STATE_PROGRESS:
                mIvThumb.setVisibility(VISIBLE);
                mIvAlpha.setVisibility(GONE);
                mIvState.setVisibility(GONE);
                mIvState.setPadding((int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f),
                        (int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f));
                if (RES_IDS[STATE_PROGRESS] != -1) {
                    mIvState.setImageResource(RES_IDS[STATE_PROGRESS]);
                } else {
                    invalidate();
                }
                break;
            case STATE_PENDING:
                mIvThumb.setVisibility(VISIBLE);
                mIvAlpha.setVisibility(VISIBLE);
                mIvState.setVisibility(VISIBLE);
                mIvState.setPadding((int) ((mWidth - mDiameter) / 2f + mPaddingIcon[0]),
                        (int) ((mHeight - mDiameter) / 2f + mPaddingIcon[1]),
                        (int) ((mWidth - mDiameter) / 2f + mPaddingIcon[0]),
                        (int) ((mHeight - mDiameter) / 2f + mPaddingIcon[1]));
                if (RES_IDS[STATE_PENDING] != -1) {
                    mIvState.setImageResource(RES_IDS[STATE_PENDING]);
                } else {
                    invalidate();
                }
                break;
            case STATE_ERROR:
                mIvThumb.setVisibility(VISIBLE);
                mIvAlpha.setVisibility(GONE);
                mIvState.setVisibility(VISIBLE);
                mIvState.setPadding((int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f),
                        (int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f));
                if (RES_IDS[STATE_ERROR] != -1) {
                    mIvState.setImageResource(RES_IDS[STATE_ERROR]);
                } else {
                    invalidate();
                }
                break;
        }
        return mRequest;
    }

    public void image(ImageView view, @DrawableRes int id) {
        view.setImageResource(id);
    }

    public void setOnClickListener(OnClickListener l) {
        this.mListener = l;
    }

    @IntDef({STATE_PROGRESS, STATE_PENDING, STATE_ERROR})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    public interface OnClickListener {
        void onRestart();

        void onResume();

        void onPause();
    }

    public class Request {
        public Request thumb(Bitmap bitmap) {
            mIvThumb.setImageBitmap(bitmap);
            return this;
        }

        public Request thumb(Drawable drawable) {
            mIvThumb.setImageDrawable(drawable);
            return this;
        }

        public Request progress(@FloatRange(from = 0f, to = 1f) float progress) {
            mFactor = 360 * progress;
            invalidate();
            return this;
        }
    }
}
