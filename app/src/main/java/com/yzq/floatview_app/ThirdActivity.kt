package com.yzq.floatview_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yzq.binding.viewbind
import com.yzq.floatview_app.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    private val binding by viewbind(ActivityThirdBinding::inflate)

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ThirdActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}