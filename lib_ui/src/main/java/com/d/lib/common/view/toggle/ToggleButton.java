package com.d.lib.common.view.toggle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import com.d.lib.common.R;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * ToggleButton
 * Created by D on 2017/6/6.
 */
public class ToggleButton extends View implements ToggleView {
    private int width;
    private int height;

    private Rect rect;
    private RectF rectF;
    private Paint paintThumb;
    private Paint paintTrack;
    private Paint paintPadding;
    private Paint paintShader;

    private int touchSlop;
    private boolean isOpen; // 当前的位置
    private boolean isClickValid; // 点击是否有效

    private float padding; // Variables 背景边框线宽度
    private int duration; // Variables 动画时长

    private ValueAnimator animation;
    private float factor = 1; // 进度因子:0-1

    private OnToggleListener listener;
    private int colorThumb, colorTrackOpen, colorTrackOff, colorPadding;
    private float dX, dY;

    public ToggleButton(Context context) {
        this(context, null);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_ToggleButton);
        colorThumb = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorThumb, ContextCompat.getColor(context, R.color.lib_pub_color_white));
        colorTrackOpen = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorTrackOpen, ContextCompat.getColor(context, R.color.lib_pub_color_main));
        colorTrackOff = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorTrackOff, ContextCompat.getColor(context, R.color.lib_pub_color_white));
        colorPadding = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorPadding, ContextCompat.getColor(context, R.color.lib_pub_color_hint));
        padding = (int) typedArray.getDimension(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_padding, 1);
        duration = typedArray.getInteger(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_duration, 0);
        typedArray.recycle();
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null); // 禁用硬件加速
        }
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        rect = new Rect();
        rectF = new RectF();

        paintThumb = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintThumb.setColor(colorThumb);

        paintTrack = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTrack.setColor(colorTrackOff);

        paintPadding = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPadding.setColor(colorPadding);

        paintShader = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShader.setColor(colorThumb);
        paintShader.setShadowLayer(padding * 2, 0, 0, colorPadding);

        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                factor = (float) animation.getAnimatedValue(); // 更新进度因子
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float rectRadius = height / 2f;
        rect.set(0, 0, width, height);
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, isOpen ? paintTrack : paintPadding);

        rect.set((int) padding, (int) padding, (int) (width - padding), (int) (height - padding));
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintTrack);

        float c0 = height / 2;
        float c1 = width - height / 2;
        float start = !isOpen ? c1 : c0;
        float end = isOpen ? c1 : c0;
        float offsetX = start + (end - start) * factor; // 通过属性动画因子，计算此瞬间圆心的横坐标

        canvas.drawCircle(offsetX, height / 2, height / 2 - padding, paintShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!(factor == 0 || factor == 1)) {
            return false;
        }
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = eX;
                dY = eY;
                isClickValid = true;
            case MotionEvent.ACTION_MOVE:
                if (isClickValid && (Math.abs(eX - dX) > touchSlop || Math.abs(eY - dY) > touchSlop)) {
                    isClickValid = false;
                }
                return isClickValid;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!isClickValid || eX < 0 || eX > width || eY < 0 || eY > height) {
                    return false;
                }
                toggle();
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开始动画
     */
    public void start() {
        stop();
        if (animation != null) {
            animation.start();
        }
    }

    /**
     * 停止动画
     */
    public void stop() {
        if (animation != null) {
            animation.cancel();
        }
    }

    @Override
    public void toggle() {
        isOpen = !isOpen;
        if (isOpen) {
            paintTrack.setColor(colorTrackOpen);
        } else {
            paintTrack.setColor(colorTrackOff);
        }
        if (duration <= 0) {
            factor = 1f;
            invalidate();
        } else {
            start();
        }
        if (listener != null) {
            listener.onToggle(isOpen);
        }
    }

    @Override
    public void setOpen(boolean open) {
        if (factor != 0 && factor != 1) {
            return;
        }
        stop();
        isOpen = open;
        factor = 1f;
        if (isOpen) {
            paintTrack.setColor(colorTrackOpen);
        } else {
            paintTrack.setColor(colorTrackOff);
        }
        invalidate();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void setOnToggleListener(OnToggleListener l) {
        this.listener = l;
    }
}