package com.overscroller.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

open class OverScrollRecyclerView : RecyclerView, IOverScrollExecutor {

    private var overScrollViewProxy: OverScrollViewProxy? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun draw(canvas: Canvas?) {
        val proxy = overScrollViewProxy
        if (proxy == null) {
            super.draw(canvas)
        } else {
            canvas?.save()
            canvas?.translate(proxy.getOffsetX(), proxy.getOffsetY())
            super.draw(canvas)
            canvas?.restore()
        }
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        overScrollViewProxy = if (layout != null) {
            OverScrollViewProxy(this, this)
        } else {
            null
        }
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
        overScrollViewProxy?.overScroll(scrollX.toFloat(), scrollY.toFloat())
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        overScrollViewProxy?.onTouch(ev)
        return super.onTouchEvent(ev)
    }

    override fun invokeOverScrollX(delta: Float, scroll: Float, maxScroll: Float, touch: Boolean):
            Boolean {
        return overScrollBy(delta.toInt(), 0,
            scroll.toInt(), 0,
            0, 0,
            maxScroll.toInt(), 0, touch)
    }

    override fun invokeOverScrollY(delta: Float, scroll: Float, maxScroll: Float, touch: Boolean):
            Boolean {
        return overScrollBy(0, delta.toInt(),
            0, scroll.toInt(),
            0, 0,
            0, maxScroll.toInt(), touch)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean {
        val result = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
        val embedResult = overScrollViewProxy?.dispatchProxyOverScroll(dx, dy, consumed)?:false
        return result || embedResult
    }
}