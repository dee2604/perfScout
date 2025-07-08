package com.deelib.perfScout.provider

import android.os.Debug
import com.deelib.perfScout.model.GcStatsInfo

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