package com.sean.android.example.base.util;

import android.util.Log;

/**
 * Created by sean on 2017. 3. 9..
 */

public class Logger {
    private static final String TAG = Logger.class.getSimpleName();

    private static final boolean DEBUG = true;


    public static void d(String logData) {
        d(TAG, logData);
    }

    public static void d(Object obj, String logData) {
        d(obj.getClass().getSimpleName(), logData);
    }

    public static void d(String tag, String logData) {
        if (DEBUG) {
            Log.d(tag, logData);
        }
    }

    public static void e(String logData) {
        e(TAG, logData);
    }

    public static void e(String tag, String logData) {
        Log.e(tag, logData);
    }

    public static void i(String logData) {
        i(TAG, logData);
    }

    public static void i(String tag, String logData) {
        Log.i(tag, logData);
    }

    public static void v(String logData) {
        v(TAG, logData);
    }

    public static void v(String tag, String logData) {
        if (DEBUG) {
            Log.v(tag, logData);
        }
    }

    public static void w(String logData) {
        w(TAG, logData);
    }

    public static void w(String tag, String logData) {
        if (DEBUG) {
            Log.w(tag, logData);
        }
    }
}
