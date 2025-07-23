package io.github.dee2604.perfScout.thread

data class ThreadProcessInfo(
    val threadCount: Int,
    val processId: Int,
    val processName: String,
    val memoryUsageBytes: Long
) 