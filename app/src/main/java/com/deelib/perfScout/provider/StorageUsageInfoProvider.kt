package com.deelib.perfScout.provider

import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.deelib.perfScout.model.StorageUsageInfo

internal object StorageUsageInfoProvider {
    internal fun getStorageUsageInfo(context: Context): StorageUsageInfo {
        return try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            val availableBlocks = stat.availableBlocksLong
            val total = blockSize * totalBlocks
            val free = blockSize * availableBlocks
            val used = total - free
            StorageUsageInfo(
                totalBytes = total,
                usedBytes = used,
                freeBytes = free
            )
        } catch (e: Exception) {
            // Fallback values if storage info cannot be retrieved
            StorageUsageInfo(
                totalBytes = 0L,
                usedBytes = 0L,
                freeBytes = 0L
            )
        }
    }
} 