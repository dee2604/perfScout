package com.deelib.perfScout.model

data class StartupTimeInfo(
    val coldStartMs: Long?,
    val warmStartMs: Long?,
    val timeToFirstDrawMs: Long?
) 