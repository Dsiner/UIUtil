package com.d.lib.ui.view.lrc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.d.lib.ui.common.Util;
import com.d.lib.ui.view.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * LrcView
 * https://github.com/android-lili/CustomLrcView-master
 * Edited by D on 2017/5/16.
 */
public class LrcView extends View implements ILrcView {
    private final String DEFAULT_TEXT = "畅音乐，享自由";
    /***移动一句歌词的持续时间***/
    private final int DURATION_AUTO_SCROLL = 1500;
    /***停止触摸 如果View需要滚动的持续时间***/
    private final int DURATION_RESET = 400;
    private final float SIZE_TEXT_DEFAULT;//variables 文字大小
    private final float SIZE_TEXT_CUR_DEFAULT;
    private final float SIZE_TIME_DEFAULT;
    private final float PADDING_DEFAULT;
    private final float PADDING_TIME_DEFAULT;

    private final float MIN_SCALE;//最小缩放
    private final float MAX_SCALE;//最大缩放

    private int width;
    private int height;
    private Scroller scroller;
    private List<LrcRow> lrcRows;
    private int touchSlop;
    private float dX, dY;//actionDown的坐标(dx,dy)
    private float lastY;//touchEvent最后一次坐标(lastX,lastY)
    private boolean dVaild;
    private boolean canDrag;

    private int colorText;
    private int colorTextCur;
    private int colorTime;
    private float sizeText;//variables 文字大小
    private float sizeTextCur;
    private float sizeTime;
    private float padding;
    private float paddingTime;
    private Paint paint;//仅用于普通文字的画笔
    private Paint paintCur;//仅用于当前高亮文字的画笔
    private Paint paintLine;//仅用于画线的画笔

    private int rowCount;
    private int lastRow = -1;
    private int curRow = -1;

    private int rowHeight;//one row height(text+padding)
    private int offsetY;

    private ValueAnimator animation;
    private float curTextOffsetX;
    private float scaleFactor;//进度因子
    private boolean withLine;

    private OnClickListener clickListener;
    private OnSeekChangeListener seekChangeListener;

    public LrcView(Context context) {
        this(context, null);
    }

