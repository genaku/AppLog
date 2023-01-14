package com.genaku.applog.strategy

import androidx.annotation.RestrictTo
import com.genaku.applog.strategy.names.INames
import com.genaku.applog.strategy.names.NameId

//@RestrictTo(RestrictTo.Scope.LIBRARY)
abstract class BaseShowListStrategy(
    createNames: () -> INames,
    private val log: (String) -> Unit
) : IShowListStrategy {

    private val createdActivities: INames = createNames()
    private val runningActivities: INames = createNames()
    private val foregroundActivities: INames = createNames()

    abstract fun getName(nameId: NameId): String

    override fun addNameId(nameId: NameId, state: State) {
        state.names.add(getName(nameId))
    }

    override fun removeNameId(nameId: NameId, state: State) {
        try {
            state.names.remove(getName(nameId))
        } catch (e: NoSuchElementException) {
            log("Experiencing problems with ${nameId.id} which was ${state.exited} while was not ${state.entered}")
        }
    }

    override fun getString(): String = StringBuilder()
        .append(createdActivities.toString("Exists"))
        .append(runningActivities.toString("Running"))
        .append(foregroundActivities.toString("Foreground"))
        .toString()

    private val State.names: INames
        get() = when (this) {
            State.EXISTING -> createdActivities
            State.RUNNING -> runningActivities
            State.FOREGROUND -> foregroundActivities
        }

    private val State.exited: String
        get() = when (this) {
            State.EXISTING -> "destroyed"
            State.RUNNING -> "stopped"
            State.FOREGROUND -> "paused"
        }

    private val State.entered: String
        get() = when (this) {
            State.EXISTING -> "created"
            State.RUNNING -> "started"
            State.FOREGROUND -> "resumed"
        }
}