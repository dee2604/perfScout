package com.deelib.perfScout.model

data class AppMemoryInfo(
    val heapUsedBytes: Long,
    val nativeUsedBytes: Long?,
    val totalUsedBytes: Long
) 