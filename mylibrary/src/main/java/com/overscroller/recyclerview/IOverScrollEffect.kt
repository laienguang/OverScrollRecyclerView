package com.overscroller.recyclerview

import android.view.MotionEvent

interface IOverScrollEffect {
    fun edgeReached(velocity: Int)
    fun springBack()
    fun onTouch(event: MotionEvent)
    fun scrollVerticallyBy(dy: Int): Int
    fun scrollHorizontalBy(dx: Int): Int
}