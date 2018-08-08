package com.d.lib.ui.view.clock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.d.lib.ui.common.UIUtil;
import com.d.lib.ui.view.R;

/**
 * ClockSetView
 * Created by D on 2018/8/7.
 */
public class ClockSetView extends View {
    public final static int MODE_HOUR = 0;
    public final static int MODE_MINUTE = 1;

    private final String[] hoursAM = new String[]{"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private final String[] hoursPM = new String[]{"00", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private final String[] minute = new String[]{"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};

    private int width;
    private int height;

    private int mode = MODE_HOUR;
    private boolean shift;

    private Paint paint;
    private Xfermode[] xfermodes;
    private int colorBg, colorMain, colorSub, colorFoucusMain, colorFoucusSub, colorIndicator;
    private float textMainSize, textSubSize;
    private float radiusBig, radiusSmall;
    private float indicatorWidth;
    private float padding;
    private float textHeight, textHeightMain, textHeightSub;
    private int sweepAngle = 90;
    private OnSelectListener listener;

    public ClockSetView(Context context) {
        this(context, null);
    }

    public ClockSetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockSetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_ClockSetView);
        colorBg = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_background, Color.parseColor("#FAFAFA"));
        colorMain = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_mainColor, Color.parseColor("#000000"));
        colorSub = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_subColor, Color.parseColor("#707070"));
        colorFoucusMain = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_mainFoucusColor, Color.parseColor("#ffffff"));
        colorFoucusSub = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_subFoucusColor, Color.parseColor("#DFB5B8"));
        colorIndicator = typedArray.getColor(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_indicatorColor, Color.parseColor("#DC4339"));
        textMainSize = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_mainTextSize, UIUtil.dip2px(context, 16f));
        textSubSize = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_subTextSize, UIUtil.dip2px(context, 12f));
        radiusBig = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_radiusBig, UIUtil.dip2px(context, 18f));
        radiusSmall = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_radiusSmall, UIUtil.dip2px(context, 3));
        indicatorWidth = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_indicatorWidth, UIUtil.dip2px(context, 2));
        padding = typedArray.getDimension(R.styleable.lib_ui_view_ClockSetView_lib_ui_view_clocksetv_padding, UIUtil.dip2px(context, 3));
        typedArray.recycle();
    }

    private void init(Context context) {
        xfermodes = new Xfermode[]{new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)};
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorMain);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSubSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        textHeightSub = UIUtil.getTextHeight(paint);
        paint.setTextSize(textMainSize);
        textHeightMain = UIUtil.getTextHeight(paint);
        textHeight = textHeightMain;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(colorBg);
        canvas.drawCircle(width / 2f, height / 2f, Math.min(width, height) / 2f, paint);
        if (mode == 0) {
            paint.setColor(colorSub);
            paint.setTextSize(textSubSize);
            textHeight = textHeightSub;
            drawDial(canvas, Math.min(width, height) / 2f - radiusBig * 3f - padding, shift, hoursPM);
            paint.setColor(colorMain);
            paint.setTextSize(textMainSize);
            textHeight = textHeightMain;
            drawDial(canvas, Math.min(width, height) / 2f - radiusBig - padding, !shift, hoursAM);
        } else {
            paint.setColor(colorMain);
            paint.setTextSize(textMainSize);
            textHeight = textHeightMain;
            drawDial(canvas, Math.min(width, height) / 2f - radiusBig - padding, true, minute);
        }
    }

    private void drawDial(Canvas canvas, float radius, boolean withC, String[] dials) {
        canvas.save();
        canvas.translate(width / 2f, height / 2f);
        paint.setXfermode(null);
        for (int i = 0; i < dials.length; i++) {
            final float x = (float) (radius * Math.sin(Math.PI / 6 * i));
            final float y = (float) (-radius * Math.cos(Math.PI / 6 * i)) + textHeight / 2f;
            canvas.drawText(dials[i], x, y, paint);
        }

        if (withC) {
            // Draw indicator
            canvas.rotate(sweepAngle);
            paint.setColor(colorIndicator);
            final float left = -indicatorWidth / 2;
            final float top = 0;
            final float right = indicatorWidth / 2;
            final float bottom = -radius;
            canvas.drawRect(left, top, right, bottom, paint);
            // Draw small circle
            canvas.drawCircle(0, 0, radiusSmall, paint);
            canvas.restore();

            // Draw big circle
            final int sc = canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);
            canvas.translate(width / 2f, height / 2f);
            canvas.rotate(sweepAngle);
            canvas.drawCircle(0, -radius, radiusBig, paint);

            canvas.rotate(-sweepAngle);
            // Draw shadow
            paint.setXfermode(xfermodes[1]);
            paint.setColor(mode == 0 && shift ? colorFoucusSub : colorFoucusMain);
            for (int i = 0; i < dials.length; i++) {
                final float x = (float) (radius * Math.sin(Math.PI / 6 * i));
                final float y = (float) (-radius * Math.cos(Math.PI / 6 * i)) + textHeight / 2f;
                canvas.drawText(dials[i], x, y, paint);
            }

            if (sweepAngle % 30 != 0) {
                // Draw big center circle
                canvas.rotate(sweepAngle);
                canvas.drawCircle(0, -radius, radiusSmall * 0.618f, paint);
                canvas.restoreToCount(sc);
            }
        }

        canvas.restore();
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
        final float eX = event.getX();
        final float eY = event.getY();
        final float cX = eX - width / 2f;
        final float cY = -eY + height / 2f;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final boolean oldShift = shift;
                final int oldSweepAngle = sweepAngle;
                sweepAngle = (int) Math.toDegrees(Math.atan(cX / cY));
                if (cX == 0 && cY > 0) {
                    sweepAngle = 0;
                } else if (cX == 0 && cY < 0) {
                    sweepAngle = 180;
                } else if (cX > 0 && cY == 0) {
                    sweepAngle = 90;
                } else if (cX < 0 && cY == 0) {
                    sweepAngle = 270;
                } else if (cX > 0 && cY < 0) {
                    sweepAngle += 180;
                } else if (cX < 0 && cY < 0) {
                    sweepAngle += 180;
                } else if (cX < 0 && cY > 0) {
                    sweepAngle += 360;
                }
                sweepAngle = Math.max(0, sweepAngle);
                sweepAngle = Math.min(360, sweepAngle);
                final int piece = mode == MODE_MINUTE ? 6 : 30;
                sweepAngle = (sweepAngle + piece / 2) / piece * piece;
                if (sweepAngle >= 360) {
                    sweepAngle = 0;
                }

                float r = (float) Math.sqrt(cX * cX + cY * cY);
                shift = r <= Math.min(width, height) / 2f - radiusBig * 2f;
                if (shift != oldShift || sweepAngle != oldSweepAngle) {
                    if (listener != null) {
                        int index = ExChange.angle2Index(mode, shift, sweepAngle);
                        listener.onSelect(mode, ExChange.index2Value(mode, index));
                    }
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setMode(int mode, @IntRange(from = 0, to = 59) int index) {
        this.mode = mode;
        if (mode == MODE_HOUR) {
            index = Math.max(0, index);
            index = Math.min(23, index);
            this.shift = index == 0 || index > 12;
        }
        this.sweepAngle = ExChange.index2Angle(mode, index);
        this.invalidate();
    }

    public interface OnSelectListener {
        void onSelect(int mode, int value);
    }

    public void setOnSelectListener(OnSelectListener l) {
        this.listener = l;
    }

    static class ExChange {

        static int index2Angle(int mode, int index) {
            final int piece = mode == MODE_MINUTE ? 6 : 30;
            return index * piece % 360;
        }

        static int angle2Index(int mode, boolean shift, int angle) {
            final int piece = mode == MODE_MINUTE ? 6 : 30;
            int index = angle / piece;
            if (mode == MODE_HOUR) {
                if (shift) {
                    if (index == 0) {
                        index = 0;
                    } else {
                        index += 12;
                    }
                } else {
                    if (index == 0) {
                        index = 12;
                    }
                }
            }
            return index;
        }

        static int value2Index(int mode, int value) {
            if (mode == MODE_HOUR) {
                return value;
            } else if (mode == MODE_MINUTE) {
                return value;
            }
            return value;
        }

        static int index2Value(int mode, int index) {
            return index;
        }
    }
}
