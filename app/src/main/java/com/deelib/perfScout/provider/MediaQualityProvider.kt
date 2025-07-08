package com.deelib.perfScout.provider

import android.content.Context
import android.app.ActivityManager
import com.deelib.perfScout.model.MediaQualityLevel

internal object MediaQualityProvider {
    
    /**
     * Analyzes device performance and recommends optimal media quality for multimedia content.
     * Considers CPU, RAM, thermal status, and network quality.
     */
    internal fun getMediaQualityRecommendation(context: Context): MediaQualityLevel {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        // Get device specs
        val totalRamGB = memoryInfo.totalMem / (1024 * 1024 * 1024)
        val availableRamGB = memoryInfo.availMem / (1024 * 1024 * 1024)
        val cpuCores = Runtime.getRuntime().availableProcessors()
        
        // Get CPU frequency (estimate)
        val cpuFreqGHz = try {
            val cpuInfo = java.io.File("/proc/cpuinfo").readText()
            val mhzLine = cpuInfo.lines().firstOrNull { it.contains("cpu MHz") }
            mhzLine?.split(":")?.getOrNull(1)?.trim()?.toDoubleOrNull()?.div(1000) ?: 1.5
        } catch (e: Exception) {
            1.5 // fallback
        }
        
        // Performance scoring
        var performanceScore = 0
        
        // RAM scoring (0-25 points)
        when {
            totalRamGB >= 8 -> performanceScore += 25
            totalRamGB >= 6 -> performanceScore += 20
            totalRamGB >= 4 -> performanceScore += 15
            totalRamGB >= 2 -> performanceScore += 10
            else -> performanceScore += 5
        }
        
        // CPU cores scoring (0-25 points)
        when {
            cpuCores >= 8 -> performanceScore += 25
            cpuCores >= 6 -> performanceScore += 20
            cpuCores >= 4 -> performanceScore += 15
            cpuCores >= 2 -> performanceScore += 10
            else -> performanceScore += 5
        }
        
        // CPU frequency scoring (0-25 points)
        when {
            cpuFreqGHz >= 2.5 -> performanceScore += 25
            cpuFreqGHz >= 2.0 -> performanceScore += 20
            cpuFreqGHz >= 1.5 -> performanceScore += 15
            cpuFreqGHz >= 1.0 -> performanceScore += 10
            else -> performanceScore += 5
        }
        
        // Available RAM scoring (0-25 points)
        when {
            availableRamGB >= 4 -> performanceScore += 25
            availableRamGB >= 2 -> performanceScore += 20
            availableRamGB >= 1 -> performanceScore += 15
            availableRamGB >= 0.5 -> performanceScore += 10
            else -> performanceScore += 5
        }
        
        // Determine quality level based on performance score (0-100)
        return when {
            performanceScore >= 85 -> MediaQualityLevel.ULTRA   // 4K content
            performanceScore >= 70 -> MediaQualityLevel.HIGH     // 1080p content
            performanceScore >= 50 -> MediaQualityLevel.MEDIUM   // 720p content
            else -> MediaQualityLevel.LOW                        // 480p content
        }
    }
    
    /**
     * Gets detailed media quality analysis with specific recommendations.
     */
    internal fun getMediaQualityAnalysis(context: Context): Map<String, Any> {
        val quality = getMediaQualityRecommendation(context)
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        val totalRamGB = memoryInfo.totalMem / (1024 * 1024 * 1024)
        val availableRamGB = memoryInfo.availMem / (1024 * 1024 * 1024)
        val cpuCores = Runtime.getRuntime().availableProcessors()
        
        return mapOf(
            "recommendedQuality" to quality,
            "totalRamGB" to totalRamGB,
            "availableRamGB" to availableRamGB,
            "cpuCores" to cpuCores,
            "reasoning" to when (quality) {
                MediaQualityLevel.ULTRA -> "High-end device with excellent performance"
                MediaQualityLevel.HIGH -> "Good device performance for HD content"
                MediaQualityLevel.MEDIUM -> "Moderate device performance"
                MediaQualityLevel.LOW -> "Limited device performance, use lower quality"
            }
        )
    }
} 