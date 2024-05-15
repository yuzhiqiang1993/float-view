package com.yzq.floatview_app

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.yzq.application.AppContext
import com.yzq.binding.viewbind
import com.yzq.floating_view.FloatingViewListener
import com.yzq.floating_view.FloatingViewManager
import com.yzq.floatview_app.databinding.ActivityMainBinding
import com.yzq.floatview_app.databinding.LayoutFloatViewBinding
import com.yzq.logger.Logger

/**
 * 主页面
 * 
 * 演示悬浮视图的基本用法：
 * - 全局显示
 * - 自动贴边
 * - 点击事件监听
 * 
 * @author yuzhiqiang
 */
class MainActivity : AppCompatActivity() {

    private val binding by viewbind(ActivityMainBinding::inflate)

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate")

        // 设置跳转按钮点击事件
        binding.btnSkip.setOnClickListener {
            Logger.d(TAG, "跳转到 SecondActivity")
            SecondActivity.start(this)
        }

        // 创建悬浮视图布局
        val floatViewBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))

        // 设置 Lottie 动画 URL
        val animationUrl = "https://lottie.host/60672cb1-7f81-426f-97ce-eb8f40fbe33c/zVu7nmZ5zx.lottie"
        floatViewBinding.contentView.setAnimationFromUrl(animationUrl)
        floatViewBinding.edgeView.setAnimationFromUrl(animationUrl)

        // 设置内边距为 0
        floatViewBinding.contentView.setPadding(0)
        floatViewBinding.edgeView.setPadding(0)

        // 创建并显示悬浮视图
        FloatingViewManager.Builder(this).floatingViewConfig {
            contentView(floatViewBinding.contentView)
            edgeView(floatViewBinding.edgeView)
            draggable(true)
            autoMoveToEdge(true)
            autoShowEdgeView(true)
            edgeViewScale(0.7f)
            gravity(Gravity.START or Gravity.BOTTOM)
            bottomMargin(100)
            leftMargin(30)
        }.globalShow(true)
            .build()
            .setFloatingViewListener(object : FloatingViewListener {
                override fun onClick() {
                    Logger.d(TAG, "悬浮视图被点击")
                }
            })
            .show()

        Logger.d(TAG, "悬浮视图已创建并显示")

    }

}
