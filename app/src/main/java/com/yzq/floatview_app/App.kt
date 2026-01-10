package com.yzq.floatview_app

import android.app.Application
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.view.setPadding
import com.yzq.application.AppManager
import com.yzq.floating_view.FloatViewLog
import com.yzq.floating_view.FloatingViewListener
import com.yzq.floating_view.FloatingViewManager
import com.yzq.floatview_app.databinding.LayoutFloatViewBinding
import com.yzq.logger.Logger
import com.yzq.logger.console.ConsoleLogPrinter

/**
 * 应用程序入口
 * @author yuzhiqiang
 */
class App : Application() {

    companion object {
        private const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()
        Logger.d(TAG, "应用启动")

        // 初始化 AppManager（启用调试模式）
        AppManager.init(this, true)

        // 初始化日志组件
        Logger.addPrinter(ConsoleLogPrinter.getInstance())


        // 初始化全局悬浮窗
        initGlobalFloatingView()

        Logger.d(TAG, "初始化完成")
    }

    /**
     * 初始化全局悬浮窗
     *
     * 演示在 Application 中初始化悬浮窗的用法
     */
    private fun initGlobalFloatingView() {

        // 开启悬浮窗日志（调试用）
        FloatViewLog.isLoggable = BuildConfig.DEBUG
        val floatViewBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(this))

        // 设置 Lottie 动画
        floatViewBinding.contentView.setAnimation("Developer.lottie")
        floatViewBinding.contentView.setPadding(0)
        floatViewBinding.edgeView.setAnimation("Developer.lottie")
        floatViewBinding.edgeView.setPadding(0)

        // 构建全局悬浮窗
        FloatingViewManager.Builder(this)
            .tag("GLOBAL_FLOAT")
            .floatingViewConfig {
                contentView(floatViewBinding.contentView)
                edgeView(floatViewBinding.edgeView)
                draggable(true)
                autoMoveToEdge(true)
                autoShowEdgeView(true)
                edgeViewScale(0.7f)
                globalShow(true) // 全局显示
                addBlackList(BlackListActivity::class.java) // 黑名单页面不显示
            }
            .build()
            .setFloatingViewListener(object : FloatingViewListener {
                override fun onClick() {
                    Logger.d(TAG, "全局悬浮窗被点击，跳转到黑名单页面")
                    // 使用 Application Context 启动 Activity 需要添加 FLAG_ACTIVITY_NEW_TASK
                    val intent = Intent(this@App, BlackListActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                }
            })
            .show()

        Logger.d(TAG, "全局悬浮窗已初始化")
    }
}
