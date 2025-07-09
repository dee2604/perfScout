package com.deelib.perfScout.media

import android.content.Context
import android.os.Build
import com.deelib.perfScout.network.NetworkQualityInfoProvider
import com.deelib.perfScout.core.PerfResult

object MediaQualityProvider {
    fun getMediaQualityRecommendation(context: Context): PerfResult<MediaQualityLevel> {
        // Example heuristic: use network speed and device API level
        return try {
            val networkInfo = NetworkQualityInfoProvider.getNetworkQualityInfo(context)
            val apiLevel = Build.VERSION.SDK_INT
            val quality = when {
                apiLevel >= 30 && networkInfo.networkSpeed == "VERY_FAST" -> MediaQualityLevel.ULTRA
                apiLevel >= 28 && networkInfo.networkSpeed == "FAST" -> MediaQualityLevel.HIGH
                apiLevel >= 26 && networkInfo.networkSpeed == "MEDIUM" -> MediaQualityLevel.MEDIUM
                else -> MediaQualityLevel.LOW
            }
            PerfResult.Success(quality)
        } catch (e: Exception) {
            PerfResult.Error("Failed to recommend media quality: ${e.message}", e)
        }
    }

    fun getMediaQualityAnalysis(context: Context): Map<String, Any> {
        val networkInfo = NetworkQualityInfoProvider.getNetworkQualityInfo(context)
        val apiLevel = Build.VERSION.SDK_INT
        return mapOf(
            "networkSpeed" to (networkInfo.networkSpeed as Any),
            "latency" to (networkInfo.latency as Any),
            "packetLoss" to (networkInfo.packetLoss as Any),
            "apiLevel" to (apiLevel as Any)
        )
    }
} 