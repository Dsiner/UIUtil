package com.d.lib.ui.tick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.d.lib.ui.R;
import com.nineoldandroids.animation.ValueAnimator;


/**
 * tick
 * Created by D on 2017/2/28.
 */

public class CTickView extends View {
    private int width;
    private int height;

    float factor;//进度因子:0-1
    float scaleAX = 0.2659f;
    float scaleAY = 0.4588f;
    float scaleBX = 0.4541f;
    float scaleBY = 0.6306f;
    float scaleCX = 0.7553f;
    float scaleCY = 0.3388f;

    private int color;
    private int colorCircle;
    private float strokeWidth;
    private Path path;
    private Path pathTick;
    private Paint paintTick;
    private Paint paintCircle;
    private PathMeasure tickPathMeasure;
    ValueAnimator animation;

    public CTickView(Context context) {
        this(context, null);
    }

    public CTickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CTickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CTickView);
        color = typedArray.getColor(R.styleable.CTickView_ctvColor, Color.parseColor("#ffffff"));
        colorCircle = typedArray.getColor(R.styleable.CTickView_ctvColorCircle, Color.parseColor("#47b018"));
        strokeWidth = typedArray.getDimension(R.styleable.CTickView_ctvStrokeWidth, 5);
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
                factor = (float) animation.getAnimatedValue();//更新进度因子
                postInvalidate();//刷新
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
     * 开始打勾动画
     */
    public void start() {
        stop();
        path = new Path();
        //属性动画-插值器刷新
        if (animation != null) {
            animation.start();
        }
    }

    public void stop() {
        if (animation != null) {
            animation.end();
        }
    }
}
