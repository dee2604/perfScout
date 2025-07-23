package io.github.dee2604.perfScout.memory

data class AppMemoryInfo(
    val heapUsedBytes: Long,
    val nativeUsedBytes: Long,
    val totalUsedBytes: Long
) 