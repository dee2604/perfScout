package com.deelib.perfScout.startup

data class StartupTimeInfo(
    val coldStartMs: Long?,
    val warmStartMs: Long?,
    val timeToFirstDrawMs: Long?
) 