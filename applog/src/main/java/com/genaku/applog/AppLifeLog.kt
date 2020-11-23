package com.genaku.applog

import android.app.Application

/**
 * Логирование системных событий приложения
 *
 * @param app [Application] - объект класса Application приложения
 * @param log - лямбда-функция, которая осуществляет запись сообщений в лог
 * @param needLifecycleLog [Boolean] - необходимо ли подключить логирование событий жизненного цикла активити и фрагментов в них
 * @param activityListType [ListType] - как показывать список активити
 * @param fragmentListType [ListType] - как показывать список фрагментов
 * @param needMemoryTrimLog [Boolean] - необходимо ли подключить логирование событий очистки памяти
 *
 * Пример:
 *
 * class App: Application() {
 *
 * private lateinit var appLifeLog: AppLifeLog
 *
 * override fun onCreate() {
 *   appLifeLog = AppLifeLog(
 *     app = this,
 *     log = { message -> Log.d("TAG", message) },
 *     needLifecycleLog = true,
 *     activityListType = ListType.STACK,
 *     fragmentListType = ListType.COUNT,
 *     needMemoryTrimLog = true
 *   )
 * }
 */
class AppLifeLog(
    app: Application,
    log: (String) -> Unit,
    needLifecycleLog: Boolean = true,
    activityListType: ListType = ListType.COUNT,
    fragmentListType: ListType = ListType.COUNT,
    needMemoryTrimLog: Boolean = true
) {

    private val loggers = mutableSetOf<Any>()

    init {
        if (needLifecycleLog) {
            loggers.add(ActivityLifecycleEventsLog(app, activityListType, fragmentListType, log))
        }
        if (needMemoryTrimLog) {
            loggers.add(MemoryTrimLog(app, log))
        }
    }
}