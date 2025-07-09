package com.deelib.perfScout.storage

data class StorageUsageInfo(
    val totalBytes: Long,
    val usedBytes: Long,
    val freeBytes: Long
) 