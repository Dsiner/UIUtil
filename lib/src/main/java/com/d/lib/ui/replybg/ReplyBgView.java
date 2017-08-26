package com.d.lib.ui.replybg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.ui.UIUtil;


/**
 * Message Background
 * Created by D on 2017/3/3.
 */
public class ReplyBgView extends View {
    private int width;
    private int height;
    private float padding;//三角形高度
    private float rectRadius;
    private Rect rect;
    private RectF rectF;
    private Paint paint;
    private Path pathTrg;//三角形路径
    private int OffsetX;//三角形左偏移
    private int halfWTrg;//三角形底边长度/2

    public ReplyBgView(Context context) {
        this(context, null);
    }

    public ReplyBgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        padding = UIUtil.dip2px(context, 5);
        OffsetX = UIUtil.dip2px(context, 6.5f);
        halfWTrg = UIUtil.dip2px(context, 3);
        rectRadius = UIUtil.dip2px(context, 3);
        pathTrg = new Path();
        rect = new Rect();
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#FF4081"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pathTrg.moveTo(OffsetX, padding);
        pathTrg.lineTo(OffsetX + halfWTrg, 0);
        pathTrg.lineTo(OffsetX + halfWTrg * 2, padding);
        pathTrg.close();
        canvas.drawPath(pathTrg, paint);

        rect.set(0, (int) padding, width, height);
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint);//在原有矩形基础上，画成圆角矩形
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}