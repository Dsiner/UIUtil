package com.d.lib.ui.togglebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import com.d.lib.ui.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * ToggleButton
 * Created by D on 2017/6/6.
 */
public class ToggleButton extends View {
    private int width;
    private int height;

    private Rect rect;
    private RectF rectF;
    private Paint paintNormal;
    private Paint paintLight;
    private Paint paintPadding;
    private Paint paintShader;

    private int touchSlop;
    private boolean isOpen;//当前的位置
    private boolean isClickValid;//点击是否有效

    private float padding;//variables 背景边框线宽度
    private int duration;//variables 动画时长

    private ValueAnimator animation;
    private float factor = 1;//进度因子:0-1

    private OnToggleListener listener;
    private int colorNormal, colorLight, colorPadding;
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_ToggleButton);
        colorNormal = typedArray.getColor(R.styleable.lib_ui_ToggleButton_lib_ui_tbtn_colorNormal, Color.parseColor("#ffffff"));
        colorLight = typedArray.getColor(R.styleable.lib_ui_ToggleButton_lib_ui_tbtn_colorLight, Color.parseColor("#FF4081"));
        colorPadding = typedArray.getColor(R.styleable.lib_ui_ToggleButton_lib_ui_tbtn_colorPadding, Color.parseColor("#e3e4e5"));
        padding = (int) typedArray.getDimension(R.styleable.lib_ui_ToggleButton_lib_ui_tbtn_padding, 4);
        duration = typedArray.getInteger(R.styleable.lib_ui_ToggleButton_lib_ui_tbtn_duration, 0);
        typedArray.recycle();
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);//禁用硬件加速
        }
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        rect = new Rect();
        rectF = new RectF();

        paintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintNormal.setColor(colorNormal);

        paintLight = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLight.setColor(colorLight);

        paintPadding = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPadding.setColor(colorPadding);

        paintShader = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShader.setColor(colorNormal);
        paintShader.setShadowLayer(padding * 2, 0, 0, colorPadding);

        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                factor = (float) animation.getAnimatedValue();//更新进度因子
                invalidate();
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (factor == 1 && listener != null) {
                    listener.onToggle(isOpen);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean toggle = (factor == 1) == isOpen;
        float rectRadius = height / 2f;
        rect.set(0, 0, width, height);
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, toggle ? paintLight : paintPadding);
        rect.set((int) padding, (int) padding, (int) (width - padding), (int) (height - padding));
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, toggle ? paintLight : paintNormal);

        float c0 = height / 2;
        float c1 = width - height / 2;
        float start = !isOpen ? c1 : c0;
        float end = isOpen ? c1 : c0;
        float offsetX = start + (end - start) * factor;//通过属性动画因子，计算此瞬间圆心的横坐标

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
                isOpen = !isOpen;
                if (duration <= 0) {
                    invalidate();
                } else {
                    start();
                }
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

    /**
     * toggle
     *
     * @param withAn: with animation
     */
    public void toggle(boolean withAn) {
        isOpen = !isOpen;
        if (withAn) {
            start();
        } else {
            factor = 1f;
            invalidate();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public interface OnToggleListener {
        /**
         * @param isOpen: isOpen
         */
        void onToggle(boolean isOpen);
    }

    public void setOnToggleListener(OnToggleListener listener) {
        this.listener = listener;
    }
}