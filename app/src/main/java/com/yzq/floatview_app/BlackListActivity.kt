package com.yzq.floatview_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * 黑名单页面（在此页面不显示全局悬浮窗）
 */
class BlackListActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BlackListActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            text = "此页面在黑名单中\n全局悬浮窗应自动隐藏"
            textSize = 24f
            gravity = Gravity.CENTER
        }
        setContentView(textView)
    }
}
