package com.yzq.floating_view

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.yzq.application.AppManager
import com.yzq.application.DefaultActivityLifecycleCallbacks
import com.yzq.logger.Logger


/**
 * @description: 悬浮视图管理实现类
 * @author : yuzhiqiang
 */

class FloatingViewManager private constructor(private val startPage: Activity) :
    IFloatingViewManager, DefaultActivityLifecycleCallbacks {


    //悬浮视图
    private var floatingView: FloatingViewContainer? = null

    //悬浮视图监听器
    private var mFloatingViewListener: FloatingViewListener? = null

    //是否自动移动到边缘
    private var autoMoveToEdge = true

    //是否可拖动
    private var draggable = true

    //layoutid
    private var layoutId: Int? = null

    private var contentView: View? = null

    //是否全局显示
    private var isGlobal = false

    //黑名单(不显示悬浮窗的页面)
    private val blackList = mutableListOf<Class<*>>()


    //默认的布局参数
    private var defaultLayoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
        Gravity.BOTTOM or Gravity.END
    )


    companion object {
        private const val TAG = "FloatingViewManager"

        fun newInstance(activity: Activity): FloatingViewManager {
            return FloatingViewManager(activity)
        }
    }

    fun layout(@LayoutRes layout: Int): FloatingViewManager {
        this.layoutId = layout
        return this
    }

    fun customView(view: View): FloatingViewManager {
        this.contentView = view
        return this
    }


    fun layoutParams(layoutParams: FrameLayout.LayoutParams): FloatingViewManager {
        this.defaultLayoutParams = layoutParams
        return this
    }

    fun autoMoveToEdge(autoMoveToEdge: Boolean): FloatingViewManager {
        this.autoMoveToEdge = autoMoveToEdge
        return this
    }

    fun draggable(dragEnable: Boolean): FloatingViewManager {
        this.draggable = dragEnable
        return this
    }


    override fun show() {
        Logger.it(TAG, "show")
        if (isGlobal) {
            AppManager.addActivityLifecycleCallbacks(this)
        }

        attach(startPage)
    }


    override fun attach(activity: Activity) {

        Logger.it(TAG, "attach", "activity:$activity")

        //在黑名单中的页面不显示悬浮窗
        if (blackList.contains(activity::class.java)) {
            Logger.it(TAG, "attach", "blackList contains")
            return
        }


        //判断悬浮是否是否已经创建过
        if (floatingView == null) {
            floatingView = FloatingViewContainer(activity).apply {
                this.layoutParams(defaultLayoutParams)
                this.autoMoveToEdge(autoMoveToEdge)
                this.draggable(draggable)
                layoutId?.let {
                    setCustomView(it)
                }
                contentView?.let {
                    setCustomView(it)
                }
                mFloatingViewListener?.let {
                    Logger.it(TAG, "attach", "setFloatingViewListener")
                    setFloatingViewListener(it)
                }
            }
        }

        //先移除
        detach()

        activity.rootContainer()?.addView(floatingView)

    }

    override fun detach() {
        floatingView?.let {
            //如果添加过，则移除
            if (it.parent != null) {
                Logger.it(TAG, "detach", "removeView")
                (it.parent as ViewGroup).removeView(it)
            }
        }
    }


    override fun setFloatingViewListener(listener: FloatingViewListener): FloatingViewManager {
        this.mFloatingViewListener = listener
        return this
    }


    override fun onActivityResumed(activity: Activity) {
        attach(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        detach()
    }


    /**
     * 是否全局显示，即在所有页面显示
     * @param isGlobal Boolean
     * @return FloatingViewManager
     */
    fun isGlobal(isGlobal: Boolean): FloatingViewManager {
        this.isGlobal = isGlobal
        return this
    }

    /**
     * 添加黑名单,不显示悬浮窗的页面
     * @param clazz Array<out Class<*>>
     * @return FloatingViewManager
     */
    fun addBlackList(vararg clazz: Class<*>): FloatingViewManager {
        blackList.addAll(clazz)
        return this
    }

}