    public LrcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_LrcView);
        colorText = typedArray.getColor(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textColor, Color.parseColor("#4577B7"));
        colorTextCur = typedArray.getColor(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textColorCur, Color.parseColor("#FF4081"));
        colorTime = typedArray.getColor(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_timeColor, Color.parseColor("#FF4081"));
        sizeText = SIZE_TEXT_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textSize, 15);
        sizeTextCur = SIZE_TEXT_CUR_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textSizeCur, 17);
        sizeTime = SIZE_TIME_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_timeSize, 8);
        padding = PADDING_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_padding, 17);
        MIN_SCALE = typedArray.getFloat(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_minScale, 0.7f);
        MAX_SCALE = typedArray.getFloat(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_maxScale, 1.7f);
        typedArray.recycle();
        paddingTime = PADDING_TIME_DEFAULT = Util.dip2px(context, 5);
        init(context);
    }

    @Override
    public void init(Context context) {
        lrcRows = new ArrayList<>();
        scroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(colorText);

        paintCur = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCur.setTextAlign(Paint.Align.LEFT);
        paintCur.setColor(colorTextCur);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setTextAlign(Paint.Align.LEFT);
        paintLine.setColor(colorTime);

        resetValues();

        initAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lrcRows == null || lrcRows.size() <= 0) {
            //画默认的显示文字
            drawLine(canvas, paint, height / 2 + Util.getTextHeight(paint) / 2, DEFAULT_TEXT);
            return;
        }

        float cy = height / 2 + offsetY;
        if (rowHeight != 0) {
            rowCount = height / rowHeight + 4;//初始化将要绘制的歌词行数,因为不需要将所有歌词画出来
        }
        int minRaw = curRow - rowCount / 2;
        int maxRaw = curRow + rowCount / 2;
        minRaw = Math.max(minRaw, 0); //处理上边界
        maxRaw = Math.min(maxRaw, lrcRows.size() - 1); //处理下边界
        //实现渐变的最大歌词行数
        int count = Math.max(maxRaw - curRow, curRow - minRaw);
        //两行歌词间字体颜色变化的透明度
        int alpha = 0;
        if (count != 0) {
            alpha = (0xFF - 0x11) / count;
        }
        //画出来的第一行歌词的y坐标
        float rowY = cy + minRaw * rowHeight;

        for (int i = minRaw; i <= maxRaw; i++) {
            String text = lrcRows.get(i).getContent();//获取到高亮歌词
            if (curRow == i) {
                //画高亮歌词
                //因为有缩放效果，所有需要动态设置歌词的字体大小
                float size = sizeTextCur + (sizeTextCur - sizeText) * scaleFactor;
                paintCur.setTextSize(size);
                float textWidth = paintCur.measureText(text);//用画笔测量歌词的宽度
                if (textWidth > width) {
                    //如果歌词宽度大于view的宽，则需要动态设置歌词的起始x坐标，以实现水平滚动
                    canvas.drawText(text, curTextOffsetX, rowY, paintCur);
                } else {
                    //如果歌词宽度小于view的宽，则让歌词居中显示
                    float rowX = (width - textWidth) / 2;
                    canvas.drawText(text, rowX, rowY, paintCur);
                }

                //画时间线和时间
                if (withLine) {
                    float lineY = height / 2 + getScrollY();
                    canvas.drawText(lrcRows.get(curRow).getTimeStr(), 0, lineY - 5, paintLine);

                    float stopX = textWidth < width - paddingTime ? (width - textWidth) / 2 - paddingTime : 0;
                    canvas.drawLine(0, lineY, stopX, lineY, paintLine);
                    float startX = textWidth < width - paddingTime ? width - (width - textWidth) / 2 + paddingTime : width;
                    canvas.drawLine(startX, lineY, width, lineY, paintLine);
                }
            } else {
                if (i == lastRow) {
                    //画高亮歌词的上一句
                    //因为有缩放效果，所有需要动态设置歌词的字体大小
                    float size = sizeTextCur - (sizeTextCur - sizeText) * scaleFactor;
                    paint.setTextSize(size);
                } else {
                    //画其他的歌词
                    paint.setTextSize(sizeText);
                }
                float textWidth = paint.measureText(text);
                float rowX = (getWidth() - textWidth) / 2;
                //如果计算出的cx为负数,将cx置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
                if (rowX < 0) {
                    rowX = 0;
                }
                //实现颜色渐变
                paint.setColor(0x1000000 * (255 - Math.abs(i - curRow) * alpha) + colorText);
                canvas.drawText(text, rowX, rowY, paint);
            }
            //计算出下一行歌词绘制的y坐标
            rowY += rowHeight;
        }
    }

    private void drawLine(Canvas canvas, Paint paint, float y, String text) {
        float textWidth = paint.measureText(text);
        float textX = (width - textWidth) / 2;
        if (textX < 0) {
            textX = 0;
        }
        canvas.drawText(text, textX, y, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void smoothScrollTo(int dstY, int duration) {
        int oldScrollY = getScrollY();
        int offset = dstY - oldScrollY;
        scroller.startScroll(getScrollX(), oldScrollY, getScrollX(), offset, duration);
        invalidate();
    }

    private void forceFinished() {
        if (scroller != null && !scroller.isFinished()) {
            scroller.forceFinished(true);
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            scaleFactor = scroller.timePassed() * 3f / DURATION_AUTO_SCROLL;
            if (scaleFactor > 1) {
                scaleFactor = 1;
            }
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (lrcRows == null || lrcRows.size() <= 0) {
            return false;
        }
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = event.getX();
                dY = lastY = event.getY();
                dVaild = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (dVaild && (Math.abs(eX - dX) > touchSlop || Math.abs(eY - dY) > touchSlop)) {
                    dVaild = false;//点击无效
                }
                if (!canDrag && Math.abs(eY - dY) > touchSlop && Math.abs(eY - dY) > Math.abs(eX - dX)) {
                    canDrag = true;
                    forceFinished();
                    stopHorizontalScrollLrc();
                    scaleFactor = 1;
                    withLine = true;
                }
                if (canDrag) {
                    float offset = eY - lastY;//偏移量
                    scrollBy(getScrollX(), -(int) offset);
                    int curRow = (getScrollY() + rowHeight / 2) / rowHeight;
                    curRow = Math.max(curRow, 0);
                    curRow = Math.min(curRow, lrcRows.size() - 1);
                    seekTo(lrcRows.get(curRow).getTime(), true);
                }
                lastY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (dVaild) {
                    if (clickListener != null) {
                        clickListener.onClick();
                    }
                } else {
                    if (canDrag) {
                        if (seekChangeListener != null) {
                            seekChangeListener.onProgressChanged(lrcRows.get(curRow).getTime());
                        }
                    }
                }
                if (getScrollY() < 0) {
                    smoothScrollTo(0, DURATION_RESET);
                } else {
                    int offset = (int) (lrcRows.size() * rowHeight - padding);
                    if (getScrollY() > offset) {
                        smoothScrollTo(offset, DURATION_RESET);
                    }
                }
                resetTouch();
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void resetTouch() {
        dX = dY = 0;
        dVaild = canDrag = false;
        withLine = false;
    }

    @Override
    public void setLrcRows(List<LrcRow> lrcRows) {
        if (this.lrcRows != null && lrcRows != null) {
            reset();
            this.lrcRows.addAll(lrcRows);
            scrollTo(getScrollX(), 0);
            invalidate();
        }
    }

    @Override
    public void seekTo(int progress, boolean fromUser) {
        if (lrcRows == null || lrcRows.size() == 0) {
            return;
        }
        //如果是由seekbar的进度改变触发 并且这时候处于拖动状态，则返回
        if (!fromUser && canDrag) {
            return;
        }
        int size = lrcRows.size();
        for (int i = size - 1; i >= 0; i--) {
            LrcRow lrcRow = lrcRows.get(i);
            if (progress >= lrcRow.getTime()) {
                if (curRow != i) {
                    lastRow = curRow;
                    curRow = i;
                    handleProgress(fromUser, lrcRow);
                }
                break;
            }
        }
    }

    private void handleProgress(boolean fromUser, LrcRow lrcRow) {
        if (canDrag) {
            invalidate();
            return;
        }
        int dstY = curRow * rowHeight;
        if (fromUser) {
            forceFinished();
            scrollTo(getScrollX(), dstY);
            invalidate();
        } else {
            smoothScrollTo(dstY, DURATION_AUTO_SCROLL);
            //如果高亮歌词的宽度大于View的宽，就需要开启属性动画，让它水平滚动
            float textWidth = paintCur.measureText(lrcRow.getContent());
            if (textWidth > width) {
                startHorizontalScrollLrc(width - textWidth, (long) (lrcRow.getTotalTime() * 0.6));
            }
        }
    }

    @Override
    public void setLrcScale(float factor) {
        if (factor < 0 || factor > 1) {
            return;
        }
        factor = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * factor;
        sizeText = SIZE_TEXT_DEFAULT * factor;
        sizeTextCur = SIZE_TEXT_CUR_DEFAULT * factor;
        sizeTime = SIZE_TIME_DEFAULT * factor;
        padding = PADDING_DEFAULT * factor;
        paddingTime = PADDING_TIME_DEFAULT * factor;
        resetValues();
        forceFinished();
        if (curRow != -1) {
            scrollTo(getScrollX(), curRow * rowHeight);
        }
        invalidate();
    }

    private void resetValues() {
        paint.setTextSize(sizeText);
        paintCur.setTextSize(sizeTextCur);
        paintLine.setTextSize(sizeTime);

        offsetY = (int) (Util.getTextHeight(paintCur) / 2);
        rowHeight = (int) (Util.getTextHeight(paintCur) + padding);
    }

    @Override
    public void reset() {
        forceFinished();
        if (lrcRows != null) {
            lrcRows.clear();
        }
    }

    private void initAnim() {
        animation = new ValueAnimator();
        animation.setInterpolator(new LinearInterpolator());
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curTextOffsetX = (float) animation.getAnimatedValue();//更新进度因子
                invalidate();
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void startHorizontalScrollLrc(float endX, long duration) {
        if (animation == null) {
            initAnim();
        } else {
            stopHorizontalScrollLrc();
        }
        animation.setFloatValues(0, endX);
        animation.setDuration(duration);
        animation.setStartDelay((long) (duration * 0.3)); //延迟执行属性动画
        animation.start();
    }

    private void stopHorizontalScrollLrc() {
        if (animation != null) {
            curTextOffsetX = 0;
            animation.cancel();
        }
    }

    public interface OnClickListener {
        void onClick();
    }

    public interface OnSeekChangeListener {
        void onProgressChanged(int progress);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnSeekChangeListener(OnSeekChangeListener seekChangeListener) {
        this.seekChangeListener = seekChangeListener;
    }
}
