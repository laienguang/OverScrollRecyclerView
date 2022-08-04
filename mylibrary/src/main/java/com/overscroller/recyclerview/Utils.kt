package com.overscroller.recyclerview

import android.os.Build

object Utils {

    fun getDeviceBrand(): String {
        return Build.BRAND
    }

    fun checkIsSmartisan(): Boolean {
        return getDeviceBrand().equals("smartisan", true)
    }
}