package com.genaku.applog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.genaku.applog.strategy.*
import com.genaku.applog.strategy.names.NameId

/**
 * Логирование событий жизненного цикла фрагментов
 *
 * @param log - лямбда-функция, которая осуществляет запись сообщений в лог
 */
class FragmentLifecycleEventsLog(
    fragmentListType: ListType,
    private val log: (String) -> Unit
) : FragmentManager.FragmentLifecycleCallbacks(),
    FragmentManager.OnBackStackChangedListener {

    private val showListStrategy = when (fragmentListType) {
        ListType.NOTHING -> EmptyStrategy()
        ListType.COUNT -> CountingStrategy(log)
        ListType.STACK -> StackStrategy(log)
    }

    private enum class ActionType {
        PRE_ATTACHED,
        ATTACHED,
        PRE_CREATED,
        CREATED,
        ACTIVITY_CREATED,
        VIEW_CREATED,
        STARTED,
        RESUMED,
        PAUSED,
        STOPPED,
        SAVING_STATE,
        VIEW_DESTROYED,
        DESTROYED,
        DETACHED
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        putToLog(f, ActionType.PRE_ATTACHED)
        super.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        putToLog(f, ActionType.ATTACHED)
        super.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        putToLog(f, ActionType.PRE_CREATED)
        super.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        putToLog(f, ActionType.CREATED)
        super.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        putToLog(f, ActionType.ACTIVITY_CREATED)
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        putToLog(f, ActionType.VIEW_CREATED)
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        putToLog(f, ActionType.STARTED)
        super.onFragmentStarted(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        putToLog(f, ActionType.RESUMED)
        super.onFragmentResumed(fm, f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        putToLog(f, ActionType.PAUSED)
        super.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        putToLog(f, ActionType.STOPPED)
        super.onFragmentStopped(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        putToLog(f, ActionType.SAVING_STATE)
        super.onFragmentSaveInstanceState(fm, f, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        putToLog(f, ActionType.VIEW_DESTROYED)
        super.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        putToLog(f, ActionType.DESTROYED)
        super.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        putToLog(f, ActionType.DETACHED)
        super.onFragmentDetached(fm, f)
    }

    override fun onBackStackChanged() {
        log("Back stack changed")
    }

    private fun putToLog(fragment: Fragment, action: ActionType) {
        val stringBuilder = getFragmentStringBuilder(fragment, action)
        stringBuilder.append(showListStrategy.getString())
        log(stringBuilder.toString())
    }

    private fun getFragmentStringBuilder(
        fragment: Fragment,
        actionType: ActionType
    ): StringBuilder = StringBuilder("Fragment: ")
        .append(fragment.javaClass.name)
        .append("#")
        .append(fragment.hashCode())
        .append(": ")
        .append(getFragmentActionString(fragment, actionType))

    private fun getFragmentActionString(fragment: Fragment, actionType: ActionType): String =
        when (actionType) {
            ActionType.PRE_ATTACHED -> {
                "pre attached"
            }
            ActionType.ATTACHED -> {
                "attached"
            }
            ActionType.PRE_CREATED -> {
                "pre created"
            }
            ActionType.CREATED -> {
                showListStrategy.addNameId(fragment.nameId, State.EXISTING)
                "created"
            }
            ActionType.ACTIVITY_CREATED -> {
                "activity created"
            }
            ActionType.VIEW_CREATED -> {
                "view created"
            }
            ActionType.STARTED -> {
                showListStrategy.addNameId(fragment.nameId, State.RUNNING)
                "started"
            }
            ActionType.RESUMED -> {
                showListStrategy.addNameId(fragment.nameId, State.FOREGROUND)
                "resumed"
            }
            ActionType.PAUSED -> {
                showListStrategy.removeNameId(fragment.nameId, State.FOREGROUND)
                "paused"
            }
            ActionType.STOPPED -> {
                showListStrategy.removeNameId(fragment.nameId, State.RUNNING)
                "stopped"
            }
            ActionType.SAVING_STATE -> {
                "saving state"
            }
            ActionType.VIEW_DESTROYED -> {
                "view destroyed"
            }
            ActionType.DESTROYED -> {
                showListStrategy.removeNameId(fragment.nameId, State.EXISTING)
                "destroyed"
            }
            ActionType.DETACHED -> {
                "detached"
            }
        }


    private val Fragment.nameId
        get() = NameId(javaClass.name, javaClass.name + "#" + hashCode())
}