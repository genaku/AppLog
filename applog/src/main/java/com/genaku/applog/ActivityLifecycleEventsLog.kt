package com.genaku.applog

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.genaku.applog.strategy.*
import com.genaku.applog.strategy.names.NameId

/**
 * Логирование событий жизненного цикла активити и фрагментов в них
 *
 * @param app [Application] - объект класса Application приложения
 * @param activityListType [ListType] - тип отображения в логе списка активити
 * @param log - лямбда-функция, которая осуществляет запись сообщений в лог
 */
class ActivityLifecycleEventsLog(
    app: Application,
    activityListType: ListType,
    private val fragmentListType: ListType,
    private val log: (String) -> Unit
) : ActivityLifecycleCallbacks {

    init {
        app.registerActivityLifecycleCallbacks(this)
    }

    private val fragmentLogs = mutableMapOf<String, FragmentLifecycleEventsLog>()

    private enum class ActionType {
        CREATED,
        STARTED,
        RESUMED,
        PAUSED,
        STOPPED,
        DESTROYED,
        SAVING_STATE
    }

    private val showListStrategy = when (activityListType) {
        ListType.NOTHING -> EmptyStrategy()
        ListType.COUNT -> CountingStrategy(log)
        ListType.STACK -> StackStrategy(log)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        putToLog(ActionType.CREATED, activity)
        addFragmentsLog(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        putToLog(ActionType.DESTROYED, activity)
        removeFragmentsLog(activity)
    }

    override fun onActivityStarted(activity: Activity) =
        putToLog(ActionType.STARTED, activity)

    override fun onActivityResumed(activity: Activity) =
        putToLog(ActionType.RESUMED, activity)

    override fun onActivityPaused(activity: Activity) =
        putToLog(ActionType.PAUSED, activity)

    override fun onActivityStopped(activity: Activity) =
        putToLog(ActionType.STOPPED, activity)

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) =
        putToLog(ActionType.SAVING_STATE, activity)

    private fun addFragmentsLog(activity: Activity) {
        if (activity is FragmentActivity) {
            val fragmentLog = FragmentLifecycleEventsLog(fragmentListType, log)
            fragmentLogs[activity.nameId.id] = fragmentLog
            with(activity.supportFragmentManager) {
                addOnBackStackChangedListener(fragmentLog)
                registerFragmentLifecycleCallbacks(fragmentLog, true)
            }
        }
    }

    private fun removeFragmentsLog(activity: Activity) {
        val fragmentLog = fragmentLogs[activity.nameId.id] ?: return
        if (activity is FragmentActivity) {
            with(activity.supportFragmentManager) {
                removeOnBackStackChangedListener(fragmentLog)
                unregisterFragmentLifecycleCallbacks(fragmentLog)
            }
        }
        fragmentLogs.remove(activity.nameId.id)
    }

    private fun putToLog(actionType: ActionType, activity: Activity) {
        val stringBuilder = getActivityStringBuilder(activity, actionType)
        stringBuilder.append(showListStrategy.getString())
        log(stringBuilder.toString())
    }

    private fun getActivityStringBuilder(activity: Activity, actionType: ActionType) =
        StringBuilder(getActivityActionString(activity, actionType)).append(": ")
            .append(activity.nameId.id)

    private fun getActivityActionString(activity: Activity, actionType: ActionType): String =
        when (actionType) {
            ActionType.CREATED -> {
                showListStrategy.addNameId(activity.nameId, State.EXISTING)
                "Activity created"
            }
            ActionType.DESTROYED -> {
                showListStrategy.removeNameId(activity.nameId, State.EXISTING)
                "Activity destroyed"
            }
            ActionType.PAUSED -> {
                showListStrategy.removeNameId(activity.nameId, State.FOREGROUND)
                "Activity paused"
            }
            ActionType.RESUMED -> {
                showListStrategy.addNameId(activity.nameId, State.FOREGROUND)
                "Activity resumed"
            }
            ActionType.STARTED -> {
                showListStrategy.addNameId(activity.nameId, State.RUNNING)
                "Activity started"
            }
            ActionType.STOPPED -> {
                showListStrategy.removeNameId(activity.nameId, State.RUNNING)
                "Activity stopped"
            }
            ActionType.SAVING_STATE -> {
                "Save activity instance state"
            }
        }

    private val Activity.nameId
        get() = NameId(javaClass.name, javaClass.name + "#" + hashCode())
}
