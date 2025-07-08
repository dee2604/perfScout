package com.deelib.perfScout.provider

import android.app.ActivityManager
import android.content.Context
import com.deelib.perfScout.model.RamInfo

internal object RamInfoProvider {
    internal fun getRamInfo(context: Context): RamInfo {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            
            val totalBytes = memoryInfo.totalMem
            val availableBytes = memoryInfo.availMem
            val usedBytes = totalBytes - availableBytes
            
            RamInfo(
                totalBytes = totalBytes,
                availableBytes = availableBytes,
                usedBytes = usedBytes
            )
        } catch (e: Exception) {
            // Fallback values if RAM info cannot be retrieved
            RamInfo(
                totalBytes = 0L,
                availableBytes = 0L,
                usedBytes = 0L
            )
        }
    }
} 