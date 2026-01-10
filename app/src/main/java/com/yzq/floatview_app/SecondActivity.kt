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
    private var localManager: FloatingViewManager? = null
    private var halfHiddenManager: FloatingViewManager? = null

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


        // 创建悬浮视图布局（默认局部）
        val floatViewBinding =
            LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))
        // 设置 Lottie 动画 (本地 Assets)
        floatViewBinding.contentView.setAnimation("Live chatbot.lottie")
        floatViewBinding.contentView.setPadding(0)
        floatViewBinding.moveView.setAnimation("Wrench.lottie")
        floatViewBinding.moveView.setPadding(0)
        floatViewBinding.edgeView.setAnimation("Live chatbot.lottie")
        floatViewBinding.edgeView.setPadding(0)

        // 创建悬浮视图管理器（不全局显示）
        localManager = FloatingViewManager.Builder(this)
            .tag("LOCAL_DEMO")
            .floatingViewConfig {
                contentView(floatViewBinding.contentView)
                moveView(floatViewBinding.moveView)
                edgeView(floatViewBinding.edgeView)
                edgeViewScale(0.8f)
                autoShowEdgeView(true)
                globalShow(false)
            }.build()

        // 显示悬浮视图
        binding.btnShowFloatView.setOnClickListener {
            Logger.d(TAG, "显示悬浮视图")
            localManager?.show(this@SecondActivity)
        }

        // 隐藏悬浮视图
        binding.btnHideFloatView.setOnClickListener {
            Logger.d(TAG, "隐藏悬浮视图")
            localManager?.hide()
        }

        // 添加半隐藏悬浮窗（演示 Tag 和 负边距）
        binding.btnAddHalfHidden.setOnClickListener {
            if (halfHiddenManager != null) {
                halfHiddenManager?.show(this)
                return@setOnClickListener
            }

            // 创建一个新的 View 用于演示
            val halfFloatBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))
            halfFloatBinding.contentView.setAnimation("Developer.lottie") // 复用开发者的动画
            halfFloatBinding.contentView.setPadding(0)
            
            // 为了演示半隐藏，我们不需要 move/edge view 特别设置，或者复用
            halfFloatBinding.edgeView.setAnimation("Developer.lottie")

            halfHiddenManager = FloatingViewManager.Builder(this)
                .tag("HALF_HIDDEN_DEMO")
                .floatingViewConfig {
                    contentView(halfFloatBinding.contentView)
                    // 演示：贴边时隐藏一半。假设 View 宽约200px，设置 -100
                    marginEdge(-100) 
                    edgeViewScale(1.0f) // 保持原大小
                    // 演示：初始位置在右侧中间
                    gravity(android.view.Gravity.END or android.view.Gravity.CENTER_VERTICAL)
                    rightMargin(0) 
                }
                .build()
            
            halfHiddenManager?.show(this)
        }


        // 切换贴边视图和内容视图（仅针对 Local Manager 演示）
        var isEdge = false
        binding.btnSwitch.setOnClickListener {
            if (isEdge) {
                Logger.d(TAG, "切换到内容视图")
                localManager?.switchToContentView()
                isEdge = false
            } else {
                Logger.d(TAG, "切换到贴边视图")
                localManager?.switchToEdgeView()
                isEdge = true
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // 必须销毁非全局的 Manager，否则会残留在静态 Map 中并持有 Context
        localManager?.destroy()
        halfHiddenManager?.destroy()
    }

}
