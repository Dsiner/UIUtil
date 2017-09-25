package com.d.lib.ui.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.ui.R;
import com.d.lib.ui.UIUtil;

/**
 * TabTextView
 * Created by D on 2017/8/25.
 */
public class TabTextView extends View implements TabView {
    private int width;
    private int height;

    private Paint paint;
    private String text = "title";
    private float textHeight;
    private float textWidth;

    /**
     * define
     */
    private int textSize;//title文字大小
    private int textColor;//title文字颜色
    private int textColorFocus;//title文字颜色
    private int padding;//title文字左右预留间距

    public TabTextView(Context context) {
        this(context, null);
    }

    public TabTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        textSize = UIUtil.dip2px(context, 15);
        padding = UIUtil.dip2px(context, 12);
        textColor = ContextCompat.getColor(context, R.color.colorT);
        textColorFocus = ContextCompat.getColor(context, R.color.colorA);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setColor(textColor);

        textHeight = UIUtil.getTextHeight(paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = width / 2f;
        float y = height / 2f + textHeight / 2f;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = (int) (textWidth + padding * 2);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void setText(String text) {
        this.text = text;
        this.textWidth = UIUtil.getTextWidth(text, paint);
    }

    @Override
    public void notifyData(boolean focus) {
        this.paint.setColor(focus ? textColorFocus : textColor);
        invalidate();
    }

    @Override
    public void onScroll(float factor) {

    }
}
