package io.github.dee2604.perfScout.storage

data class StorageUsageInfo(
    val totalBytes: Long,
    val usedBytes: Long,
    val freeBytes: Long
) 