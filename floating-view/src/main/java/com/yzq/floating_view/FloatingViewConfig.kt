package com.yzq.floating_view

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.GravityInt
import androidx.annotation.LayoutRes
import com.yzq.application.AppContext


/**
 * @description: 配置类
 * @author : yuzhiqiang
 */

class FloatingViewConfig {

    private var defaultLayoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
        Gravity.BOTTOM or Gravity.END
    ) //默认的布局参数

    private var contentView: View? = null//内容视图
    private var moveView: View? = null//移动时的视图，用于移动时显示的view，可选，不设置则默认显示内容视图
    private var edgeView: View? = null//贴边时的视图，用于贴边时显示的视图，可选，不设置则默认显示内容视图
    private var edgeViewScale = 1.0f//贴边视图的显示比例,默认是1.0f,只对edgeView有效

    private var draggable = true//是否可拖拽
    private var autoMoveToEdge = true//是否自动贴边
    private var autoShowEdgeView = true//贴边后是否自动显示贴边view
    private var marginEdge = 0//内容视图贴边是距离屏幕边缘的距离

    fun contentView(view: View) = apply { this.contentView = view }

    fun contentView(@LayoutRes layoutResId: Int) = apply {
        val inflater = LayoutInflater.from(AppContext)
        this.contentView = inflater.inflate(layoutResId, null)
    }

    fun moveView(view: View) = apply { this.moveView = view }

    fun moveView(@LayoutRes layoutResId: Int) = apply {
        val inflater = LayoutInflater.from(AppContext)
        this.moveView = inflater.inflate(layoutResId, null)
    }

    fun edgeView(view: View) = apply { this.edgeView = view }

    fun edgeView(@LayoutRes layoutResId: Int) = apply {
        val inflater = LayoutInflater.from(AppContext)
        this.edgeView = inflater.inflate(layoutResId, null)
    }

    fun draggable(draggable: Boolean) = apply { this.draggable = draggable }

    fun autoShowEdgeView(autoShowEdgeView: Boolean) =
        apply { this.autoShowEdgeView = autoShowEdgeView }

    fun edgeViewScale(scale: Float) = apply { this.edgeViewScale = scale }


    fun marginEdge(marginEdge: Int) = apply { this.marginEdge = marginEdge }

    fun autoMoveToEdge(autoMoveToEdge: Boolean) = apply { this.autoMoveToEdge = autoMoveToEdge }

    fun width(width: Int) = apply { defaultLayoutParams.width = width }

    fun height(height: Int) = apply { defaultLayoutParams.height = height }

    fun bottomMargin(bottomMargin: Int) = apply { defaultLayoutParams.bottomMargin = bottomMargin }

    fun topMargin(topMargin: Int) = apply { defaultLayoutParams.topMargin = topMargin }

    fun leftMargin(leftMargin: Int) = apply { defaultLayoutParams.leftMargin = leftMargin }

    fun rightMargin(rightMargin: Int) = apply { defaultLayoutParams.rightMargin = rightMargin }

    fun gravity(@GravityInt gravity: Int) = apply { defaultLayoutParams.gravity = gravity }

    internal fun config(floatingViewContainer: FloatingViewContainer) {

        floatingViewContainer.layoutParams = defaultLayoutParams


        contentView?.let { floatingViewContainer.contentView(it) }
        moveView?.let { floatingViewContainer.moveView(it) }
        edgeView?.let { floatingViewContainer.edgeView(it) }

        floatingViewContainer.draggable(draggable)
        floatingViewContainer.autoShowEdgeView(autoShowEdgeView)
        floatingViewContainer.edgeViewScale(edgeViewScale)
        floatingViewContainer.marginEdge(marginEdge)
        floatingViewContainer.autoMoveToEdge(autoMoveToEdge)

    }
}