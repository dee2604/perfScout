package com.deelib.perfScout.memory

data class AppMemoryInfo(
    val heapUsedBytes: Long,
    val nativeUsedBytes: Long,
    val totalUsedBytes: Long
) 