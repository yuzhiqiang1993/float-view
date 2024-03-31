package com.yzq.floating_view

import android.app.Activity


/**
 * @description: 悬浮视图管理器接口
 * @author : yuzhiqiang
 */

interface IFloatingViewManager {
    //绑定悬浮视图
    fun attach(activity: Activity)

    //解绑悬浮视图
    fun detach()

    //显示悬浮视图
    fun show()


    //设置悬浮视图监听器
    fun setFloatingViewListener(listener: FloatingViewListener): FloatingViewManager


}