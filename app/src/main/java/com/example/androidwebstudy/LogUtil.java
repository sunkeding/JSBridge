package com.example.androidwebstudy;

import android.util.Log;

public class LogUtil {
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
        Log.d("LogUtil", msg);
    }
}
