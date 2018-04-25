package com.d.lib.ui.view.vs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.ui.view.R;
import com.d.lib.ui.common.UIUtil;

/**
 * 使用：
 * VSIitem vsIitemA = new VSIitem("A", true); //true:已选中，false:未选中
 * VSIitem vsIitemB = new VSIitem("B", false);
 * vsView.setStrCompareA(vsIitemA)
 * .setStrCompareB(vsIitemB)
 * .setPercent(0.85f, true);//param1:若为50%请传-1，param2：false为只更值不刷新，true为更值并刷新view
 * <p>
 * Created by D on 2017/2/28.
 */
public class VSView extends View {
    private int width;//view的宽度
    private int height;//view的高度
    private float padding;//对比条距两端间距
    private float hPercent;//对比条高度
    private float radius;//对比条的圆角矩形弧度

    private Rect rect;
    private RectF rectF;
    private Paint paintA;//A类颜色的画笔
    private Paint paintB;//B类颜色的画笔
    private Paint paintTxt;//仅用于绘制文字的画笔
    private Path path;//通用路径
    private float margin;//两圆与对比条的垂直间距
    private float marginTxt;//文字与圆的水平间距

    private OnVSItemClickListen listener;//listener

    private float percent = 0.5f;//对比A项所占百分比 范围0-1

    private int dIndex = -1;//actionDown按压时的位置
    private int uIndex = 0;//actionUp松开时的位置
    private int mTouchSlop;//最小视为移动距离
    private float dX, dY;//actionDown的坐标(dx,dy)
    private boolean dInvaild;//标志位,点击是否有效（true有效：点中了圆，false无效:未点中圆）

    private float spaceHalf;//对比条，中间空隙的一半宽度
    private float tanW;//对比条，中间空隙的偏斜宽度
    private Bitmap bitmapA;//对比A项正常图片
    private Bitmap bitmapAP;//对比A项按压图片
    private Bitmap bitmapAS;//对比A项选中图片
    private Bitmap bitmapAU;//对比A项未选中图片
    private Bitmap bitmapB;//对比B项正常图片
    private Bitmap bitmapBP;//对比B项按压图片
    private Bitmap bitmapBS;//对比B项选中图片
    private Bitmap bitmapBU;//对比B项未选中图片
    private Rect rectBp;//仅用于图片Rect

    private VSItem itemA;//左项
    private VSItem itemB;//右项

    public VSView(Context context) {
        this(context, null);
    }

