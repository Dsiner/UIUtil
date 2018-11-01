package com.d.lib.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.d.lib.common.utils.log.ULog;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Util集合
 * Created by D on 2017/4/27.
 */
public class Util {
    private static int SCREEN_WIDTH; // 屏幕宽度
    private static int SCREEN_HEIGHT; // 屏幕宽度

    private static class Singleton {
        private static Gson gson = new Gson();
    }

    public static Gson getGsonIns() {
        return Singleton.gson;
    }

    /**
     * Toast提示
     *
     * @param context Context
     * @param msg     Message
     */
    public static void toast(Context context, String msg) {
        if (context == null || TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast提示
     *
     * @param context Context
     * @param msg     Message
     */
    public static void toastLong(Context context, String msg) {
        if (context == null || TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Toast提示
     *
     * @param context Context
     * @param resId   Resource id
     */
    public static void toast(Context context, int resId) {
        if (context == null) {
            return;
        }
        toast(context, context.getString(resId));
    }

    /**
     * 打印当前代码所在线程信息
     */
    public static void printThread(String tag) {
        ULog.d(tag + " Current thread--> Id: " + Thread.currentThread().getId()
                + " Name: " + Thread.currentThread().getName());
    }

    /**
     * 获取屏幕宽度和高度
     *
     * @return int[]{SCREEN_WIDTH, SCREEN_HEIGHT}
     */
    public static int[] getScreenSize(Activity activity) {
        if (SCREEN_WIDTH > 0 && SCREEN_HEIGHT > 0) {
            return new int[]{SCREEN_WIDTH, SCREEN_HEIGHT};
        }
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        if (metric.widthPixels != SCREEN_WIDTH) {
            SCREEN_WIDTH = metric.widthPixels;
            SCREEN_HEIGHT = metric.heightPixels;
        }
        return new int[]{SCREEN_WIDTH, SCREEN_HEIGHT};
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dpValue * (metrics.densityDpi / 160f));
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context context
     * @param spValue DisplayMetrics类中属性scaledDensity
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取字体高度
     */
    public static float getTextHeight(Paint p) {
        Paint.FontMetrics fm = p.getFontMetrics();
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
     * 获取状态栏高度
     */
    public static int getStatuBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38; // 默认为38，貌似大部分是这样的
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = MathManager.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("navigation_bar_height");
            x = MathManager.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 检查是否存在NavigationBar
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 判断某个服务是否正在运行
     */
    public static boolean isServiceRunning(final Context context, final String className) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() <= 0) {
            return false;
        }
        final int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化时间，把毫秒转换成分:秒(00:00)格式
     */
    public static String formatTime(int time) {
        StringBuilder sb;
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        if (min / 10 < 1) {
            sb = new StringBuilder("0");
            sb.append(String.valueOf(min));
        } else {
            sb = new StringBuilder(String.valueOf(min));
        }
        sb.append(":");
        if (sec / 10 < 1) {
            sb.append("0");
        }
        sb.append(String.valueOf(sec));
        return sb.toString();
    }

    /**
     * 返回数据大小(byte)对应文本
     */
    public static String formatSize(long size) {
        DecimalFormat format = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kb = size / 1024f;
            return format.format(kb) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mb = size / (1024 * 1024f);
            return format.format(mb) + "MB";
        } else {
            float gb = size / (1024 * 1024 * 1024f);
            return format.format(gb) + "GB";
        }
    }

    /**
     * 调整TextView字体大小，自适应宽度
     *
     * @param textView TextView
     * @param text     文本
     * @param maxWidth 最大宽度限制
     * @param dpMin    最小dp限制
     * @param dpMax    最大dp限制，默认dp
     */
    public static void autoSize(TextView textView, String text, float maxWidth, float dpMin, float dpMax) {
        Paint paint = textView.getPaint();
        float minSize = Util.dip2px(textView.getContext(), dpMin);
        float textSize = Util.dip2px(textView.getContext(), dpMax);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        // Get the effective width of the current TextView
        int availableWidth = Util.dip2px(textView.getContext(), maxWidth);
        float textWidth = paint.measureText(text);
        while (textWidth > availableWidth) {
            if (textSize < minSize) {
                break;
            }
            textSize = textSize - 1;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize); // The unit passed in here is px
            textWidth = paint.measureText(text) + 2;
        }
        textView.setText(text);
    }

    /**
     * 退出应用
     */
    public static void exit(Context context, int type) {
        switch (type) {
            case 0:
                // 方法1
                System.exit(0);
                break;
            case 1:
                // 方法2
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                break;
            case 2:
                // 方法3
                ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                manager.killBackgroundProcesses(context.getApplicationContext().getPackageName());
                break;
        }
    }
}
