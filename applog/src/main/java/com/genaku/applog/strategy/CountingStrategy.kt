package com.genaku.applog.strategy

import androidx.annotation.RestrictTo
import com.genaku.applog.strategy.names.NameId
import com.genaku.applog.strategy.names.NamesCount

@RestrictTo(RestrictTo.Scope.LIBRARY)
class CountingStrategy(log: (String) -> Unit) : BaseShowListStrategy(::NamesCount, log) {
    override fun getName(nameId: NameId): String = nameId.name
}