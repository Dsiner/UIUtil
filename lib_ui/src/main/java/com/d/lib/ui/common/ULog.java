package com.d.lib.ui.common;

import android.text.TextUtils;
import android.util.Log;

/**
 * Log tool, you can print the log class name, method name, line number
 */
public class ULog {

    private static final String LOG_TAG = "ULog";

    /**
     * Debug switch
     */
    private static boolean DEVELOP_MODE = true;

    public static void setDebug(boolean debug) {
        DEVELOP_MODE = debug;
    }

    private ULog() {
    }

    public static void v(String message) {
        if (!DEVELOP_MODE || TextUtils.isEmpty(message)) {
            return;
        }
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final int i = 1;
        final StackTraceElement ste = stack[i];
        Log.println(Log.VERBOSE, LOG_TAG, String.format("[%s][%s][%s]%s", ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), message));
    }

    public static void d(String message) {
        if (!DEVELOP_MODE || TextUtils.isEmpty(message)) {
            return;
        }
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final int i = 1;
        final StackTraceElement ste = stack[i];
        Log.println(Log.DEBUG, LOG_TAG, String.format("[%s][%s][%s]%s", ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), message));
    }

    public static void i(String message) {
        if (!DEVELOP_MODE || TextUtils.isEmpty(message)) {
            return;
        }
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final int i = 1;
        final StackTraceElement ste = stack[i];
        Log.println(Log.INFO, LOG_TAG, String.format("[%s][%s][%s]%s", ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), message));
    }

    public static void w(String message) {
        if (!DEVELOP_MODE || TextUtils.isEmpty(message)) {
            return;
        }
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final int i = 1;
        final StackTraceElement ste = stack[i];
        Log.println(Log.WARN, LOG_TAG, String.format("[%s][%s][%s]%s", ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), message));
    }

    public static void e(String message) {
        if (!DEVELOP_MODE || TextUtils.isEmpty(message)) {
            return;
        }
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final int i = 1;
        final StackTraceElement ste = stack[i];
        Log.println(Log.ERROR, LOG_TAG, String.format("[%s][%s][%s]%s", ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), message));
    }
}
