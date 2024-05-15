package com.yzq.floatview_app

import android.app.Application
import com.yzq.application.AppManager
import com.yzq.logger.Logger
import com.yzq.logger.console.ConsoleLogPrinter

/**
 * 应用程序入口
 * 
 * 初始化全局组件：
 * - AppManager：应用生命周期管理
 * - Logger：日志组件
 * 
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

        Logger.d(TAG, "初始化完成")
    }
}
