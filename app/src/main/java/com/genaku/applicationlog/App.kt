package com.genaku.applicationlog

import android.app.Application
import android.util.Log
import com.genaku.applog.ListType
import com.genaku.applog.AppLifeLog

class App : Application() {

    private lateinit var appLifeLog: AppLifeLog

    override fun onCreate() {
        super.onCreate()
        setupLog()
    }

    private fun setupLog() {
            appLifeLog = AppLifeLog(
                app = this,
                log = { message -> Log.d("TAG", message) },
                needLifecycleLog = true,
                activityListType = ListType.STACK,
                needMemoryTrimLog = true
            )
    }
}
