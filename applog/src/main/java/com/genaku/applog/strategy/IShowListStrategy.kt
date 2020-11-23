package com.genaku.applog.strategy

import androidx.annotation.RestrictTo
import com.genaku.applog.strategy.names.NameId

@RestrictTo(RestrictTo.Scope.LIBRARY)
interface IShowListStrategy {
    fun addNameId(nameId: NameId, state: State)
    fun removeNameId(nameId: NameId, state: State)
    fun getString(): String
}
