package com.d.lib.common.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * MathManager
 * Created by D on 2017/4/29.
 */
public class MathManager {
    // 默认除法运算精度
    private static final int DEFAULT_DIV_SCALE = 10;

    /**
     * 字符串转double
     */
    public static double parseDouble(String str) {
        if (!TextUtils.isEmpty(str)) {
            double result = 0;
            try {
                result = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return result;
        }
        return 0;
    }

    /**
     * 字符串转float
     */
    public static float parseFloat(String str) {
        if (!TextUtils.isEmpty(str)) {
            float result = 0;
            try {
                result = Float.parseFloat(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return result;
        }
        return 0;
    }

    /**
     * 字符串转int
     */
    public static int parseInt(String str) {
        if (TextUtils.isDigitsOnly(str)) {
            int result = 0;
            try {
                result = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return result;
        }
        return 0;
    }

    /**
     * 字符串转int,
     */
    public static int parseInt2(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (TextUtils.isDigitsOnly(str)) {
            int result = 0;
            try {
                result = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return result;
        }
        return 0;
    }

    /**
     * 字符串转long,
     */
    public static long parseLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (TextUtils.isDigitsOnly(str)) {
            long result = 0;
            try {
                result = Long.parseLong(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return result;
        }
        return 0;
    }


    /**
     * 格式化价格,精确到1位小数
     *
     * @param price
     * @return
     */
    public static String formatFloat(double price) {
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(price);
        } catch (Exception e) {
            return "0.0";
        }
    }

    public static String formatFloat(String price) {
        try {
            double p = Double.parseDouble(price);
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(p);
        } catch (Exception e) {
            return "0.0";
        }
    }

    public static String formatDouble(double price) {
        try {
            DecimalFormat df = new DecimalFormat("#0.000");
            return df.format(price);
        } catch (Exception e) {
            return "0.0";
        }
    }

    public static boolean checkIsInt(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public static boolean checkIsDouble(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1
     * @param v2
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        try {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));

            BigDecimal b2 = new BigDecimal(Double.toString(v2));

            return b1.add(b2).doubleValue();
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 提供精确的加法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数数学加和，以字符串格式返回
     */
    public static String add(String v1, String v2) {
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.add(b2).toString();
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1
     * @param v2
     * @return 两个参数的差
     */
    public static double subtract(double v1, double v2) {
        try {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.subtract(b2).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数数学差，以字符串格式返回
     */
    public static String subtract(String v1, String v2) {
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.subtract(b2).toString();
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     * @param v2
     * @return 两个参数的积
     */
    public static double multiply(double v1, double v2) {
        try {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.multiply(b2).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数的数学积，以字符串格式返回
     */
    public static String multiply(String v1, String v2) {
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.multiply(b2).toString();
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * <p>
     * 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * <p>
     * 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
     *
     * @param v1
     * @param v2
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale) {
        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * <p>
     * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale, int round_mode) {
        try {
            if (scale < 0) {
                throw new IllegalArgumentException(
                        "The scale must be a positive integer or zero");
            }
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, round_mode).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * <p>
     * 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
     *
     * @param v1
     * @param v2
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * <p>
     * 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
     *
     * @param v1
     * @param v2
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale) {
        return divide(v1, v2, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * <p>
     * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale, int round_mode) {
        try {
            if (scale < 0) {
                throw new IllegalArgumentException(
                        "The scale must be a positive integer or zero");
            }
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.divide(b2, scale, round_mode).toString();
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v          需要四舍五入的数字
     * @param scale      小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale, int round_mode) {
        try {
            if (scale < 0) {
                throw new IllegalArgumentException(
                        "The scale must be a positive integer or zero");
            }
            BigDecimal b = new BigDecimal(Double.toString(v));
            return b.setScale(scale, round_mode).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale) {
        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v          需要四舍五入的数字
     * @param scale      小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale, int round_mode) {
        try {
            if (scale < 0) {
                throw new IllegalArgumentException(
                        "The scale must be a positive integer or zero");
            }
            BigDecimal b = new BigDecimal(v);
            return b.setScale(scale, round_mode).toString();
        } catch (Exception e) {
            return "0";
        }
    }
}
