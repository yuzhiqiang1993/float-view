package com.yzq.floatview_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yzq.binding.viewbind
import com.yzq.floatview_app.databinding.ActivityMainBinding
import com.yzq.logger.Logger

/**
 * 主页面
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

        initListeners()
    }

    private fun initListeners() {
        // 跳转局部悬浮窗演示
        binding.btnOpenSecond.setOnClickListener {
            SecondActivity.start(this)
        }

        // 跳转黑名单页面演示
        binding.btnOpenBlacklist.setOnClickListener {
            BlackListActivity.start(this)
        }
    }

}

