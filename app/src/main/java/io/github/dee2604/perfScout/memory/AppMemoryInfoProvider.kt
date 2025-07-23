package io.github.dee2604.perfScout.memory

import android.os.Debug

internal object AppMemoryInfoProvider {
    internal fun getAppMemoryInfo(): AppMemoryInfo {
        val heapUsed = Debug.getNativeHeapAllocatedSize()
        val nativeUsed = Debug.getNativeHeapSize() - Debug.getNativeHeapFreeSize()
        val totalUsed = heapUsed + nativeUsed
        return AppMemoryInfo(
            heapUsedBytes = heapUsed,
            nativeUsedBytes = nativeUsed,
            totalUsedBytes = totalUsed
        )
    }
} 