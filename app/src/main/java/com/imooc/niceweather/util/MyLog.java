package com.imooc.niceweather.util;

import android.util.Log;

/**
 * Created by xhx12366 on 2017-09-13.
 */

public class MyLog {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int LEVEL = 0;
    public static void v(String tag, String msg){
        if(LEVEL < VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if(LEVEL < DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if(LEVEL < INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if(LEVEL < WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(LEVEL < ERROR) {
            Log.e(tag, msg);
        }
    }
}
