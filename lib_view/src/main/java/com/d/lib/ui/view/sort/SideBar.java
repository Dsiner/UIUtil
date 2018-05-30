package com.d.lib.ui.view.sort;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.d.lib.ui.common.UIUtil;
import com.d.lib.ui.view.R;

import java.util.List;

/**
 * SideBar
 * Created by D on 2017/6/6.
 */
public class SideBar extends View {
    private int TYPE_NORMAL = 0, TYPE_CENTER = 1;
    private String[] DEFAULT_LETTERS = {"↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private String[] c;
    private int maxCount;

    private int width;
    private int height;
    private Rect rect;
    private RectF rectF;
    private Paint paint;
    private Paint paintCur;
    private Paint paintRect;
    private int type;
    private int colorTrans, colorWhite, colorBar, colorRect;
    private int count;
    private float onpice;
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

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_SideBar);
        type = typedArray.getInt(R.styleable.lib_ui_view_SideBar_lib_ui_view_sidebar_type, TYPE_NORMAL);
        maxCount = typedArray.getInt(R.styleable.lib_ui_view_SideBar_lib_ui_view_sidebar_maxCount, 29);
        if (type != TYPE_CENTER) {
            String strLetters = typedArray.getString(R.styleable.lib_ui_view_SideBar_lib_ui_view_sidebar_letters);
            if (TextUtils.isEmpty(strLetters)) {
                c = DEFAULT_LETTERS;
            } else {
                c = strLetters.split(";");
            }
            maxCount = c.length;
        }
        typedArray.recycle();
    }

    private void init(Context context) {
        if (type == TYPE_CENTER) {
            c = new String[]{};
        }
        count = c.length;
        widthRect = UIUtil.dip2px(context, 70);
        rectRadius = UIUtil.dip2px(context, 6);
        colorTrans = Color.parseColor("#00000000");
        colorWhite = Color.parseColor("#ffffff");
        colorBar = Color.parseColor("#aaBBBBBB");
        colorRect = Color.parseColor("#aa7F7F7F");

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
        paintRect.setTextSize(UIUtil.dip2px(context, 32));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (type == TYPE_NORMAL) {
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
        } else {
            super.onDraw(canvas);
            float offsetY = height * (1 - 1f * count / maxCount) / 2;
            float endY = offsetY + onpice * count;

            for (int i = 0; i < count; i++) {
                canvas.drawText(c[i], width - widthBar / 2, offsetY + onpice * i + onpice / 2 + textHeight / 2, i == index ? paintCur : paint);
            }
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
        onpice = 1f * height / (type == TYPE_CENTER ? maxCount : count);
        widthBar = (int) (onpice * 1.182f);
        float textSize = onpice * 0.686f;
        paint.setTextSize(textSize);
        paintCur.setTextSize(textSize);
        textHeight = UIUtil.getTextHeight(paint);
        textLightHeight = UIUtil.getTextHeight(paintRect);
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float offsetY = type == TYPE_CENTER ? height * (1 - 1f * count / maxCount) / 2 : 0;
        float eX = event.getX();
        float eY = event.getY() - offsetY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dValid = eY >= 0 && eY <= onpice * count + 1 && eX > width - widthBar;
                return dValid && delegate(adjustIndex(eY));
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

    public void reset(List<String> datas) {
        if (type != TYPE_CENTER || datas == null) {
            return;
        }
        c = datas.toArray(new String[datas.size()]);
        count = c.length;
        invalidate();
    }

    public interface OnLetterChangedListener {
        void onChange(int index, String c);
    }

    public void setOnLetterChangedListener(OnLetterChangedListener listener) {
        this.listener = listener;
    }
}
