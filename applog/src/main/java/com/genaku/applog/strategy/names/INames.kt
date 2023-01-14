package com.genaku.applog.strategy.names

import androidx.annotation.RestrictTo

//@RestrictTo(RestrictTo.Scope.LIBRARY)
interface INames {
    fun add(name: String)
    fun remove(name: String)
    fun toString(title: String): String
}