package com.deelib.perfScout.provider

import android.content.Context
import android.os.Handler
import android.os.Looper

internal object MemoryLeakMonitorProvider {
    internal fun monitorMemoryLeaks(
        context: Context,
        intervalMs: Long = 5000,
        window: Int = 5,
        thresholdMb: Int = 20,
        callback: (isLeaking: Boolean, usageHistory: List<Long>) -> Unit
    ) {
        val usageHistory = mutableListOf<Long>()
        val handler = Handler(Looper.getMainLooper())
        fun sample() {
            val memInfo = AppMemoryInfoProvider.getAppMemoryInfo()
            val totalMb = ((memInfo.heapUsedBytes ?: 0L) + (memInfo.nativeUsedBytes ?: 0L)) / (1024 * 1024)
            usageHistory.add(totalMb)
            if (usageHistory.size > window) usageHistory.removeAt(0)
            val isLeaking = usageHistory.size == window &&
                (usageHistory.last() - usageHistory.first() >= thresholdMb)
            callback(isLeaking, usageHistory.toList())
            handler.postDelayed({ sample() }, intervalMs)
        }
        sample()
    }
} 