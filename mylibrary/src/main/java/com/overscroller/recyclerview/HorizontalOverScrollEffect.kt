package com.overscroller.recyclerview

import android.animation.Animator
import android.view.ViewConfiguration
import android.widget.OverScroller
import kotlin.math.abs

class HorizontalOverScrollEffect(val view: IOverScrollViewProxy) : BaseOverScrollEffect() {

    class FlingRunnable(val view: IOverScrollViewProxy,
                        private val listener: Animator.AnimatorListener?) :
            IFlingRunnable {

        var scroller = OverScroller(view.getContext())

        private val overFlingDistance = view.getOverFlingDistance()

        private fun startInner() {
            view.invalidate()
            view.removeCallbacks(this)
            view.postOnAnimation(this)
        }

        override fun run() {
            if (scroller.computeScrollOffset()) {
                val scrollX = view.getOverScrollX()
                val x = scroller.currX
                val delta = x - scrollX
                view.invokeOverScrollX(delta, scrollX, overFlingDistance, false)
                startInner()
            } else {
                listener?.onAnimationEnd(null)
            }
        }

        override fun startSpringBack() {
            if (scroller.springBack(view.getOverScrollX().toInt(), 0, 0,
                            0, 0, 0)) {
                startInner()
            } else {
                listener?.onAnimationEnd(null)
            }
        }

        override fun endFling() {
            view.removeCallbacks(this)
            scroller.abortAnimation()
        }

        override fun startEdgeReached(velocity: Int) {
            val flingMaxV = ViewConfiguration.get(view.getContext()).scaledMaximumFlingVelocity
            val flingDistance = Math.abs(velocity.toFloat()) / flingMaxV * view.getOverFlingDistance()
            scroller.fling(view.getOverScrollX().toInt(), 0, velocity, 0,
                    0, Int.MAX_VALUE, 0, Int.MAX_VALUE)
            scroller.notifyHorizontalEdgeReached(view.getOverScrollX().toInt(), 0, flingDistance.toInt())
            startInner()
        }
    }

    override fun createFlingRunnable(): IFlingRunnable {
        return FlingRunnable(view, null)
    }

    override fun scrollHorizontalBy(dx: Int): Int {
        if (view.canScrollHorizontally(dx)) {
            var offset = view.getOverScrollX()
            /**
             * 存在overScrollView偏移时优先执行overScroll方法
             */
            if (offset * dx > 0.0f) {
                return if (abs(offset) >= abs(dx)) {
                    view.overScroll(offset - dx, 0.0f)
                    dx
                } else {
                    view.overScroll(0.0f, 0.0f)
                    offset.toInt()
                }
            }
        }
        return 0
    }
}