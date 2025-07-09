package com.deelib.perfScout.ram

import android.app.ActivityManager
import android.content.Context

internal object RamInfoProvider {
    internal fun getRamInfo(context: Context): RamInfo {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val totalBytes = memoryInfo.totalMem
        val availableBytes = memoryInfo.availMem
        val usedBytes = totalBytes - availableBytes
        return RamInfo(
            totalBytes = totalBytes,
            availableBytes = availableBytes,
            usedBytes = usedBytes
        )
    }
} 