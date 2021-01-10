package com.d.lib.ui.view.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
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
 * SnapProgressBar
 * Created by D on 2018/3/30.
 */
public class SnapProgressBar extends FrameLayout implements View.OnClickListener {
    public final static int STATE_SCANNING = 0;
    public final static int STATE_PROGRESS = 1;
    public final static int STATE_PENDDING = 2;
    public final static int STATE_DONE = 3;
    public final static int STATE_ERROR = 4;

    /**
     * [0]: Scanning
     * [1]: Progress
     * [2]: Pendding
     * [3]: Done
     * [4]: Error
     */
    private final int[] RES_IDS = new int[]{R.drawable.lib_ui_view_vs_icon,
            R.color.lib_pub_color_trans,
            R.drawable.lib_ui_view_rtv_ic_back,
            R.drawable.lib_ui_view_vs_icon,
            R.drawable.lib_ui_view_vs_icon};

    private int mWidth, mHeight;

    private Context mContext;
    private Paint mPaint;

    private Rect mRect;
    private RectF mRectF;
    private int mColorCircle, mColorArc;
    private int mDiameter;
    private int mSpace;
    private int[] mPaddingIcon;

    private float mFactor;

    private int mState = STATE_SCANNING;
    private boolean mIsFirst = true;
    private Request mRequest;
    private RoundedImageView mIvThumb, mIvAlpha, mIvState;
    private OnClickListener mListener;

    public SnapProgressBar(@NonNull Context context) {
        this(context, null);
    }

    public SnapProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnapProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        LayoutParams lp;
        mIvThumb = new RoundedImageView(context);
        mIvThumb.setOval(true);
        mIvThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mIvThumb, lp);

        mIvAlpha = new RoundedImageView(context);
        mIvAlpha.setOval(true);
        mIvAlpha.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvAlpha.setImageResource(R.color.lib_pub_color_translucent);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mIvAlpha, lp);

        mIvState = new RoundedImageView(context);
        mIvState.setOval(true);
        mIvState.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mIvState, lp);
        mIvState.setOnClickListener(this);

        mRequest = new Request();
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
        if (mState != STATE_PROGRESS && mState != STATE_PENDDING) {
            return;
        }
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mDiameter = Math.min(mWidth, mHeight);
        mPaddingIcon[0] = (int) (mDiameter * 20f / 60f);
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
        if (v == mIvState) {
            switch (mState) {
                case STATE_PENDDING:
                    if (mListener != null) {
                        mListener.onRestart();
                    }
                    break;
            }
        }
    }

    public Request setState(@State int state) {
        this.mState = state;
        switch (state) {
            case STATE_SCANNING:
                mIvThumb.setVisibility(GONE);
                mIvAlpha.setVisibility(GONE);
                mIvState.setVisibility(VISIBLE);
                mIvState.setPadding((int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f),
                        (int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f));
                mIvState.setImageResource(RES_IDS[STATE_SCANNING]);
                break;
            case STATE_PROGRESS:
                mIvThumb.setVisibility(VISIBLE);
                mIvAlpha.setVisibility(GONE);
                mIvState.setVisibility(GONE);
                break;
            case STATE_PENDDING:
                mIvThumb.setVisibility(VISIBLE);
                mIvAlpha.setVisibility(VISIBLE);
                mIvState.setVisibility(VISIBLE);
                mIvState.setPadding((int) ((mWidth - mDiameter) / 2f + mPaddingIcon[0]),
                        (int) ((mHeight - mDiameter) / 2f + mPaddingIcon[1]),
                        (int) ((mWidth - mDiameter) / 2f + mPaddingIcon[0]),
                        (int) ((mHeight - mDiameter) / 2f + mPaddingIcon[1]));
                mIvState.setImageResource(RES_IDS[STATE_PENDDING]);
                break;
            case STATE_DONE:
                mIvThumb.setVisibility(GONE);
                mIvAlpha.setVisibility(GONE);
                mIvState.setVisibility(VISIBLE);
                mIvState.setPadding((int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f),
                        (int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f));
                mIvState.setImageResource(RES_IDS[STATE_DONE]);
                break;
            case STATE_ERROR:
                mIvThumb.setVisibility(GONE);
                mIvAlpha.setVisibility(GONE);
                mIvState.setVisibility(VISIBLE);
                mIvState.setPadding((int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f),
                        (int) ((mWidth - mDiameter) / 2f), (int) ((mHeight - mDiameter) / 2f));
                mIvState.setImageResource(RES_IDS[STATE_ERROR]);
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

    @IntDef({STATE_SCANNING, STATE_PROGRESS, STATE_PENDDING, STATE_DONE, STATE_ERROR})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    public interface OnClickListener {
        void onRestart();
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
