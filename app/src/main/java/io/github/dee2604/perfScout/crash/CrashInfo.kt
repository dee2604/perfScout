package io.github.dee2604.perfScout.crash

data class CrashInfo(
    val timestamp: Long,
    val threadName: String,
    val exceptionMessage: String?,
    val stackTrace: String
) 