package io.github.dee2604.perfScout.anr

data class AnrInfo(
    val timestamp: Long,
    val durationMs: Long,
    val mainThreadStack: String
) 