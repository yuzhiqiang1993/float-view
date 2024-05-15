package com.yzq.floating_view

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import com.yzq.application.AppManager
import com.yzq.application.DefaultActivityLifecycleCallbacks


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


    private var currentActivity: Activity? = null

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

        //在黑名单中的页面不显示悬浮窗
        if (blackList.contains(activity::class.java)) {
            return
        }

        //如果当前页面已经是悬浮窗所在页面，则不再添加
        if (currentActivity == activity && floatingViewContainer.parent != null) {
            return
        }

        floatingViewContainer.alpha = 0f
        activity.rootContainer()?.addView(floatingViewContainer)
        floatingViewContainer.animate().alpha(1f).setDuration(200).start()
        currentActivity = activity

    }


    private fun detach() {
        //如果添加过，则移除
        if (floatingViewContainer.parent != null) {
            (floatingViewContainer.parent as ViewGroup).removeView(floatingViewContainer)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        attach(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        attach(activity)
    }


    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        detach()
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity == startPage) {
            AppManager.removeActivityLifecycleCallbacks(this)
        }
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