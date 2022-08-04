package com.overscroller.recyclerview

import android.util.Log

object LogUtil {
    private const val TAG = "OverScrollRecyclerView"

    fun e(msg: String): Int {
        return Log.e(TAG, msg)
    }

    fun e(msg: String?, tr: Throwable?): Int {
        return Log.e(TAG, msg, tr)
    }

    fun d(msg: String) {
        Log.d(TAG, msg)
    }
}