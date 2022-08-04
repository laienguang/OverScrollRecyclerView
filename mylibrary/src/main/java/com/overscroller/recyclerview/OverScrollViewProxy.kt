package com.overscroller.recyclerview

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.Float.max
import kotlin.math.abs

class OverScrollViewProxy(private val view: RecyclerView, private val target: IOverScrollExecutor) : IOverScrollViewProxy,
        View.OnLayoutChangeListener {

    var overScrollEffect: IOverScrollEffect? = null
    private var offsetY = 0.0f
    private var offsetX = 0.0f
    private var overFlingDistance = 0.0f
    private var isVertical = true

    init {
        view.overScrollMode = View.OVER_SCROLL_ALWAYS
        overFlingDistance = ViewConfiguration.get(view.context).scaledOverflingDistance.toFloat()
        view.addOnLayoutChangeListener(this)
        val layoutManager = view.layoutManager
        val scrollEffect: IOverScrollEffect
        if (isVerticalLayout(layoutManager)) {
            scrollEffect = VerticalOverScrollEffect(this)
            isVertical = true
        } else if (isHorizontalLayout(layoutManager)) {
            scrollEffect = HorizontalOverScrollEffect(this)
            isVertical = false
        } else {
            throw IllegalArgumentException("LayoutManager must be vertical or horizontal layout")
        }
        val edgeEffectFactory = OverScrollEdgeEffectFactory(this, scrollEffect)
        this.overScrollEffect = scrollEffect
        view.edgeEffectFactory = edgeEffectFactory
    }

    private fun isVerticalLayout(layoutManager: RecyclerView.LayoutManager?): Boolean {
        if (layoutManager is LinearLayoutManager ||
                layoutManager is StaggeredGridLayoutManager) {
            val orientation = if (layoutManager is LinearLayoutManager) layoutManager.orientation
            else (layoutManager as StaggeredGridLayoutManager).orientation
            if (orientation == RecyclerView.VERTICAL) {
                return true
            }
        }
        return false
    }

    private fun isHorizontalLayout(layoutManager: RecyclerView.LayoutManager?): Boolean {
        if (layoutManager is LinearLayoutManager ||
                layoutManager is StaggeredGridLayoutManager) {
            val orientation = if (layoutManager is LinearLayoutManager) layoutManager.orientation
            else (layoutManager as StaggeredGridLayoutManager).orientation
            if (orientation == RecyclerView.HORIZONTAL) {
                return true
            }
        }
        return false
    }

    override fun overScroll(offsetX: Float, offsetY: Float) {
        this.offsetY = offsetY
        this.offsetX = offsetX
        LogUtil.d("overScroll x=$offsetX, y=$offsetY")
        invalidate()
    }

    override fun getOverScrollX(): Float {
        return this.offsetX
    }

    override fun getOverScrollY(): Float {
        return this.offsetY
    }

    override fun invokeOverScrollY(delta: Float, scroll: Float,
                                   maxScroll: Float, touch: Boolean): Boolean {
        return if (isVertical) {
            LogUtil.d("invokeOverScrollY delta=$delta, scroll=$scroll, maxscroll=$maxScroll")
            if (Utils.checkIsSmartisan()) {
                target.invokeOverScrollY(delta, scroll, maxScroll, touch)
            } else {
                if (!touch) {
                    target.invokeOverScrollY(delta, scroll, max(maxScroll, abs(getOverScrollY())), touch)
                } else {
                    val arg = (getHeight() - abs(getOverScrollY()))/getHeight()
                    target.invokeOverScrollY(delta / 3.0f * arg, scroll, maxScroll, touch)
                }
            }
        } else {
            false
        }
    }

    override fun invokeOverScrollX(delta: Float, scroll: Float,
                                   maxScroll: Float, touch: Boolean): Boolean {
        return if (!isVertical) {
            LogUtil.d("invokeOverScrollX delta=$delta, scroll=$scroll, maxscroll=$maxScroll")
            if (Utils.checkIsSmartisan()) {
                target.invokeOverScrollX(delta, scroll, maxScroll, touch)
            } else {
                if (!touch) {
                    target.invokeOverScrollX(delta, scroll, max(maxScroll, abs(getOverScrollX())), touch)
                } else {
                    val arg = (getWidth() - abs(getOverScrollX()))/getWidth()
                    target.invokeOverScrollX(delta / 3.0f * arg, scroll, maxScroll, touch)
                }
            }
        } else {
            false
        }
    }

    override fun getContext(): Context {
        return view.context
    }

    override fun getHeight(): Int {
        return view.height
    }

    override fun getWidth(): Int {
        return view.width
    }

    override fun invalidate() {
        view.invalidate()
    }

    override fun postOnAnimation(runnable: Runnable) {
        view.postOnAnimation(runnable)
    }

    override fun removeCallbacks(runnable: Runnable): Boolean {
        return view.removeCallbacks(runnable)
    }

    override fun getOverFlingDistance(): Float {
        return overFlingDistance
    }

    override fun canScrollVertically(dy: Int): Boolean {
        return view.canScrollVertically(dy)
    }

    override fun canScrollHorizontally(dx: Int): Boolean {
        return view.canScrollHorizontally(dx)
    }

    fun getOffsetX(): Float {
        return offsetX
    }

    fun getOffsetY(): Float {
        return offsetY
    }

    fun onTouch(event: MotionEvent?) {
        if (event != null) {
            overScrollEffect?.onTouch(event)
        }
    }

    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int,
                                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        overFlingDistance = if (isVertical) {
            (bottom - top) / 2.0f
        } else {
            (right - left) / 2.0f
        }
    }

    override fun isVertical(): Boolean {
        return isVertical
    }

    fun dispatchProxyOverScroll(dxOrg: Int, dyOrg: Int, consumedOrg: IntArray?) : Boolean {
        if (dxOrg == 0 && dyOrg != 0) {
            val consumed = consumedOrg ?: IntArray(2)
            val dy = dyOrg - consumed[1]
            if (dy != 0) {
                val result = overScrollEffect?.scrollVerticallyBy(dy)?:0
                if (result != 0) {
                    consumed[1] += result
                    return true
                }
            }

        } else if (dyOrg == 0 && dxOrg != 0) {
            val consumed = consumedOrg ?: IntArray(2)
            val dx = dxOrg - consumed[0]
            val result = overScrollEffect?.scrollHorizontalBy(dx)?:0
            if (result != 0) {
                consumed[0] += result
                return true
            }
        }
        return false
    }
}