    public VSView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VSView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int textSize = UIUtil.dip2px(context, 13);
        padding = UIUtil.dip2px(context, 2);
        hPercent = UIUtil.dip2px(context, 3);
        tanW = UIUtil.dip2px(context, 0.5f);
        spaceHalf = UIUtil.dip2px(context, 1);
        margin = UIUtil.dip2px(context, 4);
        marginTxt = UIUtil.dip2px(context, 6);
        radius = UIUtil.dip2px(context, 13.5f);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        bitmapA = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        bitmapAP = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        bitmapAS = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        bitmapAU = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        bitmapB = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        bitmapBP = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        bitmapBS = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);
        bitmapBU = BitmapFactory.decodeResource(getResources(), R.drawable.lib_ui_view_vs_icon);

        rectBp = new Rect();
        rect = new Rect();
        rectF = new RectF();

        path = new Path();

        int colorTxt = Color.parseColor("#7c838a");

        paintTxt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTxt.setTextSize(textSize);
        paintTxt.setTextAlign(Paint.Align.LEFT);
        paintTxt.setColor(colorTxt);
        paintTxt.setAlpha(0xbf);

        paintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintA.setColor(Color.parseColor("#E3542B"));
        paintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintB.setColor(Color.parseColor("#4ABC00"));

        itemA = new VSItem("", false);
        itemB = new VSItem("", false);
        culcPercet();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (height == 0 || width == 0) {
            return;
        }
        if (percent == 0) {
            drawRoundRect(canvas, rect, rectF, paintB, hPercent / 2,
                    padding, height - hPercent, width - padding, height);
        } else if (percent == 1) {
            drawRoundRect(canvas, rect, rectF, paintA, hPercent / 2,
                    padding, height - hPercent, width - padding, height);
        } else {
            drawRoundRect(canvas, rect, rectF, paintA, hPercent / 2,
                    padding, height - hPercent, padding + hPercent, height);
            drawRoundRect(canvas, rect, rectF, paintB, hPercent / 2,
                    width - hPercent - padding, height - hPercent, width - padding, height);

            float offset = (width - padding * 2 - hPercent) * percent + padding + hPercent / 2;
            if (offset < padding + hPercent + spaceHalf + tanW) {
                //限定最小值
                offset = padding + hPercent + spaceHalf + tanW;
            } else if (offset > width - padding - hPercent - spaceHalf - tanW) {
                //限定最大值
                offset = width - padding - hPercent - spaceHalf - tanW;
            }

            float left = padding + hPercent / 2;
            float right = offset - spaceHalf;
            drawPath(canvas, path, paintA,
                    left, left, right + tanW, right - +tanW,
                    height - hPercent, height, height - hPercent, height);

            left = offset + spaceHalf;
            right = width - padding - hPercent / 2;
            drawPath(canvas, path, paintB,
                    left + tanW, left - tanW, right, right,
                    height - hPercent, height, height - hPercent, height);
        }

        float cy = height - hPercent - margin - radius;
        drawBitmap(canvas, cy);

        float textHeght = UIUtil.getTextHeight(paintTxt);
        paintTxt.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(itemA.mainText + " " + itemA.percent, radius * 2 + marginTxt, cy + textHeght / 2, paintTxt);
        paintTxt.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(itemB.mainText + " " + itemB.percent, width - radius * 2 - marginTxt, cy + textHeght / 2, paintTxt);
    }

    private void drawBitmap(Canvas canvas, float cy) {
        Bitmap bpA = bitmapA, bpB = bitmapB;
        if (!itemA.isChecked && !itemB.isChecked) {
            //A、B项都未选中
            if (dInvaild) {
                if (dIndex == 0) {
                    bpA = bitmapAP;
                    bpB = bitmapB;
                } else if (dIndex == 1) {
                    bpA = bitmapA;
                    bpB = bitmapBP;
                }
            }
        } else {
            if (itemA.isChecked) {
                bpA = bitmapAS;
                bpB = bitmapBU;
            } else {
                bpA = bitmapAU;
                bpB = bitmapBS;
            }
        }
        rectBp.set(0, (int) (cy - radius), (int) (radius * 2), (int) (cy + radius));
        canvas.drawBitmap(bpA, null, rectBp, null);
        rectBp.set((int) (width - radius * 2), (int) (cy - radius), width, (int) (cy + radius));
        canvas.drawBitmap(bpB, null, rectBp, null);
    }

    private void drawPath(Canvas canvas, Path path, Paint paint,
                          float ltX, float lbX, float rtX, float rbX,
                          float ltY, float lbY, float rtY, float rbY) {
        path.reset();
        path.moveTo(ltX, ltY);
        path.lineTo(lbX, lbY);
        path.lineTo(rbX, rbY);
        path.lineTo(rtX, rtY);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * draw圆角矩形
     *
     * @param canvas:canvas
     * @param rect:rect
     * @param rectF:recF
     * @param paint:paint
     * @param rxy:radius
     * @param left:left
     * @param top:top
     * @param right:right
     * @param bottom:bottom
     */
    private void drawRoundRect(Canvas canvas, Rect rect, RectF rectF, Paint paint, float rxy,
                               float left, float top, float right, float bottom) {
        rect.set((int) left, (int) top, (int) right, (int) bottom);
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rxy, rxy, paint);
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
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = event.getX();
                dY = event.getY();
                float cline = height - hPercent - margin;
                if (eX >= 0 && eX <= radius * 2 && eY >= cline - radius * 2 && eY <= cline) {
                    dIndex = 0;
                    dInvaild = true;//点击有效
                } else if (eX >= width - radius * 2 && eX <= width && eY >= cline - radius * 2 && eY <= cline) {
                    dIndex = 1;
                    dInvaild = true;//点击有效
                } else {
                    dIndex = -1;
                    dInvaild = false;//点击无效
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(eX - dX) > mTouchSlop || Math.abs(eY - dY) > mTouchSlop) {
                    dInvaild = false;//点击无效
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (dInvaild) {
                    float clineF = height - hPercent - margin;
                    if (eX >= 0 && eX <= radius * 2 && eY >= clineF - radius * 2 && eY <= clineF) {
                        uIndex = 0;
                    } else if (eX >= width - radius * 2 && eX <= width && eY >= clineF - radius * 2 && eY <= clineF) {
                        uIndex = 1;
                    }
                    if (uIndex == dIndex) {
                        if (listener != null) {
                            if (uIndex == 0) {
                                listener.onItemClick(uIndex, itemA);
                            } else if (uIndex == 1) {
                                listener.onItemClick(uIndex, itemB);
                            }
                        }
                    }
                }
                //reset
                dX = 0;
                dY = 0;
                dIndex = -1;
                dInvaild = false;
                invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
                //reset
                dX = 0;
                dY = 0;
                dIndex = -1;
                dInvaild = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void culcPercet() {
        if (percent == -1) {
            itemA.percent = itemB.percent = "";
            percent = 0.5f;
        } else {
            itemA.percent = UIUtil.formatDecimal(percent * 100, 2) + "%";
            itemB.percent = UIUtil.formatDecimal(100 - percent * 100, 2) + "%";
        }
    }

    public VSView setStrCompareA(VSItem strCompareA) {
        this.itemA = strCompareA;
        return this;
    }

    public VSView setStrCompareB(VSItem strCompareB) {
        this.itemB = strCompareB;
        return this;
    }

    /**
     * 动态刷新-设置对比项A所占百分比并重新绘制-格式为0.00
     *
     * @param percentA:percentA
     * @param isRefrsh:isRefrsh
     */
    public void setPercent(float percentA, boolean isRefrsh) {
        percent = percentA;
        culcPercet();
        if (isRefrsh) {
            invalidate();
        }
    }

    public interface OnVSItemClickListen {
        void onItemClick(int index, VSItem iitem);
    }

    public void setOnVSItemSelectListener(OnVSItemClickListen listener) {
        this.listener = listener;
    }
}