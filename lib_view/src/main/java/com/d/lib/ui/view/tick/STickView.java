package com.d.lib.ui.view.tick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.d.lib.ui.common.Util;
import com.d.lib.ui.view.R;
import com.nineoldandroids.animation.ValueAnimator;

public class STickView extends View {
    private final long mDuration = 2500; // Anim duration

    private int width;
    private int height;

    private float factor;
    private float scaleAX = 0.2659f;
    private float scaleAY = 0.4588f;
    private float scaleBX = 0.4541f;
    private float scaleBY = 0.6306f;
    private float scaleCX = 0.7553f;
    private float scaleCY = 0.3388f;

    private int color;
    private int colorCircle;
    private float strokeWidth;
    private Path path;
    private Path pathTick;
    private Paint paintTick;
    private Paint paintCircle;
    private PathMeasure tickPathMeasure;
    private ValueAnimator animation;

    public STickView(Context context) {
        this(context, null);
    }

    public STickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public STickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_CTickView);
        color = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_color, Color.parseColor("#ffffff"));
        colorCircle = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_colorCircle, Color.parseColor("#47b018"));
        strokeWidth = typedArray.getDimension(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_strokeWidth, Util.dip2px(context, 3.5f));
        typedArray.recycle();
        init();
    }

    public void init() {
        path = new Path();
        pathTick = new Path();

        tickPathMeasure = new PathMeasure();

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(colorCircle);

        paintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTick.setColor(color);
        paintTick.setStyle(Paint.Style.STROKE);
        paintTick.setStrokeWidth(strokeWidth);

        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(mDuration);
        animation.setInterpolator(new SpringScaleInterpolator(0.4f));
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (paintCircle != null && paintTick != null) {
                    float alpha = 1f * animation.getDuration() / mDuration;
                    alpha = Math.min(alpha, 1f);
                    alpha = Math.max(alpha, 0f);
                    paintCircle.setAlpha((int) (255 * alpha));
                    paintTick.setAlpha((int) (255 * alpha));
                }
                factor = (float) animation.getAnimatedValue();
                factor = factor / 1.27f;
                factor = Math.min(factor, 1f);
                factor = Math.max(factor, 0f);
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = this.width * 0.85f * factor;
        float height = this.height * 0.85f * factor;
        float startX = (this.width - width) / 2f;
        float startY = (this.height - height) / 2f;
        path.reset();
        pathTick.reset();
        pathTick.moveTo(startX + width * scaleAX, startY + height * scaleAY);
        pathTick.lineTo(startX + width * scaleBX, startY + height * scaleBY);
        pathTick.lineTo(startX + width * scaleCX, startY + height * scaleCY);
        tickPathMeasure.setPath(pathTick, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        tickPathMeasure.getSegment(0, tickPathMeasure.getLength(), path, true);
        width = this.width * factor;
        height = this.height * factor;
        startX = (this.width - width) / 2f;
        startY = (this.height - height) / 2f;
        canvas.drawCircle(startX + width / 2f, startY + width / 2f, width / 2f, paintCircle);
        paintTick.setStrokeWidth(strokeWidth * factor);
        canvas.drawPath(path, paintTick);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * Start animation
     */
    public void start() {
        stop();
        path = new Path();
        if (animation != null) {
            animation.start();
        }
    }

    /**
     * Stop animation
     */
    public void stop() {
        if (animation != null) {
            animation.end();
        }
    }

    class SpringScaleInterpolator implements Interpolator {
        private float factor;

        SpringScaleInterpolator(float factor) {
            this.factor = factor;
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }
}
