package com.yzq.floating_view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import com.yzq.application.AppContext


/**
 * @description: 悬浮视图容器
 * @author : yuzhiqiang
 */
internal class FloatingViewContainer @JvmOverloads constructor(
    context: Context = AppContext, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FloatingViewListener {

    companion object {
        const val TAG = "FloatingViewContainer"
    }

    private var parentWidth: Float = 0f//父视图的宽度
    private var parentHeight: Float = 0f//父视图的高度

    private var floatingViewListener: FloatingViewListener? = null//悬浮视图监听器

    private var draggable = true//是否可拖动
    private var autoMoveToEdge = true//是否自动贴边
    private var marginEdge = 0//边缘的边距
    private var contentView: View? = null//内容视图，必选
    private var moveView: View? = null//移动时的视图，用于移动时显示的view，可选，不设置则默认显示内容视图
    private var edgeView: View? = null//贴边时的视图，用于贴边时显示的视图，可选，不设置则默认显示内容视图
    private var edgeViewScale = 1.0f//贴边视图的显示比例,默认是1.0f,只对edgeView有效
    private var autoShowEdgeView = true //贴边时是否自动显示贴边视图


    private var touchX = 0f//触摸点的X坐标
    private var touchY = 0f//触摸点的Y坐标
    private var viewX = 0f//悬浮视图的X坐标
    private var viewY = 0f//悬浮视图的Y坐标

    private var lastTouchDownTime: Long = 0//最后一次触摸按下的时间戳


    fun contentView(view: View) {
        contentView = view
        contentView?.let { safeAddView(it) }

    }

    fun edgeView(view: View) {
        edgeView = view
        edgeView?.visibility = View.INVISIBLE
        edgeView?.let { safeAddView(it) }
    }


    fun moveView(view: View) {
        moveView = view
        moveView?.visibility = View.INVISIBLE
        moveView?.let { safeAddView(it) }
    }

    private fun safeAddView(view: View) {
        if (view.parent != null) {
            val parent = view.parent as ViewGroup
            parent.removeView(view)
        }
        addView(view)
    }

    fun edgeViewScale(scale: Float) {
        edgeViewScale = scale
    }

    /**
     * 设置悬浮视图监听器
     */
    fun setFloatingViewListener(listener: FloatingViewListener) {
        this.floatingViewListener = listener
    }

    /**
     * 设置是否可拖动
     */
    fun draggable(draggable: Boolean) {
        this.draggable = draggable
    }

    /**
     * 设置是否自动贴边
     */
    fun autoMoveToEdge(autoMoveToEdge: Boolean) {
        this.autoMoveToEdge = autoMoveToEdge
    }

    /**
     * 设置边缘的边距
     * @param marginEdge Int
     * @return FloatingViewContainer
     */
    fun marginEdge(marginEdge: Int) {
        this.marginEdge = marginEdge
    }

    /**
     * 触摸事件处理
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        updateParentContainerSize()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleActionDown(event)
            }

            MotionEvent.ACTION_MOVE -> {
                handleActionMove(event)
            }

            MotionEvent.ACTION_UP -> {
                handleActionUp()

            }
        }
        return true
    }

    private fun handleActionMove(event: MotionEvent) {
        if (!draggable) return
        val desX = viewX + (event.rawX - touchX)
        val desY = viewY + (event.rawY - touchY)
        this.x = desX.coerceIn(0f, parentWidth - width)
        this.y = desY.coerceIn(0f, parentHeight - height)
        showMoveView()
    }

    private fun handleActionUp() {
        if (isOnClickEvent()) {
            floatingViewListener?.onClick()
        }
        if (autoMoveToEdge) {
            if (autoShowEdgeView) {
                switchToEdgeView()
            } else {
                switchToContentView()
            }

        }
    }


    /**
     * 处理按下事件,记录下当前位置以及时间
     * @param event MotionEvent
     */
    private fun handleActionDown(event: MotionEvent) {
        viewX = x
        viewY = y
        touchX = event.rawX//触摸点的X坐标
        touchY = event.rawY//触摸点的Y坐标
        lastTouchDownTime = System.currentTimeMillis()
    }


    fun autoShowEdgeView(autoShowEdgeView: Boolean) {
        this.autoShowEdgeView = autoShowEdgeView
    }

    private fun showMoveView() {
        contentView?.visibility = View.VISIBLE
        edgeView?.visibility = View.INVISIBLE
        moveView?.let {
            it.visibility = View.VISIBLE
            contentView?.visibility = View.INVISIBLE
        }

    }


    private fun updateParentContainerSize() {
        //获取父视图的宽高
        runCatching {
            parentWidth = (parent as ViewGroup).width.toFloat()
            parentHeight = (parent as ViewGroup).height.toFloat()
        }

    }

    /**
     * 判断是否是点击事件
     */
    private fun isOnClickEvent(): Boolean {
        val tapTimeout = ViewConfiguration.getTapTimeout().toLong()
        return System.currentTimeMillis() - lastTouchDownTime < tapTimeout
    }

    /**
     * 切换到贴边视图
     */
    internal fun switchToEdgeView() {
        updateParentContainerSize()

        if (edgeView == null) {
            switchToContentView()
            return
        }
        edgeView?.let {
            // 计算贴边视图应该显示的宽度
            val edgeViewScaleWidth = (it.width * edgeViewScale).toInt()
            //是否是左边
            val nearestLeft = this.x < parentWidth / 2
            // 计算最终的x坐标
            val moveDistance = if (nearestLeft) {
                //如果是左边的话，那么移动的坐标就是负值，这个值实际上就是贴边视图的宽度减去 edgeViewScaleWidth 的值，也就是最终会显示edgeViewScaleWidth的宽度
                -(it.width - edgeViewScaleWidth)
            } else {
                //如果是右边，那么移动的距离屏幕的宽度
                parentWidth - edgeViewScaleWidth

            }.toFloat()

            move(moveDistance, y)
            // 隐藏内容视图和移动时视图
            edgeView?.visibility = View.VISIBLE
            contentView?.visibility = View.INVISIBLE
            moveView?.visibility = View.INVISIBLE
        }


    }


    /**
     * 切换到内容视图
     */
    internal fun switchToContentView() {
        updateParentContainerSize()
        //计算移动的距离
        val moveDistance =
            if (this.x < parentWidth / 2) marginEdge.toFloat() else parentWidth - width - marginEdge

        move(moveDistance, y)

        edgeView?.visibility = View.INVISIBLE
        contentView?.visibility = View.VISIBLE
        moveView?.visibility = View.INVISIBLE
    }

    /**
     * 移动视图
     */
    private fun move(destX: Float, destY: Float) {
        //从当前位置移动到目标位置，带有动画效果
        this.animate().setDuration(200).x(destX).y(destY).start()
        this.x = destX
        this.y = destY
    }


}
