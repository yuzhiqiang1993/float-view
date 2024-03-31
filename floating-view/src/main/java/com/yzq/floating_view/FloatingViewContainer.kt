package com.yzq.floating_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.yzq.application.AppContext
import com.yzq.logger.Logger


/**
 * @description: 悬浮视图容器
 * @author : yuzhiqiang
 */

class FloatingViewContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "FloatingView"
    }

    private var parentWidth: Float = 0f//父视图的宽度
    private var parentHeight: Float = 0f//父视图的高度

    private var floatingViewListener: FloatingViewListener? = null//悬浮视图监听器
    private var draggable = true//是否可拖动
    private var autoMoveToEdge = true//是否自动贴边
    private var marginEdge = 0//边缘的边距

    private var originalRawX = 0f//触摸点的X坐标
    private var originalRawY = 0f//触摸点的Y坐标
    private var viewOriginalX = 0f//悬浮视图的X坐标
    private var viewOriginalY = 0f//悬浮视图的Y坐标

    private var lastTouchDownTime: Long = 0//最后一次触摸按下的时间戳

    /**
     * 设置自定义视图
     */
    fun setCustomView(view: View): FloatingViewContainer {
        Logger.it(TAG, "setCustomView")
        removeAllViews()
        addView(view)
        return this
    }

    /**
     * 设置自定义视图布局资源
     */
    fun setCustomView(@LayoutRes layoutResId: Int): FloatingViewContainer {
        val inflater = LayoutInflater.from(AppContext)
        val view = inflater.inflate(layoutResId, this, false)
        return setCustomView(view)
    }

    /**
     * 设置布局参数
     */
    fun layoutParams(params: ViewGroup.LayoutParams): FloatingViewContainer {
        super.setLayoutParams(params)
        return this
    }

    /**
     * 设置悬浮视图监听器
     */
    fun setFloatingViewListener(listener: FloatingViewListener): FloatingViewContainer {
        Logger.it(TAG, "setFloatingViewListener")
        this.floatingViewListener = listener
        return this
    }

    /**
     * 设置是否可拖动
     */
    fun draggable(draggable: Boolean): FloatingViewContainer {
        this.draggable = draggable
        return this
    }

    /**
     * 设置是否自动贴边
     */
    fun autoMoveToEdge(autoMoveToEdge: Boolean): FloatingViewContainer {
        this.autoMoveToEdge = autoMoveToEdge
        return this
    }

    /**
     * 触摸事件处理
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {

        updateParentContainerSize()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewOriginalX = x//悬浮视图的X坐标
                viewOriginalY = y//悬浮视图的Y坐标
                originalRawX = event.rawX//触摸点的X坐标
                originalRawY = event.rawY//触摸点的Y坐标
                lastTouchDownTime = System.currentTimeMillis()

                Logger.i(
                    TAG,
                    "ACTION_DOWN",
                    "viewOriginalX:$viewOriginalX viewOriginalY:$viewOriginalY originalRawX:$originalRawX originalRawY:$originalRawY"
                )
            }

            MotionEvent.ACTION_MOVE -> {
                updateViewPosition(event)
            }

            MotionEvent.ACTION_UP -> {
                Logger.it(TAG, "ACTION_UP")
                if (isOnClickEvent()) {
                    Logger.it(TAG, "ACTION_UP", "isOnClickEvent")

                    floatingViewListener?.onClick()
                }
                if (autoMoveToEdge) {
                    moveToEdge()
                }

            }
        }
        return true
    }

    private fun updateParentContainerSize() {
        //获取父视图的宽高
        parentWidth = (parent as ViewGroup).width.toFloat()
        parentHeight = (parent as ViewGroup).height.toFloat()
    }

    /**
     * 更新视图位置
     */
    private fun updateViewPosition(event: MotionEvent) {
        if (!draggable) return/*计算目标位置*/
        val desX = viewOriginalX + event.rawX - originalRawX
        val desY = viewOriginalY + event.rawY - originalRawY
        this.x = desX.coerceIn(0f, parentWidth - width)
        this.y = desY.coerceIn(0f, parentHeight - height)
    }

    /**
     * 判断是否是点击事件
     */
    private fun isOnClickEvent(): Boolean {

        val tapTimeout = ViewConfiguration.getTapTimeout().toLong()
        Logger.it(TAG, "isOnClickEvent", "tapTimeout:$tapTimeout")
        return System.currentTimeMillis() - lastTouchDownTime < tapTimeout
    }

    /**
     * 移动到边缘
     */
    private fun moveToEdge() {
        val nearestLeft = this.x < parentWidth / 2//是否靠左边
        val moveDistance =
            if (nearestLeft) marginEdge.toFloat() else parentWidth - width - marginEdge.toFloat()
        move(moveDistance, y)
    }

    /**
     * 移动视图
     */
    private fun move(destX: Float, destY: Float) {
        //从当前位置移动到目标位置，带有动画效果
        animate().setDuration(200).x(destX).y(destY).start()
    }


}
