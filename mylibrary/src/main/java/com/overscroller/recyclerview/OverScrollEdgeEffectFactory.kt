package com.overscroller.recyclerview

import android.graphics.Canvas
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView

class OverScrollEdgeEffectFactory(private val overScrollView: IOverScrollViewProxy,
                                  private val overScrollEffect: IOverScrollEffect
) :
        RecyclerView.EdgeEffectFactory() {

    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
        return OverScrollEdgeEffect(overScrollView, direction)
    }

    inner class OverScrollEdgeEffect(val view: IOverScrollViewProxy, private val direction: Int) :
            EdgeEffect(view.getContext()) {

        override fun draw(canvas: Canvas?): Boolean {
            return false
        }

        override fun isFinished(): Boolean {
            return true
        }

        override fun onAbsorb(velocity: Int) {
            when (direction) {
                DIRECTION_BOTTOM -> {
                    if (overScrollView.isVertical()) {
                        if (view.getOverScrollY() >= 0.0f) {
                            overScrollEffect.edgeReached(-velocity)
                        }
                    }
                }
                DIRECTION_TOP -> {
                    if (overScrollView.isVertical()) {
                        if (view.getOverScrollY() <= 0.0f) {
                            overScrollEffect.edgeReached(velocity)
                        }
                    }
                }
                DIRECTION_LEFT -> {
                    if (!overScrollView.isVertical()) {
                        if (view.getOverScrollX() <= 0.0f) {
                            overScrollEffect.edgeReached(velocity)
                        }
                    }
                }
                DIRECTION_RIGHT -> {
                    if (!overScrollView.isVertical()) {
                        if (view.getOverScrollX() >= 0.0f) {
                            overScrollEffect.edgeReached(-velocity)
                        }
                    }
                }
            }
        }

        override fun onPull(deltaDistance: Float) {
            onPull(deltaDistance, 0.0f)
        }

        override fun onPull(deltaDistance: Float, displacement: Float) {
            val realDeltaDistance = deltaDistance * view.getHeight()
            LogUtil.d("onPull deltaDistance=$deltaDistance realDeltaDistance=$realDeltaDistance")
            when (direction) {
                DIRECTION_BOTTOM -> {
                    view.invokeOverScrollY(-realDeltaDistance, view.getOverScrollY(),
                            view.getHeight().toFloat(), true)
                }
                DIRECTION_TOP -> {
                    view.invokeOverScrollY(realDeltaDistance, view.getOverScrollY(),
                            view.getHeight().toFloat(), true)
                }
                DIRECTION_LEFT -> {
                    view.invokeOverScrollX(realDeltaDistance, view.getOverScrollX(),
                            view.getWidth().toFloat(), true)
                }
                DIRECTION_RIGHT -> {
                    view.invokeOverScrollX(-realDeltaDistance, view.getOverScrollX(),
                            view.getWidth().toFloat(), true)
                }
            }
        }

        override fun onRelease() {
            when (direction) {
                DIRECTION_BOTTOM -> {
                    if (overScrollView.isVertical()) {
                        if (view.getOverScrollY() < 0) {
                            overScrollEffect.springBack()
                        }
                    }
                }
                DIRECTION_TOP -> {
                    if (overScrollView.isVertical()) {
                        if (view.getOverScrollY() > 0) {
                            overScrollEffect.springBack()
                        }
                    }
                }
                DIRECTION_LEFT -> {
                    if (!overScrollView.isVertical()) {
                        if (view.getOverScrollX() > 0) {
                            overScrollEffect.springBack()
                        }
                    }
                }
                DIRECTION_RIGHT -> {
                    if (!overScrollView.isVertical()) {
                        if (view.getOverScrollX() < 0) {
                            overScrollEffect.springBack()
                        }
                    }
                }
            }
        }
    }
}