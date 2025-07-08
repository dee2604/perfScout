package com.deelib.perfScout.model

data class ThreadProcessInfo(
    val threadCount: Int,
    val processId: Int,
    val processName: String?,
    val memoryUsageBytes: Long?
) 