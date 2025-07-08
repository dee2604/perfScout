package com.deelib.perfScout.provider

import android.os.Debug
import com.deelib.perfScout.model.AppMemoryInfo

internal object AppMemoryInfoProvider {
    internal fun getAppMemoryInfo(): AppMemoryInfo {
        val runtime = Runtime.getRuntime()
        val heapUsed = runtime.totalMemory() - runtime.freeMemory()
        val memInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(memInfo)
        val nativeUsed = memInfo.nativePss * 1024L // in bytes
        val totalUsed = memInfo.totalPss * 1024L // in bytes
        return AppMemoryInfo(
            heapUsedBytes = heapUsed,
            nativeUsedBytes = nativeUsed,
            totalUsedBytes = totalUsed
        )
    }
} 