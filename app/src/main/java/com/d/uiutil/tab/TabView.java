package com.d.uiutil.tab;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.d.uiutil.R;
import com.d.uiutil.Util;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;


/**
 * TAB
 * Created by D on 2017/3/8.
 */
public class TabView extends View {
    private int width;
    private int height;

    private Rect rect;
    private RectF rectF;
    private Paint paintA;
    private Paint paintB;
    private Paint paintTitle;//仅用于普通文字的画笔
    private Paint paintTitleCur;//仅用于当前选中文字的画笔

    private int count;//总数量
    private float withB;//单个标题宽度block
    private float withP;//两端预留间距side padding
    private int lastIndex;//上次的位置
    private int curIndex;//当前的位置
    private int dIndex = 0;//actionDown按压的位置
    private int uIndex = 0;//actionUp松开的位置

    private float factor;//进度因子:0-1
    private ValueAnimator animation;

    private int duration;//variables 动画时长
    private float padding;//variables 背景边框线宽度
    private int textSize;//variables 标题文字大小
    private float rectRadius;//variables 圆角矩形弧度
    private String[] TITLES;//variables 标题

    private OnTabSelectedListener listener;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //get args
        textSize = Util.dip2px(context, 14);
        padding = 2;
        TITLES = new String[]{"TAB0", "TAB1", "TAB2", "TAB3"};
        duration = 250;
        int colorStroke = getResources().getColor(R.color.colorBlue);
        int colorStrokeBlank = getResources().getColor(R.color.colorWhite);
        int colorText = getResources().getColor(R.color.colorBlue);
        int colorTextCur = getResources().getColor(R.color.colorWhite);

        count = TITLES.length;

        rect = new Rect();
        rectF = new RectF();
        paintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintA.setColor(colorStroke);

        paintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintB.setColor(colorStrokeBlank);

        paintTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTitle.setTextSize(textSize);
        paintTitle.setTextAlign(Paint.Align.CENTER);
        paintTitle.setColor(colorText);

        paintTitleCur = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTitleCur.setTextSize(textSize);
        paintTitleCur.setTextAlign(Paint.Align.CENTER);
        paintTitleCur.setColor(colorTextCur);

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
                    //选中回调
                    listener.onTabSelected(curIndex);
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
        rect.set(0, 0, width, height);
        rectF.set(rect);
        //step1-1:draw圆角矩形-边框线颜色
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintA);

        rect.set((int) padding, (int) padding, (int) (width - padding), (int) (height - padding));
        rectF.set(rect);
        //step1-2:draw圆角矩形-背景颜色
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintB);
        //step1-3:带边框的背景绘制完毕

        float start = withP + withB * lastIndex;//本次动画开始前的起始位置横坐标
        float end = withP + withB * curIndex;//本次动画结束时的预计位置横坐标
        float offsetX = start + (end - start) * factor;//通过属性动画因子，计算此瞬间滑块的其实横坐标

        //起始坐标offsetX=圆角矩形的left横坐标+预留间距withP
        //left   offsetX - withP
        //top    0
        //right  offsetX + withB + withP
        //bottom height
        rect.set((int) (offsetX - withP), 0, (int) (offsetX + withB + withP), height);
        rectF.set(rect);

        //step2:draw当前圆角矩形滑块
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintA);

        int textheight = (int) Util.getTextHeight(paintTitle);//获取标题的高度px
        int starty = (height + textheight) / 2;//标题的绘制y坐标,即标题底部中心点y坐标

        //step3:遍历绘制所有标题
        for (int i = 0; i < TITLES.length; i++) {
            float startx = withP + withB * i + withB / 2;//标题的绘制x坐标，即标题底部中心点x坐标
            float cursor = (offsetX + withB / 2) - withP;
            if (cursor < 0) {
                cursor = 0;
            }
            int offsetCur = (int) (cursor / withB);
            if (offsetCur == i && (offsetCur == curIndex || offsetCur == lastIndex)) {
                //当前滑块位置位于动画起始index或终止index时，文字高亮
                canvas.drawText(TITLES[i], startx, starty, paintTitleCur);
            } else {
                canvas.drawText(TITLES[i], startx, starty, paintTitle);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        rectRadius = (height + 0.5f) / 2;
        withP = (int) (rectRadius * 0.85f);
        withB = (width - withP * 2) / count;
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (count <= 0) {
            return false;
        }
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dIndex = (int) ((eX - withP) / withB);
                dIndex = Math.max(dIndex, 0);
                dIndex = Math.min(dIndex, count - 1);
                return (factor == 0 || factor == 1) && dIndex != curIndex;
            case MotionEvent.ACTION_MOVE:
                return factor == 0 || factor == 1;
            case MotionEvent.ACTION_UP:
                if (eX < 0 || eX > width || eY < 0 || eY > height) {
                    return true;
                }
                uIndex = (int) ((eX - withP) / withB);
                uIndex = Math.max(dIndex, 0);
                uIndex = Math.min(dIndex, count - 1);
                if (uIndex == dIndex) {
                    if (factor == 0 || factor == 1) {
                        lastIndex = curIndex;
                        curIndex = dIndex;
                        start();
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                break;
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
     * 切换当前tab
     *
     * @param index:  destination index
     * @param withAn: with animation
     */
    public void selectTab(int index, boolean withAn) {
        if (index == curIndex) {
            return;
        }
        lastIndex = curIndex;
        curIndex = index;
        if (withAn) {
            start();
        } else {
            factor = 1f;
            invalidate();
        }
    }

    public interface OnTabSelectedListener {
        /**
         * @param position: position
         */
        void onTabSelected(int position);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        this.listener = listener;
    }
}