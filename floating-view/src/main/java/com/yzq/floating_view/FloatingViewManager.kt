package com.yzq.floating_view

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

import android.view.ViewGroup

/**
 * 悬浮视图管理实现类
 *
 * 负责管理悬浮视图的生命周期、显示隐藏、黑名单过滤等功能
 *
 * @author yuzhiqiang
 */
class FloatingViewManager private constructor(
    private val context: Context,
    private val tag: String,
    private val floatingViewConfig: FloatingViewConfig
) : SimpleActivityLifecycleCallbacks {


    /**
     * 销毁当前悬浮视图管理器
     */
    fun destroy() {
        hide()
        instances.remove(tag)
    }

    companion object {
        private const val TAG = "FloatingViewManager"
        private const val ANIMATION_DURATION_MS = 200L
        private const val DEFAULT_TAG = "default_floating_view"

        private val instances = java.util.concurrent.ConcurrentHashMap<String, FloatingViewManager>()

        /**
         * 根据 Tag 获取悬浮视图管理器实例
         *
         * @param tag 标签
         * @return FloatingViewManager?
         */
        fun get(tag: String): FloatingViewManager? {
            return instances[tag]
        }

        /**
         * 根据 Tag 销毁悬浮视图管理器实例
         *
         * @param tag 标签
         */
        fun destroy(tag: String) {
            instances[tag]?.destroy()
        }
    }

    @Volatile
    private var currentActivity: Activity? = null

    private var isFirstAttach = true // 是否是首次依附

    private val floatingViewContainer: FloatingViewContainer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val container = FloatingViewContainer(context)
        floatingViewConfig.config(container)
        container
    }


    /**
     * 显示悬浮视图
     * @param activity 指定显示的 Activity，如果不传则依赖生命周期回调自动显示
     */
    fun show(activity: Activity? = null) {
        FloatViewLog.d(TAG, "显示悬浮视图")
        if (floatingViewConfig.globalShow) {
            if (context is Application) {
                // 先移除，防止重复添加
                context.unregisterActivityLifecycleCallbacks(this)
                context.registerActivityLifecycleCallbacks(this)
            } else {
                FloatViewLog.w(TAG, "全局显示需要 Context 为 Application 上下文")
            }
        }
        activity?.let { attach(it) }
    }


    /**
     * 隐藏悬浮视图
     */
    fun hide() {
        FloatViewLog.d(TAG, "隐藏悬浮视图")
        detach()
        isFirstAttach = true
        if (floatingViewConfig.globalShow) {
            if (context is Application) {
                context.unregisterActivityLifecycleCallbacks(this)
            }
        }
    }


    /**
     * 附加到指定 Activity
     *
     * @param activity 目标 Activity
     */
    @Synchronized
    private fun attach(activity: Activity) {
        val activityClass = activity::class.java

        // 白名单优先：如果设置了白名单，只有在白名单中的页面才显示
        if (floatingViewConfig.whiteList.isNotEmpty()) {
            val inWhiteList = floatingViewConfig.whiteList.any { it.matches(activityClass) }
            if (!inWhiteList) {
                FloatViewLog.d(
                    TAG,
                    "当前 Activity 不在白名单中，不显示悬浮视图: ${activity::class.java.name}"
                )
                return
            }
        }

        // 黑名单检查：在黑名单中的页面不显示悬浮窗
        if (floatingViewConfig.blackList.isNotEmpty()) {
            val inBlackList = floatingViewConfig.blackList.any { it.matches(activityClass) }
            if (inBlackList) {
                FloatViewLog.d(
                    TAG,
                    "当前 Activity 在黑名单中，不显示悬浮视图: ${activity::class.java.name}"
                )
                return
            }
        }

        //如果当前页面已经是悬浮窗所在页面，则不再添加
        if (currentActivity == activity && floatingViewContainer.parent != null) {
            FloatViewLog.d(TAG, "当前页面已是悬浮窗所在页面，无需重复添加")
            return
        }

        // 确保从旧的父容器移除
        detach()

        val rootContainerResult = activity.rootContainer()
        if (rootContainerResult.isFailure) {
            FloatViewLog.e(TAG, "获取根视图容器失败，无法添加悬浮视图", rootContainerResult.exceptionOrNull())
            return
        }

        val rootContainer: ViewGroup = rootContainerResult.getOrThrow()
        rootContainer.addView(floatingViewContainer)

        // 仅在首次添加时执行动画
        if (isFirstAttach) {
            floatingViewContainer.alpha = 0f
            floatingViewContainer.animate().alpha(1f).setDuration(ANIMATION_DURATION_MS).start()
            isFirstAttach = false
        } else {
            floatingViewContainer.alpha = 1f
        }

        currentActivity = activity
        FloatViewLog.d(TAG, "悬浮视图已附加到 Activity: ${activity::class.java.simpleName}")

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
                FloatViewLog.d(TAG, "悬浮视图已分离")
            } catch (e: Exception) {
                FloatViewLog.e(TAG, "移除悬浮视图失败", e)
            }
        }
        currentActivity = null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // 可以在这里做一些初始化，但 attach 通常在 resumed
    }

    override fun onActivityResumed(activity: Activity) {
        attach(activity)
    }


    override fun onActivityDestroyed(activity: Activity) {
        // 移除销毁 Activity 的引用检查
        if (currentActivity == activity) {
            detach()
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
     * @param context 上下文
     */
    class Builder(context: Context) {
        private val context = context.applicationContext
        private var floatingViewConfig = FloatingViewConfig()
        private var tag = DEFAULT_TAG

        /**
         * 设置悬浮窗标签（用于支持多个悬浮窗，不设置则默认使用同一TAG）
         * @param tag 标签
         */
        fun tag(tag: String) = apply { this.tag = tag }


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
         * 设置悬浮视图配置（对象方式，兼容 Java 使用）
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
            synchronized(instances) {
                if (instances.containsKey(tag)) {
                    instances[tag]?.destroy()
                }
                val manager =FloatingViewManager(context, tag, floatingViewConfig)
                instances[tag] = manager
                return manager
            }
        }
    }

}
