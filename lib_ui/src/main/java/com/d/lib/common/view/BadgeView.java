package com.d.lib.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.d.lib.common.R;

@SuppressLint("AppCompatCustomView")
public class BadgeView extends TextView {
    private int width;
    private int height;

    private int color;
    private boolean circle;
    private float corner;
    private int max;
    private Rect rect;
    private RectF rectF;
    private Paint paint;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_BadgeView);
        color = typedArray.getColor(R.styleable.lib_pub_BadgeView_lib_pub_badgev_color, getResources().getColor(R.color.lib_pub_color_red));
        circle = typedArray.getBoolean(R.styleable.lib_pub_BadgeView_lib_pub_badgev_circle, true);
        corner = typedArray.getDimension(R.styleable.lib_pub_BadgeView_lib_pub_badgev_radius, -1);
        max = typedArray.getInteger(R.styleable.lib_pub_BadgeView_lib_pub_badgev_max, -1);
        typedArray.recycle();
    }

    private void init(Context context) {
        setWillNotDraw(false);
        rect = new Rect();
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!circle || getMeasuredWidth() > getMeasuredHeight() + 1) {
            rect.set(0, 0, width, height);
            rectF.set(rect);
            canvas.drawRoundRect(rectF, corner, corner, paint);
        } else {
            canvas.drawCircle(width / 2f, height / 2f, height / 2f, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (corner == -1) {
            corner = (height + 0.5f) / 2;
        }
    }

    public final void setTextAdj(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (max > 0) {
            try {
                int count = Integer.valueOf(text.toString());
                if (count > max) {
                    text = max + "+";
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        setText(text);
    }
}
