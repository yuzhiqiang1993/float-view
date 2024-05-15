package com.yzq.floatview_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.yzq.application.AppContext
import com.yzq.binding.viewbind
import com.yzq.floating_view.FloatingViewManager
import com.yzq.floatview_app.databinding.ActivitySecondBinding
import com.yzq.floatview_app.databinding.LayoutFloatViewBinding
import com.yzq.logger.Logger

/**
 * 第二页面
 * 
 * 演示悬浮视图的高级用法：
 * - 手动显示/隐藏
 * - 手动切换贴边视图和内容视图
 * - 自定义三种视图（内容视图、移动视图、贴边视图）
 * 
 * @author yuzhiqiang
 */
class SecondActivity : AppCompatActivity() {

    private val binding by viewbind(ActivitySecondBinding::inflate)

    companion object {
        private const val TAG = "SecondActivity"

        /**
         * 启动 SecondActivity
         * 
         * @param activity 启动 Activity
         */
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SecondActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate")

        // 设置跳转按钮点击事件
        binding.tvSecond.setOnClickListener {
            Logger.d(TAG, "跳转到 ThirdActivity")
            ThirdActivity.start(this)
        }

        // 创建悬浮视图布局
        val floatViewBinding =
            LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))
        val animationUrl = "https://assets7.lottiefiles.com/packages/lf20_5lTxAupekw.json"

        // 设置 Lottie 动画
        floatViewBinding.contentView.setAnimationFromUrl(animationUrl)
        floatViewBinding.contentView.setPadding(0)

        floatViewBinding.moveView.setAnimationFromUrl("https://lottie.host/24b37317-aea5-4ad3-bd37-6f5fc7433d20/zl5ji5Zws0.lottie")
        floatViewBinding.moveView.setPadding(0)

        floatViewBinding.edgeView.setAnimationFromUrl(animationUrl)
        floatViewBinding.edgeView.setPadding(0)

        // 创建悬浮视图管理器（不全局显示）
        val floatingViewManager = FloatingViewManager.Builder(this).floatingViewConfig {
            contentView(floatViewBinding.contentView)
            moveView(floatViewBinding.moveView)
            edgeView(floatViewBinding.edgeView)
            edgeViewScale(0.8f)
            autoShowEdgeView(true)
        }.globalShow(false).build()

        // 显示悬浮视图
        binding.btnShowFloatView.setOnClickListener {
            Logger.d(TAG, "显示悬浮视图")
            floatingViewManager.show()
        }

        // 隐藏悬浮视图
        binding.btnHideFloatView.setOnClickListener {
            Logger.d(TAG, "隐藏悬浮视图")
            floatingViewManager.hide()
        }

        // 切换贴边视图和内容视图
        var isEdge = false
        binding.btnSwitch.setOnClickListener {
            if (isEdge) {
                Logger.d(TAG, "切换到内容视图")
                floatingViewManager.switchToContentView()
                isEdge = false
            } else {
                Logger.d(TAG, "切换到贴边视图")
                floatingViewManager.switchToEdgeView()
                isEdge = true
            }
        }

    }

}
