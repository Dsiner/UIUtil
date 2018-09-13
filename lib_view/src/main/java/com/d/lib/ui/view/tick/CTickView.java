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
import android.view.animation.LinearInterpolator;

import com.d.lib.ui.view.R;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Tick
 * Created by D on 2017/2/28.
 */
public class CTickView extends View {
    private int width;
    private int height;

    private float factor; // Factor: 0-1
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

    public CTickView(Context context) {
        this(context, null);
    }

    public CTickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CTickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_CTickView);
        color = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_color, Color.parseColor("#ffffff"));
        colorCircle = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_colorCircle, Color.parseColor("#47b018"));
        strokeWidth = typedArray.getDimension(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_strokeWidth, 5);
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
        animation.setDuration(250);
        animation.setInterpolator(new LinearInterpolator());
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                factor = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        pathTick.moveTo(width * scaleAX, height * scaleAY);
        pathTick.lineTo(width * scaleBX, height * scaleBY);
        pathTick.lineTo(width * scaleCX, height * scaleCY);
        tickPathMeasure.setPath(pathTick, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        tickPathMeasure.getSegment(0, factor * tickPathMeasure.getLength(), path, true);
        path.rLineTo(0, 0);
        canvas.drawCircle(width / 2f, width / 2f, width / 2f, paintCircle);
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
}
