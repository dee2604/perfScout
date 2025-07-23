package io.github.dee2604.perfScout.media

import android.content.Context
import android.os.Build
import io.github.dee2604.perfScout.api.PerfResult
import io.github.dee2604.perfScout.network.NetworkQualityInfoProvider

data class MediaQualityAnalysis(
    val networkSpeed: String,
    val latency: Long,
    val packetLoss: Double,
    val apiLevel: Int,
    val recommendedQuality: MediaQualityLevel
)

object MediaQualityProvider {
    fun getMediaQualityRecommendation(context: Context): PerfResult<MediaQualityLevel> {
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

    fun getMediaQualityAnalysis(context: Context): PerfResult<MediaQualityAnalysis> {
        return try {
            val networkInfo = NetworkQualityInfoProvider.getNetworkQualityInfo(context)
            val apiLevel = Build.VERSION.SDK_INT
            val recommendedQuality = when {
                apiLevel >= 30 && networkInfo.networkSpeed == "VERY_FAST" -> MediaQualityLevel.ULTRA
                apiLevel >= 28 && networkInfo.networkSpeed == "FAST" -> MediaQualityLevel.HIGH
                apiLevel >= 26 && networkInfo.networkSpeed == "MEDIUM" -> MediaQualityLevel.MEDIUM
                else -> MediaQualityLevel.LOW
            }

            val analysis = MediaQualityAnalysis(
                networkSpeed = networkInfo.networkSpeed ?: "",
                latency = networkInfo.latency?.toLong() ?: 0L,
                packetLoss = networkInfo.packetLoss ?: 0.0,
                apiLevel = apiLevel,
                recommendedQuality = recommendedQuality
            )
            PerfResult.Success(analysis)
        } catch (e: Exception) {
            PerfResult.Error("Failed to analyze media quality: ${e.message}", e)
        }
    }
} 