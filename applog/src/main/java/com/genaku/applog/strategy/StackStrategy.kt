package com.genaku.applog.strategy

import androidx.annotation.RestrictTo
import com.genaku.applog.strategy.names.NameId
import com.genaku.applog.strategy.names.NamesCount
import com.genaku.applog.strategy.names.NamesList

//@RestrictTo(RestrictTo.Scope.LIBRARY)
class StackStrategy(log: (String) -> Unit) : BaseShowListStrategy(::NamesList, log) {
    override fun getName(nameId: NameId): String = nameId.id
}