package com.yzq.floating_view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.yzq.application.AppManager
import com.yzq.application.DefaultActivityLifecycleCallbacks

/**
 * 悬浮视图管理实现类
 * 
 * 负责管理悬浮视图的生命周期、显示隐藏、黑名单过滤等功能
 * 
 * @author yuzhiqiang
 */
class FloatingViewManager private constructor(
    private val startPage: Activity,
    private val globalShow: Boolean = false,
    private val blackList: MutableList<Class<*>> = mutableListOf(),
    private val floatingViewConfig: FloatingViewConfig,
) : DefaultActivityLifecycleCallbacks {


    companion object {
        private const val TAG = "FloatingViewManager"
        private const val ANIMATION_DURATION_MS = 200L
    }

    @Volatile
    private var currentActivity: Activity? = null

    private val floatingViewContainer: FloatingViewContainer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val container = FloatingViewContainer()
        floatingViewConfig.config(container)
        container
    }


    /**
     * 显示悬浮视图
     */
    fun show() {
        Log.d(TAG, "显示悬浮视图")
        if (globalShow) {
            AppManager.addActivityLifecycleCallbacks(this)
        }
        attach(startPage)
    }


    /**
     * 隐藏悬浮视图
     */
    fun hide() {
        Log.d(TAG, "隐藏悬浮视图")
        detach()
    }


    /**
     * 附加到指定 Activity
     * 
     * @param activity 目标 Activity
     */
    @Synchronized
    private fun attach(activity: Activity) {
        //在黑名单中的页面不显示悬浮窗
        if (blackList.contains(activity::class.java)) {
            Log.d(TAG, "当前 Activity 在黑名单中，不显示悬浮视图")
            return
        }

        //如果当前页面已经是悬浮窗所在页面，则不再添加
        if (currentActivity == activity && floatingViewContainer.parent != null) {
            Log.d(TAG, "当前页面已是悬浮窗所在页面，无需重复添加")
            return
        }

        floatingViewContainer.alpha = 0f
        val rootContainerResult = activity.rootContainer()
        if (rootContainerResult.isFailure) {
            Log.e(TAG, "获取根视图容器失败，无法添加悬浮视图", rootContainerResult.exceptionOrNull())
            return
        }
        
        val rootContainer: ViewGroup = rootContainerResult.getOrThrow()
        rootContainer.addView(floatingViewContainer)
        floatingViewContainer.animate().alpha(1f).setDuration(ANIMATION_DURATION_MS).start()
        currentActivity = activity
        Log.d(TAG, "悬浮视图已附加到 Activity: ${activity::class.java.simpleName}")

    }


    /**
     * 从当前 Activity 分离悬浮视图
     */
    @Synchronized
    private fun detach() {
        //如果添加过，则移除
        if (floatingViewContainer.parent != null) {
            try {
                val parent = floatingViewContainer.parent as ViewGroup
                parent.removeView(floatingViewContainer)
                Log.d(TAG, "悬浮视图已分离")
            } catch (e: Exception) {
                Log.e(TAG, "移除悬浮视图失败", e)
            }
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


    /**
     * 设置悬浮视图监听器
     * 
     * @param listener 悬浮视图监听器
     * @return FloatingViewManager
     */
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


    /**
     * 构建器类
     */
    class Builder(private val startPage: Activity) {
        private var floatingViewConfig = FloatingViewConfig()
        private var globalShow = false
        private val blackList = mutableListOf<Class<*>>()

        /**
         * 设置是否全局显示
         * 
         * @param globalShow 是否全局显示
         * @return Builder
         */
        fun globalShow(globalShow: Boolean) = apply { this.globalShow = globalShow }

        /**
         * 添加黑名单 Activity
         * 
         * @param clazz Activity 类列表
         * @return Builder
         */
        fun addBlackList(vararg clazz: Class<*>) = apply { blackList.addAll(clazz) }


        /**
         * 设置悬浮视图配置（Lambda 方式，推荐 Kotlin 使用）
         * 
         * @param block 配置代码块
         * @return Builder
         */
        fun floatingViewConfig(block: FloatingViewConfig.() -> Unit) = apply {
            floatingViewConfig.block()
        }

        /**
         * 设置悬浮视图配置（对象方式，推荐 Java 使用）
         * 
         * @param floatingViewConfig 配置对象
         * @return Builder
         */
        fun floatingViewConfig(floatingViewConfig: FloatingViewConfig) = apply {
            this.floatingViewConfig = floatingViewConfig
        }

        /**
         * 构建悬浮视图管理器
         * 
         * @return FloatingViewManager
         */
        fun build(): FloatingViewManager {
            return FloatingViewManager(startPage, globalShow, blackList, floatingViewConfig)
        }
    }

}
