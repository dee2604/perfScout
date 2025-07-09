package com.deelib.perfScout.thread

data class ThreadProcessInfo(
    val threadCount: Int,
    val processId: Int,
    val processName: String,
    val memoryUsageBytes: Long
) 