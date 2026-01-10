package com.yzq.floating_view

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/**
 * Activity 扩展函数
 */

/**
 * 获取 Activity 的根视图容器
 * 
 * @return 根视图容器（android.R.id.content），获取失败返回错误结果
 */
internal fun Activity.rootContainer(): Result<ViewGroup> {
    return try {
        val contentView = this.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
        Result.success(contentView)
    } catch (e: Exception) {
        FloatViewLog.e("ActivityExt", "获取根视图容器失败", e)
        Result.failure(e)
    }
}
