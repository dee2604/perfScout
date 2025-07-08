package com.deelib.perfScout.model

data class CrashInfo(
    val timestamp: Long,
    val threadName: String,
    val exceptionMessage: String?,
    val stackTrace: String
) 