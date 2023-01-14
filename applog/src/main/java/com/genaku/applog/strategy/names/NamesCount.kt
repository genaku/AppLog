package com.genaku.applog.strategy.names

import androidx.annotation.RestrictTo

//@RestrictTo(RestrictTo.Scope.LIBRARY)
class NamesCount : INames {

    private val names: MutableMap<String, Int> = mutableMapOf()

    override fun add(name: String) {
        val count = names[name] ?: 0
        names[name] = count + 1
    }

    override fun remove(name: String) {
        val count = names[name]
        when {
            count == null -> throw NoSuchElementException()
            count > 1 -> names[name] = count - 1
            else -> names.remove(name)
        }
    }

    override fun toString(title: String): String {
        val stringBuilder = StringBuilder()
        if (names.isNotEmpty()) {
            stringBuilder.append("\n$title:")
            for ((key, value) in names) {
                stringBuilder.append('\n').append(key).append(": ").append(value)
            }
        }
        return stringBuilder.toString()
    }
}