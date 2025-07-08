package com.deelib.perfScout.model

data class AnrInfo(
    val timestamp: Long,
    val durationMs: Long,
    val mainThreadStack: String
) 