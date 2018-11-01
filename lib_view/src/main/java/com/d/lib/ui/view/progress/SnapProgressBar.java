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

import com.d.lib.common.utils.Util;
import com.d.lib.ui.view.R;
import com.d.lib.ui.view.roundedimageview.RoundedImageView;

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
    private final int[] resIds = new int[]{R.drawable.lib_ui_view_vs_icon,
            R.color.lib_pub_color_trans,
            R.drawable.lib_ui_view_vs_icon,
            R.drawable.lib_ui_view_vs_icon,
            R.drawable.lib_ui_view_vs_icon};

    private int width, height;

    private Context context;
    private Paint paint;

    private Rect rect;
    private RectF rectF;
    private int colorCircle, colorArc;
    private int diameter;
    private int space;
    private int[] paddingIcon;

    private float factor;

    private int mState = STATE_SCANNING;
    private boolean isFirst = true;
    private Request mRequest;
    private RoundedImageView ivThumb, ivAlpha, ivState;
    private OnClickListener listener;

    @IntDef({STATE_SCANNING, STATE_PROGRESS, STATE_PENDDING, STATE_DONE, STATE_ERROR})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

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
        colorCircle = Color.parseColor("#33FF4081");
        colorArc = Color.parseColor("#FF4081");
        space = Util.dip2px(context, 2.5f);
        paddingIcon = new int[]{Util.dip2px(context, 21.5f),
                Util.dip2px(context, 20f)};
    }

    private void init(Context context) {
        this.context = context;
        this.setWillNotDraw(false);
        LayoutParams lp;
        ivThumb = new RoundedImageView(context);
        ivThumb.setOval(true);
        ivThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(ivThumb, lp);

        ivAlpha = new RoundedImageView(context);
        ivAlpha.setOval(true);
        ivAlpha.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivAlpha.setImageResource(R.drawable.lib_ui_view_stab_circle_msg);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(ivAlpha, lp);

        ivState = new RoundedImageView(context);
        ivState.setOval(true);
        ivState.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(ivState, lp);
        ivState.setOnClickListener(this);

        mRequest = new Request();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorCircle);
        paint.setStrokeWidth(space);
        paint.setStyle(Paint.Style.STROKE);

        rect = new Rect();
        rectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState != STATE_PROGRESS && mState != STATE_PENDDING) {
            return;
        }
        rect.set((int) ((width - diameter) / 2f + space / 2f),
                (int) ((height - diameter) / 2f + space / 2f),
                width - (int) ((width - diameter) / 2f + space / 2f),
                height - (int) ((height - diameter) / 2f + space / 2f));
        rectF.set(rect);
        paint.setColor(colorCircle);
        paint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawArc(rectF, -90, 360, false, paint);
        paint.setColor(colorArc);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, -90, factor, false, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        diameter = Math.min(width, height);
        paddingIcon[0] = (int) (diameter * 21.5f / 60f);
        paddingIcon[1] = (int) (diameter * 20f / 60f);
        ivThumb.setPadding((int) ((width - diameter) / 2f + space), (int) ((height - diameter) / 2f + space),
                (int) ((width - diameter) / 2f + space), (int) ((height - diameter) / 2f + space));
        ivAlpha.setPadding((int) ((width - diameter) / 2f + space), (int) ((height - diameter) / 2f + space),
                (int) ((width - diameter) / 2f + space), (int) ((height - diameter) / 2f + space));
        setMeasuredDimension(width, height);
        if (isFirst && diameter > 0) {
            isFirst = false;
            setState(mState);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivState) {
            switch (mState) {
                case STATE_PENDDING:
                    if (listener != null) {
                        listener.onRestart();
                    }
                    break;
            }
        }
    }

    public Request setState(@State int state) {
        this.mState = state;
        switch (state) {
            case STATE_SCANNING:
                ivThumb.setVisibility(GONE);
                ivAlpha.setVisibility(GONE);
                ivState.setVisibility(VISIBLE);
                ivState.setPadding((int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f),
                        (int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f));
                ivState.setImageResource(resIds[STATE_SCANNING]);
                break;
            case STATE_PROGRESS:
                ivThumb.setVisibility(VISIBLE);
                ivAlpha.setVisibility(GONE);
                ivState.setVisibility(GONE);
                break;
            case STATE_PENDDING:
                ivThumb.setVisibility(VISIBLE);
                ivAlpha.setVisibility(VISIBLE);
                ivState.setVisibility(VISIBLE);
                ivState.setPadding((int) ((width - diameter) / 2f + paddingIcon[0]),
                        (int) ((height - diameter) / 2f + paddingIcon[1]),
                        (int) ((width - diameter) / 2f + paddingIcon[0]),
                        (int) ((height - diameter) / 2f + paddingIcon[1]));
                ivState.setImageResource(resIds[STATE_PENDDING]);
                break;
            case STATE_DONE:
                ivThumb.setVisibility(GONE);
                ivAlpha.setVisibility(GONE);
                ivState.setVisibility(VISIBLE);
                ivState.setPadding((int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f),
                        (int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f));
                ivState.setImageResource(resIds[STATE_DONE]);
                break;
            case STATE_ERROR:
                ivThumb.setVisibility(GONE);
                ivAlpha.setVisibility(GONE);
                ivState.setVisibility(VISIBLE);
                ivState.setPadding((int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f),
                        (int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f));
                ivState.setImageResource(resIds[STATE_ERROR]);
                break;
        }
        return mRequest;
    }

    public void image(ImageView view, @DrawableRes int id) {
        view.setImageResource(id);
    }

    public class Request {
        public Request thumb(Bitmap bitmap) {
            ivThumb.setImageBitmap(bitmap);
            return this;
        }

        public Request thumb(Drawable drawable) {
            ivThumb.setImageDrawable(drawable);
            return this;
        }

        public Request progress(@FloatRange(from = 0f, to = 1f) float progress) {
            factor = 360 * progress;
            invalidate();
            return this;
        }
    }

    public interface OnClickListener {
        void onRestart();
    }

    public void setOnClickListener(OnClickListener l) {
        this.listener = l;
    }
}
