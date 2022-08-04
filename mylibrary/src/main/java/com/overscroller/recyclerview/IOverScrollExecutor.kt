package com.overscroller.recyclerview

interface IOverScrollExecutor {
    fun invokeOverScrollX(delta: Float, scroll: Float, maxScroll: Float, touch: Boolean): Boolean
    fun invokeOverScrollY(delta: Float, scroll: Float, maxScroll: Float, touch: Boolean): Boolean
}