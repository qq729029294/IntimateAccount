
package com.nan.utils;

import android.util.Log;

public class LogUtils {
	
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;
    
    // 日志等级
    public static int sLogLevel = VERBOSE;
    
    public static String sDefaultTag = "";
    
    public static void configure(String defaultTag, int logLevel) {
    	sLogLevel = logLevel;
    	sDefaultTag = defaultTag;
    }

    public static void i(String TAG, String msg) {
        if (sLogLevel <= INFO) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String TAG, String msg, Throwable e) {
        if (sLogLevel <= INFO) {
            Log.i(TAG, msg, e);
        }
    }

    public static void e(String TAG, String msg) {
        if (sLogLevel <= ERROR) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (sLogLevel <= ERROR) {
            Log.e(TAG, msg, e);
        }
    }

    public static void d(String TAG, String msg) {
        if (sLogLevel <= DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String TAG, String msg, Throwable e) {
        if (sLogLevel <= DEBUG) {
            Log.d(TAG, msg, e);
        }
    }

    public static void v(String TAG, String msg) {
        if (sLogLevel <= VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String TAG, String msg, Throwable e) {
        if (sLogLevel <= INFO) {
            Log.v(TAG, msg, e);
        }
    }

    public static void w(String TAG, String msg) {
        if (sLogLevel <= WARN) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String TAG, String msg, Throwable e) {
        if (sLogLevel <= INFO) {
            Log.w(TAG, msg, e);
        }
    }

    public static void printStackTrace(Throwable e) {
        if (sLogLevel <= INFO) {
            e.printStackTrace();
        }
    }
}
