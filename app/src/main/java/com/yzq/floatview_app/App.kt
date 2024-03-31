package com.yzq.floatview_app

import android.app.Application
import com.yzq.application.AppManager
import com.yzq.logger.Logger
import com.yzq.logger.console.ConsoleLogPrinter

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppManager.init(this, true)
        Logger.addPrinter(ConsoleLogPrinter.getInstance())


    }
}