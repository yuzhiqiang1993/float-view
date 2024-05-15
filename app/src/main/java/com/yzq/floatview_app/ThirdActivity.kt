package com.yzq.floatview_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yzq.binding.viewbind
import com.yzq.floatview_app.databinding.ActivityThirdBinding
import com.yzq.logger.Logger

/**
 * 第三页面
 * 
 * 演示从 Java 代码中使用悬浮视图
 * 
 * @author yuzhiqiang
 */
class ThirdActivity : AppCompatActivity() {

    private val binding by viewbind(ActivityThirdBinding::inflate)

    companion object {
        private const val TAG = "ThirdActivity"

        /**
         * 启动 ThirdActivity
         * 
         * @param activity 启动 Activity
         */
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ThirdActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate")

        // 设置跳转按钮点击事件
        binding.button.setOnClickListener {
            Logger.d(TAG, "跳转到 JavaActivity")
            JavaActivity.start(this)
        }
    }
}
