package com.d.lib.ui.view.segement;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.ui.common.UIUtil;
import com.d.lib.ui.view.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SegementView
 * Created by D on 2018/1/17.
 */
public class SegementView extends View {
    private int width;
    private int height;

    private Rect rect;
    private RectF rectF;
    private Paint paintA;
    private Paint paintB;
    private int colorA;
    private int colorB;

    private List<String> TITLES = new ArrayList<>();//variables Titles
    private String strTitles;
    private float textSize;
    private float rectRadius;
    private float divideWidth;
    private float padding;//variables Background border line width
    private int heightText;
    private int curIndex = 0;
    private float dX, dY;
    private int dIndex = 0;
    private int touchSlop;
    private boolean isClickValid;

    private OnSelectedListener listener;

    public SegementView(Context context) {
        this(context, null);
    }

    public SegementView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_SegementView);
        strTitles = typedArray.getString(R.styleable.lib_ui_view_SegementView_lib_ui_view_segementv_titles);
        colorA = typedArray.getColor(R.styleable.lib_ui_view_SegementView_lib_ui_view_segementv_colorMain, ContextCompat.getColor(context, R.color.lib_ui_common_color_accent));
        colorB = typedArray.getColor(R.styleable.lib_ui_view_SegementView_lib_ui_view_segementv_colorSub, Color.parseColor("#ffffff"));
        textSize = typedArray.getDimension(R.styleable.lib_ui_view_SegementView_lib_ui_view_segementv_textSize, UIUtil.dip2px(context, 14));
        rectRadius = typedArray.getDimension(R.styleable.lib_ui_view_SegementView_lib_ui_view_segementv_radius, -1);
        divideWidth = typedArray.getDimension(R.styleable.lib_ui_view_SegementView_lib_ui_view_segementv_divideWidth, UIUtil.dip2px(context, 1));
        padding = typedArray.getDimension(R.styleable.lib_ui_view_SegementView_lib_ui_view_segementv_borderWidth, UIUtil.dip2px(context, 1));
        typedArray.recycle();
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //Disabling hardware acceleration
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        rect = new Rect();
        rectF = new RectF();
        paintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintA.setColor(colorA);
        paintA.setTextSize(textSize);
        paintA.setTextAlign(Paint.Align.CENTER);

        paintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintB.setColor(colorB);
        paintB.setTextSize(textSize);
        paintB.setTextAlign(Paint.Align.CENTER);

        //Get title height px
        heightText = (int) UIUtil.getTextHeight(paintB);

        if (!TextUtils.isEmpty(strTitles)) {
            String[] strs = strTitles.split(";");
            TITLES.addAll(Arrays.asList(strs));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TITLES == null || TITLES.size() <= 0) {
            return;
        }
        int size = TITLES.size();
        float space = (1f * width) / size / 2;

        //Background
        rect.set(0, 0, width, height);
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintA);

        rect.set((int) padding, (int) padding, (int) (width - padding), (int) (height - padding));
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintB);

        //Slider
        if (curIndex == 0) {
            canvas.drawRect(space, 0, space * 2, height, paintA);
            rect.set(0, 0, (int) (space * 2), height);
            rectF.set(rect);
            canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintA);
        } else if (curIndex == size - 1) {
            canvas.drawRect(space * (size * 2 - 2), 0, space * (size * 2 - 1), height, paintA);
            rect.set((int) (space * (size * 2 - 2)), 0, width, height);
            rectF.set(rect);
            canvas.drawRoundRect(rectF, rectRadius, rectRadius, paintA);
        } else {
            canvas.drawRect(space * 2 * curIndex, 0, space * 2 * (curIndex + 1), height, paintA);
        }

        int starty = (height + heightText) / 2;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                //Draw divide line
                canvas.drawRect(space * 2 * i - divideWidth / 2, 0,
                        space * 2 * i + divideWidth / 2, height, paintA);
            }
            //Draw title
            canvas.drawText(TITLES.get(i), space * 2 * i + space, starty, curIndex == i ? paintB : paintA);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (rectRadius == -1) {
            rectRadius = (height + 0.5f) / 2;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = eX;
                dY = eY;
                dIndex = getIndex(eX, eY);
                isClickValid = true;
                return dIndex != curIndex;
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
                int uIndex = getIndex(eX, eY);
                if (uIndex == dIndex) {
                    curIndex = dIndex;
                    if (listener != null) {
                        listener.onSelected(curIndex);
                    }
                    invalidate();
                    return true;
                }
                return false;
        }
        return super.onTouchEvent(event);
    }

    private int getIndex(float eX, float eY) {
        if (eX < 0 || eX > width || eY < 0 || eY > height) {
            return 0;
        }
        int size = TITLES.size();
        int index = (int) (eX / (1f * width / size));
        index = Math.min(index, size - 1);
        index = Math.max(index, 0);
        return index;
    }

    public void setTitles(List<String> ts) {
        if (ts == null) {
            return;
        }
        this.TITLES.clear();
        this.TITLES.addAll(ts);
        invalidate();
    }

    /**
     * Switch current Tab
     *
     * @param index: destination index
     */
    public void select(int index) {
        if (index < 0 || index > 1) {
            return;
        }
        curIndex = index;
        invalidate();
    }

    public interface OnSelectedListener {
        /**
         * @param index: index
         */
        void onSelected(int index);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }
}