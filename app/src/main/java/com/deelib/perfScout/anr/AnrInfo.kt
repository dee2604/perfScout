package com.deelib.perfScout.anr

data class AnrInfo(
    val timestamp: Long,
    val durationMs: Long,
    val mainThreadStack: String
) 