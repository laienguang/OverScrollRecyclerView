package com.overscroller.recyclerview

import android.view.MotionEvent

abstract class BaseOverScrollEffect : IOverScrollEffect {

    var flingRunnable: IFlingRunnable? = null

    interface IFlingRunnable : Runnable {
        fun endFling()
        fun startEdgeReached(velocity: Int)
        fun startSpringBack()
    }

    abstract fun createFlingRunnable(): IFlingRunnable

    override fun onTouch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                flingRunnable?.endFling()
                flingRunnable = null
                LogUtil.d("down x=${event.x} y=${event.y}")
            }
            MotionEvent.ACTION_MOVE -> {
                LogUtil.d("move x=${event.x} y=${event.y}")
            }
        }
    }


    override fun edgeReached(velocity: Int) {
        flingRunnable?.endFling()
        flingRunnable = createFlingRunnable()
        flingRunnable?.startEdgeReached(velocity)
    }

    override fun springBack() {
        flingRunnable?.endFling()
        flingRunnable = createFlingRunnable()
        flingRunnable?.startSpringBack()
    }

    override fun scrollHorizontalBy(dx: Int): Int {
        TODO("Not yet implemented")
    }

    override fun scrollVerticallyBy(dy: Int): Int {
        TODO("Not yet implemented")
    }
}