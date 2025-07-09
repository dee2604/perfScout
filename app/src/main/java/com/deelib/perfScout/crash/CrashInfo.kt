package com.deelib.perfScout.crash

data class CrashInfo(
    val timestamp: Long,
    val threadName: String,
    val exceptionMessage: String?,
    val stackTrace: String
) 