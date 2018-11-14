package com.d.lib.ui.view.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.common.utils.Util;
import com.d.lib.ui.view.R;

/**
 * SettingProgressView
 * Created by D on 2016/7/7.
 */
public class SettingProgressView extends View {
    private int width;
    private int height;

    private Paint paint;
    private Paint paintShader; // 用于绘制当前选中的渐变阴影
    private int itemCount;
    private int colorSelected;
    private int colorUnselected;
    private float radiusSmall;
    private float radiusBig;
    private float radiusSpace;
    private float lineHeight; // 中间连接线高度
    private float shadeRadius; // 渐变阴影的宽度
    private float itemWidth;
    private float firstItemRange;
    private float bigRadiusWidth;
    private int curColor; // 当前选中的颜色
    private int curPosition;
    private int clickPosition;
    private int touchSlop;
    private boolean canDrag;
    private boolean isClickValid; // 点击是否有效
    private float divX, divY; // 当前Move位置
    private float lastX;
    private float lastY;
    private OnProgressChangeListener listener;

    public SettingProgressView(Context context) {
        super(context, null);
    }

    public SettingProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_SettingProgressView);
        itemCount = typedArray.getInteger(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_count, 5);
        colorSelected = typedArray.getColor(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_colorSelect, Color.parseColor("#69B068"));
        colorUnselected = typedArray.getColor(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_colorUnselect, Color.GRAY);
        radiusSmall = typedArray.getDimension(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_radiusSmall, Util.dip2px(context, 5));
        radiusBig = typedArray.getDimension(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_radiusBig, Util.dip2px(context, 10));
        radiusSpace = typedArray.getDimension(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_radiusSpace, Util.dip2px(context, 2));
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // 禁用硬件加速
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        lineHeight = Util.dip2px(context, 2);
        shadeRadius = Util.dip2px(context, 5);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShader = new Paint(Paint.ANTI_ALIAS_FLAG);
        curColor = Color.WHITE;
        paintShader.setColor(curColor);
        paintShader.setShadowLayer(shadeRadius, 0, 0, colorUnselected);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(colorUnselected);
        itemWidth = (width - (radiusSpace + radiusBig) * 2) / (itemCount - 1);
        float startx = radiusSpace + radiusBig;
        float starty = height / 2 - lineHeight / 2;
        float endx;
        float endy = starty + lineHeight;
        if (canDrag) {
            endx = divX;
        } else {
            endx = radiusSpace + radiusBig + itemWidth * curPosition;
        }
        canvas.drawRect(startx, starty, endx, endy, paint);

        paint.setColor(colorSelected);
        canvas.drawRect(endx, starty, width - radiusSpace - radiusBig, endy, paint);

        starty += lineHeight / 2;
        for (int i = 0; i < itemCount; i++) {
            startx = radiusBig + radiusSpace + itemWidth * i;
            if (i == curPosition) {
                if (canDrag) {
                    if (divX > startx) {
                        drawCircle(canvas, colorUnselected, startx, starty);
                    } else if (divX < startx) {
                        drawCircle(canvas, colorSelected, startx, starty);
                    }
                    paint.setColor(colorSelected);
                    canvas.drawCircle(divX, starty, radiusBig + radiusSpace - shadeRadius, paint);
                } else {
                    canvas.drawCircle(startx, starty, radiusBig + radiusSpace - shadeRadius, paintShader);
                }
            } else if (i < curPosition) {
                drawCircle(canvas, colorUnselected, startx, starty);
            } else {
                drawCircle(canvas, colorSelected, startx, starty);
            }
        }
    }

    /**
     * 绘制圆
     */
    private void drawCircle(Canvas canvas, int color, float startx, float starty) {
        paint.setColor(color);
        canvas.drawCircle(startx, starty, radiusSmall + radiusSpace, paint);
        paint.setColor(color);
        canvas.drawCircle(startx, starty, radiusSmall, paint);
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
        firstItemRange = radiusBig + radiusSpace + itemWidth / 2;
        bigRadiusWidth = (radiusBig + radiusSpace) * 2;
        int position = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = divX = event.getX();
                lastY = divY = event.getY();
                clickPosition = -1;
                if (event.getX() < bigRadiusWidth) {
                    clickPosition = 0;
                } else if (event.getX() >= width - bigRadiusWidth) {
                    clickPosition = itemCount - 1;
                } else if (event.getX() >= firstItemRange) {
                    clickPosition = (int) ((event.getX() - firstItemRange) / itemWidth) + 1;
                    if (!(Math.abs(bigRadiusWidth / 2 + itemWidth * clickPosition - event.getX()) <= bigRadiusWidth / 2)) {
                        clickPosition = -1; // 未点中大圆范围内
                    }
                }
                if (clickPosition == curPosition) {
                    canDrag = true;
                    isClickValid = false; // 点击无效，交给ACTION_MOVE处理
                    if (divX < bigRadiusWidth / 2) {
                        divX = bigRadiusWidth / 2;
                    }
                    if (divX > width - bigRadiusWidth / 2) {
                        divX = width - bigRadiusWidth / 2;
                    }
                    invalidate();
                } else {
                    canDrag = false;
                    isClickValid = true; // 点击有效，交给ACTION_UP处理
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!canDrag) {
                    return true;
                }
                // Disable parent view interception events
                getParent().requestDisallowInterceptTouchEvent(true);
                divX = event.getX();
                divY = event.getY();
                if (divX < bigRadiusWidth / 2) {
                    divX = bigRadiusWidth / 2;
                }
                if (divX > width - bigRadiusWidth / 2) {
                    divX = width - bigRadiusWidth / 2;
                }
                isClickValid = false; // 只要移动，点击无效，交给ACTION_UP处理
                if (event.getX() >= width - firstItemRange) {
                    position = itemCount - 1;
                } else if (event.getX() >= firstItemRange) {
                    position = (int) ((event.getX() - firstItemRange) / itemWidth) + 1;
                }
                if (curPosition != position) {
                    curPosition = position;
                    if (listener != null) {
                        listener.onProgressChange(curPosition);
                    }
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                canDrag = false;
                if (!isClickValid) {
                    invalidate();
                    if (listener != null) {
                        listener.onClick(curPosition);
                    }
                    return true;
                } else {
                    if (Math.abs(event.getX() - lastX) >= touchSlop || Math.abs(event.getY() - lastY) >= touchSlop) {
                        return true;
                    }
                }
                if (event.getX() >= width - firstItemRange) {
                    position = itemCount - 1;
                } else if (event.getX() >= firstItemRange) {
                    position = (int) ((event.getX() - firstItemRange) / itemWidth) + 1;
                }
                if (curPosition != position) {
                    curPosition = position;
                }
                invalidate();
                if (listener != null) {
                    listener.onClick(curPosition);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置当前选中
     */
    public SettingProgressView setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        return this;
    }

    public interface OnProgressChangeListener {

        /**
         * @param position From 0 to item count - 1
         */
        void onProgressChange(int position);

        /**
         * @param position from 0 to item count - 1
         */
        void onClick(int position);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        this.listener = listener;
    }
}
