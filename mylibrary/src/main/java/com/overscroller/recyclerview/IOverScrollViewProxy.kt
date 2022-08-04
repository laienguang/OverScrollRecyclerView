package com.overscroller.recyclerview

import android.content.Context

interface IOverScrollViewProxy : IOverScrollExecutor {

    fun overScroll(offsetX: Float, offsetY: Float)

    fun getOverScrollX(): Float

    fun getOverScrollY(): Float

    fun getContext(): Context

    fun getHeight(): Int

    fun getWidth(): Int

    fun invalidate()

    fun postOnAnimation(runnable: Runnable)

    fun removeCallbacks(runnable: Runnable): Boolean

    fun getOverFlingDistance(): Float

    fun canScrollVertically(dy: Int): Boolean

    fun canScrollHorizontally(dx: Int): Boolean

    fun isVertical(): Boolean
}