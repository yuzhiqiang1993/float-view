package com.yzq.floatview_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.yzq.binding.viewbind
import com.yzq.floating_view.FloatingViewManager
import com.yzq.floatview_app.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private val binding by viewbind(ActivitySecondBinding::inflate)


    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SecondActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvSecond.setOnClickListener {
            ThirdActivity.start(this)
        }

        FloatingViewManager
            .newInstance(this)
            .layout(R.layout.layout_float_view1)
            .layoutParams(
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = (Gravity.BOTTOM or Gravity.END)
                    setMargins(0, topMargin, rightMargin, 500)
                }
            )
            .isGlobal(false)
            .show()

    }


}