package com.genaku.applog.strategy

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
enum class State {
    EXISTING,
    RUNNING,
    FOREGROUND
}