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

import com.d.lib.common.utils.Util;
import com.d.lib.common.view.roundedimageview.RoundedImageView;
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
    private int[] resIds = new int[]{-1, -1, -1};
    private Point[] pointsA, pointsB, pointsC;

    private int width, height;

    private Context context;
    private Paint gPaint;
    private Path gPath;

    private Paint paint;
    private Rect rect;
    private RectF rectF;
    private int colorCircle, colorArc;
    private int diameter;
    private int space;
    private int[] paddingIcon;
    private float strokeWidth;

    private float factor;

    private int mState = STATE_PROGRESS;
    private boolean isFirst = true;
    private Request mRequest;
    private RoundedImageView ivThumb, ivAlpha, ivState;
    private OnClickListener listener;

    @IntDef({STATE_PROGRESS, STATE_PENDING, STATE_ERROR})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

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
        colorCircle = Color.parseColor("#33FF4081");
        colorArc = Color.parseColor("#FF4081");
        space = Util.dip2px(context, 2.5f);
        paddingIcon = new int[]{Util.dip2px(context, 21.5f),
                Util.dip2px(context, 20f)};
    }

    private void init(Context context) {
        this.context = context;
        this.setWillNotDraw(false);
        strokeWidth = Util.dip2px(context, 3.5f);
        LayoutParams lp;
        ivThumb = new RoundedImageView(context);
        ivThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(ivThumb, lp);

        ivAlpha = new RoundedImageView(context);
        ivAlpha.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivAlpha.setImageResource(R.color.lib_pub_color_trans);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(ivAlpha, lp);

        ivState = new RoundedImageView(context);
        ivState.setScaleType(ImageView.ScaleType.CENTER_CROP);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(ivState, lp);
        setOnClickListener(this);

        mRequest = new Request();
        gPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gPaint.setColor(colorCircle);
        gPaint.setStyle(Paint.Style.FILL);
        gPath = new Path();

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
        draw(canvas, mState);
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

    private void draw(@NonNull Canvas canvas, @State int state) {
        switch (state) {
            case STATE_PROGRESS:
                draw(canvas, colorArc, getPoints(0));
                draw(canvas, colorArc, getPoints(1));
                break;
            case STATE_PENDING:
                draw(canvas, colorArc, getPoints(2));
                break;
            case STATE_ERROR:
                gPaint.setColor(colorArc);
                canvas.translate(width / 2, height / 2);
                canvas.rotate(45);

                float factorB = 0.51f;
                rect.set((int) (-width / 2f * factorB), (int) (-strokeWidth / 2f),
                        (int) (width / 2f * factorB), (int) (strokeWidth / 2f));
                rectF.set(rect);
                canvas.drawRect(rectF, gPaint);

                canvas.rotate(90);
                canvas.drawRect(rectF, gPaint);
                canvas.rotate(-135);
                canvas.translate(-width / 2, -height / 2);
                break;
        }
    }

    @NonNull
    private Point[] getPoints(@IntRange(from = 0, to = 2) int type) {
        switch (type) {
            case 0:
                if (pointsA == null) {
                    float factorA = 0.56f;
                    float startX = width / 2f - strokeWidth * 1.63f;
                    float startY = height / 2f * factorA;
                    pointsA = new Point[]{new Point((int) (startX), (int) (startY)),
                            new Point((int) (startX), (int) (height - startY)),
                            new Point((int) (startX + strokeWidth), (int) (height - startY)),
                            new Point((int) (startX + strokeWidth), (int) (startY))};
                }
                return pointsA;
            case 1:
                if (pointsB == null) {
                    float factorA = 0.56f;
                    float startX = width / 2f + strokeWidth * 0.63f;
                    float startY = height / 2f * factorA;
                    pointsB = new Point[]{new Point((int) (startX), (int) (startY)),
                            new Point((int) (startX), (int) (height - startY)),
                            new Point((int) (startX + strokeWidth), (int) (height - startY)),
                            new Point((int) (startX + strokeWidth), (int) (startY))};
                }
                return pointsB;
            case 2:
            default:
                if (pointsC == null) {
                    pointsC = new Point[]{new Point((int) (width * (0.33f + 0.05f)), (int) (height * 0.25f)),
                            new Point((int) (width * (0.66f + 0.05f)), (int) (height * 0.5f)),
                            new Point((int) (width * (0.33f + 0.05f)), (int) (height * 0.75f))};
                }
                return pointsC;
        }
    }

    private void draw(@NonNull Canvas canvas, @ColorInt int color, @NonNull Point... points) {
        gPaint.setColor(color);
        gPath.reset();
        boolean first = true;
        for (Point p : points) {
            if (first) {
                first = false;
                gPath.moveTo(p.x, p.y);
                continue;
            }
            gPath.lineTo(p.x, p.y);
        }
        gPath.close();
        canvas.drawPath(gPath, gPaint);
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
        switch (mState) {
            case STATE_PROGRESS:
                if (listener != null) {
                    listener.onPause();
                }
                break;
            case STATE_PENDING:
                if (listener != null) {
                    listener.onResume();
                }
                break;
            case STATE_ERROR:
                if (listener != null) {
                    listener.onRestart();
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
                ivThumb.setVisibility(VISIBLE);
                ivAlpha.setVisibility(GONE);
                ivState.setVisibility(GONE);
                ivState.setPadding((int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f),
                        (int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f));
                if (resIds[STATE_PROGRESS] != -1) {
                    ivState.setImageResource(resIds[STATE_PROGRESS]);
                } else {
                    invalidate();
                }
                break;
            case STATE_PENDING:
                ivThumb.setVisibility(VISIBLE);
                ivAlpha.setVisibility(VISIBLE);
                ivState.setVisibility(VISIBLE);
                ivState.setPadding((int) ((width - diameter) / 2f + paddingIcon[0]),
                        (int) ((height - diameter) / 2f + paddingIcon[1]),
                        (int) ((width - diameter) / 2f + paddingIcon[0]),
                        (int) ((height - diameter) / 2f + paddingIcon[1]));
                if (resIds[STATE_PENDING] != -1) {
                    ivState.setImageResource(resIds[STATE_PENDING]);
                } else {
                    invalidate();
                }
                break;
            case STATE_ERROR:
                ivThumb.setVisibility(VISIBLE);
                ivAlpha.setVisibility(GONE);
                ivState.setVisibility(VISIBLE);
                ivState.setPadding((int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f),
                        (int) ((width - diameter) / 2f), (int) ((height - diameter) / 2f));
                if (resIds[STATE_ERROR] != -1) {
                    ivState.setImageResource(resIds[STATE_ERROR]);
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

        void onResume();

        void onPause();
    }

    public void setOnClickListener(OnClickListener l) {
        this.listener = l;
    }
}
