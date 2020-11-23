package com.genaku.applog

import android.content.ComponentCallbacks2
import android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND
import android.content.ComponentCallbacks2.TRIM_MEMORY_COMPLETE
import android.content.ComponentCallbacks2.TRIM_MEMORY_MODERATE
import android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL
import android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW
import android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE
import android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
import android.content.Context
import android.content.res.Configuration

/**
 * Логирование событий очистки памяти на устройстве
 *
 * @param context [Context] - контекст приложения
 * @param log - лямбда-функция, которая осуществляет запись сообщений в лог
 */
class MemoryTrimLog(context: Context, private val log: (String) -> Unit) : ComponentCallbacks2 {

    init {
        context.registerComponentCallbacks(this)
    }

    override fun onTrimMemory(level: Int) {
        val message = when (level) {
            TRIM_MEMORY_COMPLETE -> "complete (80)"
            TRIM_MEMORY_MODERATE -> "moderate (60)"
            TRIM_MEMORY_BACKGROUND -> "background (40)"
            TRIM_MEMORY_UI_HIDDEN -> "UI hidden (20)"
            TRIM_MEMORY_RUNNING_CRITICAL -> "running critical (15)"
            TRIM_MEMORY_RUNNING_LOW -> "running low (10)"
            TRIM_MEMORY_RUNNING_MODERATE -> "running moderate (5)"
            else -> "unknown ($level)"
        }
        log("Trim memory, level: $message")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        log("Configuration changed: $newConfig")
    }

    override fun onLowMemory() {
        // do nothing
    }
}
