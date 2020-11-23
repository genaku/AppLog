package com.genaku.applog.strategy.names

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
class NamesList : INames {

    private val names: MutableList<String> = mutableListOf()

    override fun add(name: String) {
        names.add(name)
    }

    override fun remove(name: String) {
        if (names.contains(name)) {
            names.remove(name)
        } else {
            throw NoSuchElementException()
        }
    }

    override fun toString(title: String): String {
        val stringBuilder = StringBuilder()
        if (names.isNotEmpty()) {
            stringBuilder.append("\n$title:")
            names.forEach {
                stringBuilder.append('\n').append(it)
            }
        }
        return stringBuilder.toString()
    }
}