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

        val floatViewBinding =
            LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))
        val url = "https://assets7.lottiefiles.com/packages/lf20_5lTxAupekw.json"

        floatViewBinding.contentView.setAnimationFromUrl(url)

        floatViewBinding.contentView.setPadding(0)

        floatViewBinding.moveView.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_5lTxAupekw.json")


        floatViewBinding.moveView.setPadding(0)

        floatViewBinding.edgeView.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_5lTxAupekw.json")

        floatViewBinding.edgeView.setPadding(0)

        val floatingViewManager = FloatingViewManager.Builder(this).floatingViewConfig {
            contentView(floatViewBinding.contentView)
            moveView(floatViewBinding.moveView)
            edgeView(floatViewBinding.edgeView)
            edgeViewScale(0.8f)
            autoShowEdgeView(true)
        }.globalShow(false).build()


        binding.btnShowFloatView.setOnClickListener {
            floatingViewManager.show()
        }


        binding.btnHideFloatView.setOnClickListener {
            floatingViewManager.hide()
        }

        var isEdge = false
        binding.btnSwitch.setOnClickListener {
            isEdge = if (isEdge) {
                floatingViewManager.switchToContentView()
                false
            } else {
                floatingViewManager.switchToEdgeView()
                true
            }

        }

    }


}