package com.yzq.floating_view

import android.app.Activity
import android.view.ViewGroup
import com.yzq.application.AppManager
import com.yzq.application.DefaultActivityLifecycleCallbacks
import com.yzq.logger.Logger


/**
 * @description: 悬浮视图管理实现类
 * @author : yuzhiqiang
 */

class FloatingViewManager private constructor(
    private val startPage: Activity,
    private val globalShow: Boolean = false,
    private val blackList: MutableList<Class<*>> = mutableListOf(),
    private val floatingViewConfig: FloatingViewConfig,
) : DefaultActivityLifecycleCallbacks {

    private val floatingViewContainer: FloatingViewContainer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val container = FloatingViewContainer()
        floatingViewConfig.config(container)
        container
    }


    companion object {
        private const val TAG = "FloatingViewManager"
    }

    fun show() {

        if (globalShow) {
            AppManager.addActivityLifecycleCallbacks(this)
        }
        attach(startPage)
    }


    fun hide() {
        detach()
    }


    private fun attach(activity: Activity) {

        Logger.it(TAG, "attach", "activity:$activity")
        //在黑名单中的页面不显示悬浮窗
        if (blackList.contains(activity::class.java)) {
            Logger.it(TAG, "attach", "blackList contains")
            return
        }
        //先移除
        detach()
        activity.rootContainer()?.addView(floatingViewContainer)

    }


    private fun detach() {
        //如果添加过，则移除
        if (floatingViewContainer.parent != null) {
            Logger.it(TAG, "detach", "removeView")
            (floatingViewContainer.parent as ViewGroup).removeView(floatingViewContainer)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        attach(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        detach()
    }


    fun setFloatingViewListener(listener: FloatingViewListener) = apply {
        floatingViewContainer.setFloatingViewListener(listener)
    }

    /**
     * 切换到边缘视图

     */
    fun switchToEdgeView() {
        floatingViewContainer.switchToEdgeView()
    }

    /**
     * 切换到内容视图
     */
    fun switchToContentView() {
        floatingViewContainer.switchToContentView()
    }


    class Builder(private val startPage: Activity) {
        private var floatingViewConfig = FloatingViewConfig()
        private var globalShow = false
        private val blackList = mutableListOf<Class<*>>()

        fun globalShow(globalShow: Boolean) = apply { this.globalShow = globalShow }

        fun addBlackList(vararg clazz: Class<*>) = apply { blackList.addAll(clazz) }


        fun floatingViewConfig(block: FloatingViewConfig.() -> Unit) = apply {
            floatingViewConfig.block()
        }

        fun floatingViewConfig(floatingViewConfig: FloatingViewConfig) = apply {
            this.floatingViewConfig = floatingViewConfig
        }

        fun build(): FloatingViewManager {
            return FloatingViewManager(startPage, globalShow, blackList, floatingViewConfig)
        }
    }

}