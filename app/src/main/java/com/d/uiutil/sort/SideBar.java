package com.d.uiutil.sort;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.d.uiutil.Util;

/**
 * SideBar
 * Created by D on 2017/6/6.
 */
public class SideBar extends View {
    private final String[] c = {"↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int width;
    private int height;
    private Rect rect;
    private RectF rectF;
    private Paint paint;
    private Paint paintCur;
    private Paint paintRect;
    private int colorTrans, colorWhite, colorBar, colorRect;
    private int count;
    private int onpice;
    private int widthBar;
    private int widthRect;
    private int rectRadius;
    private float textHeight;
    private float textLightHeight;
    private boolean dValid;
    private int index = -1;
    private OnLetterChangedListener listener;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        count = c.length;
        widthRect = Util.dip2px(context, 70);
        rectRadius = Util.dip2px(context, 6);
        colorTrans = Color.parseColor("#00000000");
        colorWhite = Color.parseColor("#ffffff");
        colorBar = Color.parseColor("#BBBBBB");
        colorRect = Color.parseColor("#7F7F7F");

        rect = new Rect();
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#565656"));

        paintCur = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCur.setTextAlign(Paint.Align.CENTER);
        paintCur.setColor(Color.parseColor("#FF4081"));

        paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRect.setTextAlign(Paint.Align.CENTER);
        paintRect.setTextSize(Util.dip2px(context, 32));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        resetRect(width - widthBar, 0, width, height, index == -1 ? colorTrans : colorBar);
        canvas.drawRect(rectF, paintRect);

        for (int i = 0; i < count; i++) {
            canvas.drawText(c[i], width - widthBar / 2, onpice * i + onpice / 2 + textHeight / 2, i == index ? paintCur : paint);
        }
        if (index >= 0 && index < count) {
            resetRect((width - widthRect) / 2, (height - widthRect) / 2, (width + widthRect) / 2, (height + widthRect) / 2, colorRect);
            canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintRect);
            paintRect.setColor(colorWhite);
            canvas.drawText(c[index], width / 2, (height + textLightHeight) / 2, paintRect);
        }
    }

    private void resetRect(int left, int top, int right, int bottom, int color) {
        rect.set(left, top, right, bottom);
        rectF.set(rect);
        paintRect.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        onpice = height / count;
        widthBar = (int) (onpice * 1.182f);
        int textSize = Util.dip2px(getContext(), onpice * 0.36f);
        paint.setTextSize(textSize);
        paintCur.setTextSize(textSize);
        textHeight = Util.getTextHeight(paint);
        textLightHeight = Util.getTextHeight(paintRect);
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dValid = eX > width - widthBar;
                return delegate(adjustIndex(eY));
            case MotionEvent.ACTION_MOVE:
                return delegate(adjustIndex(eY));
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return delegate(-1);
        }
        return super.onTouchEvent(event);
    }

    private int adjustIndex(float eY) {
        eY = Math.max(eY, 0);
        eY = Math.min(eY, height);
        int i = (int) (eY / onpice);
        i = Math.max(i, 0);
        i = Math.min(i, count - 1);
        return i;
    }

    private boolean delegate(int i) {
        if (dValid && i != index) {
            index = i;
            if (index != -1 && listener != null) {
                listener.onChange(index, c[index]);
            }
            invalidate();
            return true;
        }
        return false;
    }

    public interface OnLetterChangedListener {
        void onChange(int index, String c);
    }

    public void setOnLetterChangedListener(OnLetterChangedListener listener) {
        this.listener = listener;
    }
}
