package io.github.dee2604.perfScout.startup

data class StartupTimeInfo(
    val coldStartMs: Long?,
    val warmStartMs: Long?,
    val timeToFirstDrawMs: Long?
) 