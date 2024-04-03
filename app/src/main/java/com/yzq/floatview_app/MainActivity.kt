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


    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnSkip.setOnClickListener {
            SecondActivity.start(this)
        }

        val floatViewBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))

        floatViewBinding.contentView.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_5lTxAupekw.json")
        floatViewBinding.edgeView.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_5lTxAupekw.json")

        floatViewBinding.contentView.setPadding(0)
        floatViewBinding.edgeView.setPadding(0)



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
//            marginEdge(30)
        }.globalShow(true).addBlackList(SecondActivity::class.java).build()
            .setFloatingViewListener(object : FloatingViewListener {
                override fun onClick() {
                    Logger.it(TAG, "floatview onClick")
                }

            }).show()


    }


}