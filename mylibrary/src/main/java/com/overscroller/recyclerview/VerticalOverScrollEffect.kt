package com.overscroller.recyclerview

import android.animation.Animator
import android.view.ViewConfiguration
import android.widget.OverScroller
import kotlin.math.abs

class VerticalOverScrollEffect(val view: IOverScrollViewProxy) : BaseOverScrollEffect() {

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
                val scrollY = view.getOverScrollY()
                val y = scroller.currY
                val delta = y - scrollY
                view.invokeOverScrollY(delta, scrollY, overFlingDistance, false)
                startInner()
            } else {
                listener?.onAnimationEnd(null)
            }
        }

        override fun startSpringBack() {
            if (scroller.springBack(0, view.getOverScrollY().toInt(), 0,
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
            scroller.fling(0, view.getOverScrollY().toInt(), 0, velocity,
                    0, Int.MAX_VALUE, 0, Int.MAX_VALUE)
            scroller.notifyVerticalEdgeReached(view.getOverScrollY().toInt(), 0, flingDistance.toInt())
            startInner()
        }
    }

    override fun createFlingRunnable(): IFlingRunnable {
        return FlingRunnable(view, null)
    }

    override fun scrollVerticallyBy(dy: Int): Int {
        if (view.canScrollVertically(dy)) {
            var offset = view.getOverScrollY()
            /**
             * 存在overScrollView偏移时优先执行overScroll方法
             */
            if (offset * dy > 0.0f) {
                return if (abs(offset) >= abs(dy)) {
                    view.overScroll(0.0f, offset - dy)
                    dy
                } else {
                    view.overScroll(0.0f, 0.0f)
                    offset.toInt()
                }
            }
        }
        return 0
    }
}