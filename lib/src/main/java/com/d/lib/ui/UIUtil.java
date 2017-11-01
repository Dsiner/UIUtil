package com.d.lib.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * util
 * Created by D on 2017/4/19.
 */
public class UIUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dpValue * (metrics.densityDpi / 160f));
    }

    /**
     * 获取字体高度
     */
    public static float getTextHeight(Paint p) {
        Paint.FontMetrics fm = p.getFontMetrics();// 获取字体高度
        return (float) ((Math.ceil(fm.descent - fm.top) + 2) / 2);
    }

    public static int getTextWidth(String str, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.width();
    }

    public static int getTextWidth(String str, TextView tvText) {
        Rect bounds = new Rect();
        TextPaint paint = tvText.getPaint();
        paint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.width();
    }

    /**
     * format a number properly with the given number of digits
     *
     * @param number the number to format
     * @param digits the number of digits
     * @return
     */
    public static String formatDecimal(double number, int digits) {
        number = roundNumber((float) number, digits);
        StringBuffer a = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                a.append(".");
            a.append("0");
        }
        DecimalFormat nf = new DecimalFormat("###,###,###,##0" + a.toString());
        String formatted = nf.format(number);
        return formatted;
    }

    /**
     * Math.pow(...) is very expensive, so avoid calling it and create it
     * yourself.
     */
    private static final int POW_10[] = {
            1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
    };

    public static float roundNumber(float number, int digits) {
        try {
            if (digits == 0) {
                int r0 = (int) Math.round(number);
                return r0;
            } else if (digits > 0) {
                if (digits > 9)
                    digits = 9;
                StringBuffer a = new StringBuffer();
                for (int i = 0; i < digits; i++) {
                    if (i == 0)
                        a.append(".");
                    a.append("0");
                }
                DecimalFormat nf = new DecimalFormat("#" + a.toString());
                String formatted = nf.format(number);
                return Float.valueOf(formatted);
            } else {
                digits = -digits;
                if (digits > 9)
                    digits = 9;
                int r2 = (int) (number / POW_10[digits] + 0.5);
                return r2 * POW_10[digits];
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return number;
        }
    }

    /**
     * 获取屏幕高度和宽度
     */
    public static int[] getScreenSize(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return new int[]{metric.widthPixels, metric.heightPixels};
    }

    /**
     * 调整TextView字体大小，自适应宽度
     *
     * @param textView：textView
     * @param text：文本
     * @param maxWidth：最大宽度限制
     * @param dpMin：最小dp限制
     * @param dpMax：最大dp限制，默认dp
     */
    public static void autoSize(TextView textView, String text, float maxWidth, float dpMin, float dpMax) {
        Paint paint = textView.getPaint();
        float minSize = UIUtil.dip2px(textView.getContext(), dpMin);
        float textSize = UIUtil.dip2px(textView.getContext(), dpMax);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        //获得当前TextView的有效宽度
        int availableWidth = UIUtil.dip2px(textView.getContext(), maxWidth);
        float textWidth = paint.measureText(text);
        while (textWidth > availableWidth) {
            if (textSize < minSize) {
                break;
            }
            textSize = textSize - 1;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);//这里传入的单位是px
            textWidth = paint.measureText(text) + 2;
        }
        textView.setText(text);
    }
}
