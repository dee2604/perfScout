package io.github.dee2604.perfScout.gc

import android.os.Debug

object GcStatsInfoProvider {
    fun getGcStatsInfo(): GcStatsInfo {
        val stats = Debug.getRuntimeStats()
        val gcCount = stats["art.gc.gc-count"] ?: stats["art.gc.gc-count-bg"]
        val gcTime = stats["art.gc.gc-time"] ?: stats["art.gc.gc-time-bg"]
        return GcStatsInfo(
            gcCount = gcCount?.toLong(),
            gcTimeMs = gcTime?.toLong()
        )
    }
} 