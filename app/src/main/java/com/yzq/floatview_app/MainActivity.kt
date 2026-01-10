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

class MainActivity : AppCompatActivity() {

    private val binding by viewbind(ActivityMainBinding::inflate)
    private lateinit var floatingViewManager: FloatingViewManager

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate")

        initGlobalFloatingView()
        initListeners()
    }

    private fun initListeners() {
        // 全局悬浮窗开关
        binding.switchGlobalFloat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                floatingViewManager.show(this)
            } else {
                floatingViewManager.hide()
            }
        }

        // 跳转局部演示
        binding.btnOpenSecond.setOnClickListener {
            SecondActivity.start(this)
        }

        // 跳转黑名单演示
        binding.btnOpenBlacklist.setOnClickListener {
            BlackListActivity.start(this)
        }
    }

    private fun initGlobalFloatingView() {
        // 创建悬浮视图布局
        val floatViewBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))

        // 设置 Lottie 动画 (本地 Assets)
        floatViewBinding.contentView.setAnimation("Developer.lottie")
        floatViewBinding.edgeView.setAnimation("Developer.lottie")

        // 设置内边距为 0
        floatViewBinding.contentView.setPadding(0)
        floatViewBinding.edgeView.setPadding(0)

        // 创建并显示悬浮视图
        floatingViewManager = FloatingViewManager.Builder(this).floatingViewConfig {
            contentView(floatViewBinding.contentView)
            edgeView(floatViewBinding.edgeView)
            draggable(true)
            autoMoveToEdge(true)
            autoShowEdgeView(true)
            edgeViewScale(0.7f)
            gravity(Gravity.START or Gravity.BOTTOM)
            bottomMargin(100)
            leftMargin(30)
        }
            .globalShow(true)
            .addBlackList(BlackListActivity::class.java)
            .build()
            .setFloatingViewListener(object : FloatingViewListener {
                override fun onClick() {
                    Logger.d(TAG, "悬浮视图被点击")
                }
            })

        // 默认显示
        floatingViewManager.show(this)
        Logger.d(TAG, "全局悬浮视图已初始化")
    }

}
