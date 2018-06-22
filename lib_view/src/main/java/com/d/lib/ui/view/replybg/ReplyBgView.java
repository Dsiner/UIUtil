package com.d.lib.ui.view.replybg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.ui.common.UIUtil;
import com.d.lib.ui.view.R;

/**
 * Message Background
 * Created by D on 2017/3/3.
 */
public class ReplyBgView extends View {
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int TOP = 2;
    private final int BOTTOM = 3;

    private int width;
    private int height;

    private Rect rect;
    private RectF rectF;
    private Paint paint;
    private Path pathTrg;//三角形路径
    private int gravity;
    private int colorBg;
    private float rectRadius;
    private float offset;//三角形左偏移
    private float trgHalfWidth;//三角形底边长度/2
    private float trgHeight;//三角形高度

    public ReplyBgView(Context context) {
        this(context, null);
    }

    public ReplyBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_ReplyBgView);
        gravity = typedArray.getInteger(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_gravity, TOP);
        colorBg = typedArray.getColor(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_color, ContextCompat.getColor(context, R.color.lib_ui_common_color_accent));
        rectRadius = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_radius, UIUtil.dip2px(context, 3));
        offset = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_offset, UIUtil.dip2px(context, 6.5f));
        trgHalfWidth = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_trgWidth, UIUtil.dip2px(context, 6)) / 2;
        trgHeight = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_trgHeight, UIUtil.dip2px(context, 5));
        typedArray.recycle();
    }

    private void init(Context context) {
        pathTrg = new Path();
        rect = new Rect();
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorBg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (gravity) {
            case LEFT:
                pathTrg.moveTo(trgHeight, offset > 0 ? offset : height + offset - trgHalfWidth * 2);
                pathTrg.lineTo(0, offset > 0 ? offset + trgHalfWidth : height + offset - trgHalfWidth);
                pathTrg.lineTo(trgHeight, offset > 0 ? offset + trgHalfWidth * 2 : height + offset);
                pathTrg.close();
                canvas.drawPath(pathTrg, paint);

                rect.set((int) trgHeight, 0, width, height);
                rectF.set(rect);
                canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint);//在原有矩形基础上，画成圆角矩形
                break;

            case RIGHT:
                pathTrg.moveTo(width - trgHeight, offset > 0 ? offset : height + offset - trgHalfWidth * 2);
                pathTrg.lineTo(width, offset > 0 ? offset + trgHalfWidth : height + offset - trgHalfWidth);
                pathTrg.lineTo(width - trgHeight, offset > 0 ? offset + trgHalfWidth * 2 : height + offset);
                pathTrg.close();
                canvas.drawPath(pathTrg, paint);

                rect.set(0, 0, (int) (width - trgHeight), height);
                rectF.set(rect);
                canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint);//在原有矩形基础上，画成圆角矩形
                break;

            case TOP:
                pathTrg.moveTo(offset > 0 ? offset : width + offset - trgHalfWidth * 2, trgHeight);
                pathTrg.lineTo(offset > 0 ? offset + trgHalfWidth : width + offset - trgHalfWidth, 0);
                pathTrg.lineTo(offset > 0 ? offset + trgHalfWidth * 2 : width + offset, trgHeight);
                pathTrg.close();
                canvas.drawPath(pathTrg, paint);

                rect.set(0, (int) trgHeight, width, height);
                rectF.set(rect);
                canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint);//在原有矩形基础上，画成圆角矩形
                break;

            case BOTTOM:
                pathTrg.moveTo(offset > 0 ? offset : width + offset - trgHalfWidth * 2, height - trgHeight);
                pathTrg.lineTo(offset > 0 ? offset + trgHalfWidth : width + offset - trgHalfWidth, height);
                pathTrg.lineTo(offset > 0 ? offset + trgHalfWidth * 2 : width + offset, height - trgHeight);
                pathTrg.close();
                canvas.drawPath(pathTrg, paint);

                rect.set(0, 0, width, (int) (height - trgHeight));
                rectF.set(rect);
                canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint);//在原有矩形基础上，画成圆角矩形
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}