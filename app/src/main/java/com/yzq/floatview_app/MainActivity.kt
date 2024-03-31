package com.yzq.floatview_app

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var floatingViewBinding: LayoutFloatViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.textView.setOnClickListener {
            SecondActivity.start(this)
        }



        floatingViewBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(AppContext))


        floatingViewBinding.ivFloatBig.setOnClickListener {
            Logger.i("ivFloatBig click")
        }


        FloatingViewManager.newInstance(this).customView(floatingViewBinding.root)
            .setFloatingViewListener(object : FloatingViewListener {
                override fun onClick() {
                    Logger.it(TAG, "FloatingViewListener Click")
                }
            })
            .autoMoveToEdge(true).draggable(true).isGlobal(true)
            .addBlackList(SecondActivity::class.java).show()


    }

}