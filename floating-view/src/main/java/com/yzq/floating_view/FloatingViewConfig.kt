package com.yzq.floating_view


import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.GravityInt
import androidx.annotation.LayoutRes

/**
 * 悬浮视图配置类
 *
 * 用于配置悬浮视图的各种属性，包括视图、布局参数、行为等
 *
 * @author yuzhiqiang
 */
class FloatingViewConfig {

    companion object {
        private const val TAG = "FloatingViewConfig"
    }

    private var defaultLayoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
        Gravity.BOTTOM or Gravity.END
    ) //默认的布局参数

    private var contentView: View? = null//内容视图
    private var contentViewResId: Int? = null

    private var moveView: View? = null//移动时的视图，用于移动时显示的view，可选，不设置则默认显示内容视图
    private var moveViewResId: Int? = null

    private var edgeView: View? = null//贴边时的视图，用于贴边时显示的视图，可选，不设置则默认显示内容视图
    private var edgeViewResId: Int? = null

    private var edgeViewScale = 1.0f//贴边视图的显示比例,默认是1.0f,只对edgeView有效

    private var draggable = true//是否可拖拽
    private var autoMoveToEdge = true//是否自动贴边
    private var autoShowEdgeView = true//贴边后是否自动显示贴边view
    private var marginEdge = 0//内容视图贴边是距离屏幕边缘的距离
    internal var globalShow = false // 是否全局显示
    internal val blackList = mutableListOf<ActivityFilterRule>() // 黑名单规则列表
    internal val whiteList = mutableListOf<ActivityFilterRule>() // 白名单规则列表（优先级高于黑名单）

    /**
     * 设置内容视图
     *
     * @param view 内容视图
     * @return FloatingViewConfig
     */
    fun contentView(view: View) = apply {
        this.contentView = view
        this.contentViewResId = null
    }

    // ... (contentView method overload, no change)

    /**
     * 设置是否全局显示
     *
     * @param globalShow 是否全局显示
     * @return FloatingViewConfig
     */
    fun globalShow(globalShow: Boolean) = apply { this.globalShow = globalShow }

    /**
     * 添加黑名单规则（仅在全局显示模式下有效）
     *
     * @param rules Activity 过滤规则列表
     * @return FloatingViewConfig
     */
    fun addBlackList(vararg rules: ActivityFilterRule) = apply { blackList.addAll(rules) }

    /**
     * 添加黑名单 Activity 类型（仅在全局显示模式下有效）
     *
     * 这是为了保持 API 向后兼容的便捷方法
     *
     * @param clazz Activity 类列表
     * @return FloatingViewConfig
     */
    fun addBlackList(vararg clazz: Class<*>) = apply {
        blackList.addAll(clazz.map { ActivityFilterRule.ClassRule(it) })
    }

    /**
     * 添加黑名单包名（仅在全局显示模式下有效）
     *
     * 匹配指定包名下的所有 Activity
     *
     * 示例：
     * ```
     * addBlackListPackages("com.example.feature", "com.example.debug")
     * ```
     *
     * @param packageNames 包名列表
     * @return FloatingViewConfig
     */
    fun addBlackListPackages(vararg packageNames: String) = apply {
        blackList.addAll(packageNames.map { ActivityFilterRule.PackageRule(it) })
    }

    /**
     * 添加黑名单类名（仅在全局显示模式下有效）
     *
     * 支持简单类名或全限定类名
     *
     * 示例：
     * ```
     * addBlackListClassNames("MainActivity", "com.example.DebugActivity")
     * ```
     *
     * @param classNames 类名列表（简单名或全限定名）
     * @return FloatingViewConfig
     */
    fun addBlackListClassNames(vararg classNames: String) = apply {
        blackList.addAll(classNames.map { ActivityFilterRule.ClassNameRule(it) })
    }

    /**
     * 添加白名单规则（仅在全局显示模式下有效）
     *
     * 白名单优先级高于黑名单，只有在白名单中的 Activity 才会显示悬浮窗
     *
     * @param rules Activity 过滤规则列表
     * @return FloatingViewConfig
     */
    fun addWhiteList(vararg rules: ActivityFilterRule) = apply { whiteList.addAll(rules) }

    /**
     * 添加白名单 Activity 类型（仅在全局显示模式下有效）
     *
     * 只有在白名单中的 Activity 才会显示悬浮窗
     *
     * @param clazz Activity 类列表
     * @return FloatingViewConfig
     */
    fun addWhiteList(vararg clazz: Class<*>) = apply {
        whiteList.addAll(clazz.map { ActivityFilterRule.ClassRule(it) })
    }

    /**
     * 添加白名单包名（仅在全局显示模式下有效）
     *
     * 只有指定包名下的 Activity 才会显示悬浮窗
     *
     * 示例：
     * ```
     * addWhiteListPackages("com.example.main", "com.example.home")
     * ```
     *
     * @param packageNames 包名列表
     * @return FloatingViewConfig
     */
    fun addWhiteListPackages(vararg packageNames: String) = apply {
        whiteList.addAll(packageNames.map { ActivityFilterRule.PackageRule(it) })
    }

    /**
     * 添加白名单类名（仅在全局显示模式下有效）
     *
     * 只有指定类名的 Activity 才会显示悬浮窗
     *
     * 示例：
     * ```
     * addWhiteListClassNames("MainActivity", "com.example.HomeActivity")
     * ```
     *
     * @param classNames 类名列表（简单名或全限定名）
     * @return FloatingViewConfig
     */
    fun addWhiteListClassNames(vararg classNames: String) = apply {
        whiteList.addAll(classNames.map { ActivityFilterRule.ClassNameRule(it) })
    }

    /**
     * 设置内容视图（通过布局资源 ID）
     *
     * @param layoutResId 布局资源 ID
     * @return FloatingViewConfig
     */
    fun contentView(@LayoutRes layoutResId: Int) = apply {
        this.contentViewResId = layoutResId
        this.contentView = null
    }

    /**
     * 设置移动时显示的视图
     *
     * @param view 移动视图
     * @return FloatingViewConfig
     */
    fun moveView(view: View) = apply {
        this.moveView = view
        this.moveViewResId = null
    }

    /**
     * 设置移动时显示的视图（通过布局资源 ID）
     *
     * @param layoutResId 布局资源 ID
     * @return FloatingViewConfig
     */
    fun moveView(@LayoutRes layoutResId: Int) = apply {
        this.moveViewResId = layoutResId
        this.moveView = null
    }

    /**
     * 设置贴边时显示的视图
     *
     * @param view 贴边视图
     * @return FloatingViewConfig
     */
    fun edgeView(view: View) = apply {
        this.edgeView = view
        this.edgeViewResId = null
    }

    /**
     * 设置贴边时显示的视图（通过布局资源 ID）
     *
     * @param layoutResId 布局资源 ID
     * @return FloatingViewConfig
     */
    fun edgeView(@LayoutRes layoutResId: Int) = apply {
        this.edgeViewResId = layoutResId
        this.edgeView = null
    }

    /**
     * 设置是否可拖拽
     *
     * @param draggable 是否可拖拽
     * @return FloatingViewConfig
     */
    fun draggable(draggable: Boolean) = apply { this.draggable = draggable }

    /**
     * 设置贴边后是否自动显示贴边视图
     *
     * @param autoShowEdgeView 是否自动显示贴边视图
     * @return FloatingViewConfig
     */
    fun autoShowEdgeView(autoShowEdgeView: Boolean) =
        apply { this.autoShowEdgeView = autoShowEdgeView }

    /**
     * 设置贴边视图的显示比例
     *
     * @param scale 显示比例（0.0-1.0）
     * @return FloatingViewConfig
     */
    fun edgeViewScale(scale: Float) = apply { this.edgeViewScale = scale }


    /**
     * 设置内容视图贴边时距离屏幕边缘的距离
     *
     * @param marginEdge 边距（像素）
     * @return FloatingViewConfig
     */
    fun marginEdge(marginEdge: Int) = apply { this.marginEdge = marginEdge }

    /**
     * 设置是否自动贴边
     *
     * @param autoMoveToEdge 是否自动贴边
     * @return FloatingViewConfig
     */
    fun autoMoveToEdge(autoMoveToEdge: Boolean) = apply { this.autoMoveToEdge = autoMoveToEdge }

    /**
     * 设置宽度
     *
     * @param width 宽度（像素）
     * @return FloatingViewConfig
     */
    fun width(width: Int) = apply { defaultLayoutParams.width = width }

    /**
     * 设置高度
     *
     * @param height 高度（像素）
     * @return FloatingViewConfig
     */
    fun height(height: Int) = apply { defaultLayoutParams.height = height }

    /**
     * 设置底部边距
     *
     * @param bottomMargin 底部边距（像素）
     * @return FloatingViewConfig
     */
    fun bottomMargin(bottomMargin: Int) = apply { defaultLayoutParams.bottomMargin = bottomMargin }

    /**
     * 设置顶部边距
     *
     * @param topMargin 顶部边距（像素）
     * @return FloatingViewConfig
     */
    fun topMargin(topMargin: Int) = apply { defaultLayoutParams.topMargin = topMargin }

    /**
     * 设置左边距
     *
     * @param leftMargin 左边距（像素）
     * @return FloatingViewConfig
     */
    fun leftMargin(leftMargin: Int) = apply { defaultLayoutParams.leftMargin = leftMargin }

    /**
     * 设置右边距
     *
     * @param rightMargin 右边距（像素）
     * @return FloatingViewConfig
     */
    fun rightMargin(rightMargin: Int) = apply { defaultLayoutParams.rightMargin = rightMargin }

    /**
     * 设置重力
     *
     * @param gravity 重力值（如 Gravity.BOTTOM | Gravity.END）
     * @return FloatingViewConfig
     */
    fun gravity(@GravityInt gravity: Int) = apply { defaultLayoutParams.gravity = gravity }

    /**
     * 配置悬浮视图容器
     *
     * @param floatingViewContainer 悬浮视图容器
     */
    internal fun config(floatingViewContainer: FloatingViewContainer) {
        // 验证必需参数
        if (contentView == null && contentViewResId == null) {
            val exception = IllegalStateException("必须设置 contentView")
            FloatViewLog.e(TAG, "配置验证失败", exception)
            throw exception
        }

        FloatViewLog.d(TAG, "开始配置悬浮视图容器")
        floatingViewContainer.layoutParams = defaultLayoutParams


        if (contentView != null) {
            floatingViewContainer.contentView(contentView!!)
        } else if (contentViewResId != null) {
            floatingViewContainer.contentView(contentViewResId!!)
        }

        if (moveView != null) {
            floatingViewContainer.moveView(moveView!!)
        } else if (moveViewResId != null) {
            floatingViewContainer.moveView(moveViewResId!!)
        }

        if (edgeView != null) {
            floatingViewContainer.edgeView(edgeView!!)
        } else if (edgeViewResId != null) {
            floatingViewContainer.edgeView(edgeViewResId!!)
        }

        floatingViewContainer.draggable(draggable)
        floatingViewContainer.autoShowEdgeView(autoShowEdgeView)
        floatingViewContainer.edgeViewScale(edgeViewScale)
        floatingViewContainer.marginEdge(marginEdge)
        floatingViewContainer.autoMoveToEdge(autoMoveToEdge)

        FloatViewLog.d(TAG, "悬浮视图容器配置完成")
    }
}
