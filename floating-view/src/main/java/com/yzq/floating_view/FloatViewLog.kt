package com.yzq.floating_view

import android.util.Log

/**
 * 悬浮视图日志工具类
 *
 * 提供统一的日志管理，支持开关控制
 *
 * @author yuzhiqiang
 */
object FloatViewLog {

    /**
     * 是否启用日志（默认关闭，建议在 Debug 模式下开启）
     */
    @Volatile
    var isLoggable: Boolean = false

    internal fun d(tag: String, message: String) {
        if (isLoggable) {
            Log.d(tag, message)
        }
    }

    internal fun w(tag: String, message: String) {
        if (isLoggable) {
            Log.w(tag, message)
        }
    }

    internal fun e(tag: String, message: String, throwable: Throwable? = null) {
        // Error 日志始终打印
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
}
