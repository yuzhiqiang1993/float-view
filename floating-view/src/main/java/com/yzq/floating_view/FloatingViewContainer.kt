package com.yzq.floating_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 悬浮视图容器
 *
 * 支持拖动、贴边、视图切换等功能
 *
 * @author yuzhiqiang
 */
internal class FloatingViewContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "FloatingViewContainer"
        private const val ANIMATION_DURATION_MS = 200L
    }

    private var parentWidth: Float = 0f // 父视图的宽度
    private var parentHeight: Float = 0f // 父视图的高度

    private var lastX = 0f // 记录最新的 X 坐标
    private var lastY = 0f // 记录最新的 Y 坐标

    private var floatingViewListener: FloatingViewListener? = null // 悬浮视图监听器

    private var draggable = true // 是否可拖动
    private var autoMoveToEdge = true // 是否自动贴边
    private var marginEdge = 0 // 边缘的边距
    private var contentView: View? = null // 内容视图，必选
    private var moveView: View? = null // 移动时的视图，用于移动时显示
    private var edgeView: View? = null // 贴边时的视图，用于贴边时显示
    private var edgeViewScale = 1.0f // 贴边视图的显示比例
    private var autoShowEdgeView = true // 贴边时是否自动显示贴边视图


    private var touchX = 0f // 触摸点的X坐标
    private var touchY = 0f // 触摸点的Y坐标
    private var viewX = 0f // 悬浮视图的X坐标
    private var viewY = 0f // 悬浮视图的Y坐标

    private var lastTouchDownTime: Long = 0 // 最后一次触摸按下的时间戳


    fun contentView(view: View) {
        contentView = view
        contentView?.let { safeAddView(it) }
    }

    fun contentView(@androidx.annotation.LayoutRes layoutResId: Int) {
        val view = LayoutInflater.from(context).inflate(layoutResId, this, false)
        contentView(view)
    }

    fun edgeView(view: View) {
        edgeView = view
        edgeView?.visibility = View.INVISIBLE
        edgeView?.let { safeAddView(it) }
    }

    fun edgeView(@androidx.annotation.LayoutRes layoutResId: Int) {
        val view = LayoutInflater.from(context).inflate(layoutResId, this, false)
        edgeView(view)
    }

    fun moveView(view: View) {
        moveView = view
        moveView?.visibility = View.INVISIBLE
        moveView?.let { safeAddView(it) }
    }

    fun moveView(@androidx.annotation.LayoutRes layoutResId: Int) {
        val view = LayoutInflater.from(context).inflate(layoutResId, this, false)
        moveView(view)
    }

    private fun safeAddView(view: View) {
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }
        addView(view)
    }

    fun edgeViewScale(scale: Float) {
        edgeViewScale = scale
    }

    /**
     * 设置悬浮视图监听器
     * 
     * @param listener 悬浮视图监听器
     */
    fun setFloatingViewListener(listener: FloatingViewListener) {
        this.floatingViewListener = listener
    }

    /**
     * 设置是否可拖动
     * 
     * @param draggable 是否可拖动
     */
    fun draggable(draggable: Boolean) {
        this.draggable = draggable
    }

    /**
     * 设置是否自动贴边
     * 
     * @param autoMoveToEdge 是否自动贴边
     */
    fun autoMoveToEdge(autoMoveToEdge: Boolean) {
        this.autoMoveToEdge = autoMoveToEdge
    }

    /**
     * 设置边缘的边距
     * 
     * @param marginEdge 边缘的边距
     */
    fun marginEdge(marginEdge: Int) {
        this.marginEdge = marginEdge
    }

    /**
     * 触摸事件处理
     * 
     * @param event 触摸事件
     * @return true 表示事件已处理
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 恢复位置
        if (lastX != 0f || lastY != 0f) {
            this.x = lastX
            this.y = lastY
        }
    }

    /**
     * 处理移动事件
     * 
     * @param event 触摸事件
     */
    private fun handleActionMove(event: MotionEvent) {
        if (!draggable) {
            FloatViewLog.d(TAG, "不可拖动，忽略移动事件")
            return
        }
        val destinationX = viewX + (event.rawX - touchX)
        val destinationY = viewY + (event.rawY - touchY)
        this.x = destinationX.coerceIn(0f, parentWidth - width)
        this.y = destinationY.coerceIn(0f, parentHeight - height)
        lastX = this.x
        lastY = this.y
        showMoveView()
    }

    /**
     * 处理抬起事件
     */
    private fun handleActionUp() {
        if (isOnClickEvent()) {
            FloatViewLog.d(TAG, "触发点击事件")
            floatingViewListener?.onClick()
            restoreContentView()
            return
        }
        if (autoMoveToEdge) {
            if (autoShowEdgeView) {
                FloatViewLog.d(TAG, "自动贴边并显示贴边视图")
                switchToEdgeView()
            } else {
                FloatViewLog.d(TAG, "自动贴边并显示内容视图")
                switchToContentView()
            }
        } else {
            // 如果不自动贴边，松手后单纯恢复显示内容视图
            restoreContentView()
        }
    }

    /**
     * 仅恢复内容视图的显示状态，不改变位置
     */
    private fun restoreContentView() {
        contentView?.visibility = View.VISIBLE
        moveView?.visibility = View.INVISIBLE
        edgeView?.visibility = View.INVISIBLE
    }


    /**
     * 处理按下事件,记录下当前位置以及时间
     * 
     * @param event 触摸事件
     */
    private fun handleActionDown(event: MotionEvent) {
        viewX = x
        viewY = y
        touchX = event.rawX // 触摸点的X坐标
        touchY = event.rawY // 触摸点的Y坐标
        lastTouchDownTime = System.currentTimeMillis()
    }


    /**
     * 设置贴边时是否自动显示贴边视图
     * 
     * @param autoShowEdgeView 是否自动显示贴边视图
     */
    fun autoShowEdgeView(autoShowEdgeView: Boolean) {
        this.autoShowEdgeView = autoShowEdgeView
    }

    /**
     * 显示移动时的视图
     */
    private fun showMoveView() {
        contentView?.visibility = View.VISIBLE
        edgeView?.visibility = View.INVISIBLE
        moveView?.let { view ->
            view.visibility = View.VISIBLE
            contentView?.visibility = View.INVISIBLE
        }

    }


    /**
     * 更新父容器尺寸
     * 
     * @return 更新结果
     */
    private fun updateParentContainerSize(): Result<Unit> {
        //获取父视图的宽高
        return try {
            parentWidth = (parent as ViewGroup).width.toFloat()
            parentHeight = (parent as ViewGroup).height.toFloat()
            Result.success(Unit)
        } catch (e: Exception) {
            FloatViewLog.e(TAG, "获取父视图宽高失败", e)
            Result.failure(e)
        }
    }

    /**
     * 判断是否是点击事件
     * 
     * @return true 表示是点击事件
     */
    private fun isOnClickEvent(): Boolean {
        val tapTimeout = ViewConfiguration.getTapTimeout().toLong()
        val elapsedTime = System.currentTimeMillis() - lastTouchDownTime
        return elapsedTime < tapTimeout
    }

    /**
     * 切换到贴边视图
     */
    internal fun switchToEdgeView() {
        updateParentContainerSize()

        if (edgeView == null) {
            FloatViewLog.d(TAG, "未设置贴边视图，切换到内容视图")
            switchToContentView()
            return
        }
        edgeView?.let { edge ->
            // 计算贴边视图应该显示的宽度
            val edgeViewScaleWidth = (edge.width * edgeViewScale).toInt()
            //是否是左边
            val isNearLeftEdge = this.x < parentWidth / 2
            // 计算最终的x坐标
            val moveDistance = if (isNearLeftEdge) {
                // 左边：移动到负坐标，显示 edgeViewScaleWidth 宽度
                -(edge.width - edgeViewScaleWidth).toFloat()
            } else {
                // 右边：移动到屏幕右侧
                (parentWidth - edgeViewScaleWidth).toFloat()
            }

            move(moveDistance, y)
            // 隐藏内容视图和移动时视图
            edge.visibility = View.VISIBLE
            contentView?.visibility = View.INVISIBLE
            moveView?.visibility = View.INVISIBLE
        }


    }


    /**
     * 切换到内容视图
     */
    internal fun switchToContentView() {
        updateParentContainerSize()
        FloatViewLog.d(TAG, "切换到内容视图")
        //计算移动的距离
        val moveDistance: Float
        if (this.x < parentWidth / 2) {
            moveDistance = marginEdge.toFloat()
        } else {
            moveDistance = parentWidth - width - marginEdge
        }

        move(moveDistance, y)

        edgeView?.visibility = View.INVISIBLE
        contentView?.visibility = View.VISIBLE
        moveView?.visibility = View.INVISIBLE
    }

    /**
     * 移动视图到目标位置
     * 
     * @param destX 目标X坐标
     * @param destY 目标Y坐标
     */
    private fun move(destX: Float, destY: Float) {
        lastX = destX
        lastY = destY
        //从当前位置移动到目标位置，带有动画效果
        this.animate().setDuration(ANIMATION_DURATION_MS).x(destX).y(destY).start()
    }

}
