package com.genaku.applog.strategy

import androidx.annotation.RestrictTo
import com.genaku.applog.strategy.names.NameId

@RestrictTo(RestrictTo.Scope.LIBRARY)
class EmptyStrategy : IShowListStrategy {

    override fun addNameId(nameId: NameId, state: State) {
        // do nothing
    }

    override fun removeNameId(nameId: NameId, state: State) {
        // do nothing
    }

    override fun getString(): String = ""
}