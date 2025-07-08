/**
 * PerfScout: Android Performance & Usage Metrics Library
 *
 * PerfScout provides easy, permissionless access to vital app and device performance metrics with a single method call per feature.
 *
 * Features:
 * - CPU, RAM, GPU, Battery, Network, Storage, Thread/Process, Frame Rendering/Jank, Startup Time, Device Info, App Memory, App CPU, GC, App Uptime, Thermal Info, Network Quality
 *
 * Usage Example:
 * ```kotlin
 * val cpuInfo = PerfScout.getCpuInfo()
 * val ramInfo = PerfScout.getRamInfo(context)
 * val batteryInfo = PerfScout.getBatteryInfo(context)
 * val deviceInfo = PerfScout.getDeviceInfo(context)
 * val appMemory = PerfScout.getAppMemoryInfo()
 * val appUptime = PerfScout.getAppUptimeInfo(context)
 * ```
 *
 * For startup time tracking:
 * ```kotlin
 * // In MainActivity.onCreate()
 * PerfScout.recordActivityOnCreate(this)
 * 
 * // After first draw
 * PerfScout.recordFirstDraw(this)
 * ```
 *
 * Most features require no special permissions.
 *
 * See README.md for full details and sample app.
 */
package com.deelib.perfScout

import android.content.Context
import com.deelib.perfScout.model.*
import com.deelib.perfScout.provider.*


class PerfScout {
    companion object {
        fun getCpuInfo(): CpuInfo = CpuInfoProvider.getCpuInfo()
        fun getRamInfo(context: Context): RamInfo = RamInfoProvider.getRamInfo(context)
        fun getGpuInfo(): GpuInfo = GpuInfoProvider.getGpuInfo()
        fun getDeviceInfo(context: Context): DeviceInfo = DeviceInfoProvider.getDeviceInfo(context)
        
        fun getBatteryInfo(context: Context): BatteryInfo = BatteryInfoProvider.getBatteryInfo(context)
        fun getNetworkUsageInfo(context: Context): NetworkUsageInfo = NetworkUsageInfoProvider.getNetworkUsageInfo(context)
        fun getStorageUsageInfo(context: Context): StorageUsageInfo = StorageUsageInfoProvider.getStorageUsageInfo(context)
        fun getThreadProcessInfo(context: Context): ThreadProcessInfo = ThreadProcessInfoProvider.getThreadProcessInfo(context)
        fun getFrameRenderingInfo(durationMillis: Long = 2000, callback: (FrameRenderingInfo) -> Unit) = FrameRenderingInfoProvider.getFrameRenderingInfo(durationMillis, callback)
        fun recordActivityOnCreate(context: Context) = StartupTimeInfoProvider.recordActivityOnCreate(context)
        fun recordFirstDraw(context: Context) = StartupTimeInfoProvider.recordFirstDraw(context)
        fun getStartupTimeInfo(): StartupTimeInfo = StartupTimeInfoProvider.getStartupTimeInfo()
        
    
    
        fun getAppMemoryInfo(): AppMemoryInfo = AppMemoryInfoProvider.getAppMemoryInfo()
        fun getAppCpuUsageInfo(callback: (AppCpuUsageInfo) -> Unit, intervalMs: Long = 500) = AppCpuUsageInfoProvider.getAppCpuUsageInfo(callback, intervalMs)
        fun getGcStatsInfo(): GcStatsInfo = GcStatsInfoProvider.getGcStatsInfo()

        fun getAppUptimeInfo(context: Context): AppUptimeInfo = AppUptimeInfoProvider.getAppUptimeInfo(context)
        fun getStartupTimeInfoForPackage(packageName: String): StartupTimeInfo? = StartupTimeInfoProvider.getStartupTimeInfoForPackage(packageName)
        
        fun getThermalInfo(context: Context): ThermalInfo = ThermalInfoProvider.getThermalInfo(context)
        fun getNetworkQualityInfo(context: Context): NetworkQualityInfo = NetworkQualityInfoProvider.getNetworkQualityInfo(context)
        fun getMediaQualityRecommendation(context: Context): MediaQualityLevel = MediaQualityProvider.getMediaQualityRecommendation(context)
        fun getMediaQualityAnalysis(context: Context): Map<String, Any> = MediaQualityProvider.getMediaQualityAnalysis(context)

        fun monitorMemoryLeaks(
            context: Context,
            intervalMs: Long = 5000,
            window: Int = 5,
            thresholdMb: Int = 20,
            callback: (isLeaking: Boolean, usageHistory: List<Long>) -> Unit
        ) = MemoryLeakMonitorProvider.monitorMemoryLeaks(context, intervalMs, window, thresholdMb, callback)

        fun setAnrListener(listener: ((AnrInfo) -> Unit)?) =
            AnrMonitorProvider.setListener(listener)
        fun setCrashListener(listener: ((CrashInfo) -> Unit)?) =
            CrashMonitorProvider.setListener(listener)
        fun setJankListener(listener: ((JankInfo) -> Unit)?) =
            JankMonitorProvider.setListener(listener)

        fun enableStrictMode(
            detectAll: Boolean = true,
            penaltyLog: Boolean = true,
            penaltyCallback: ((StrictModeViolationInfo) -> Unit)? = null
        ) = StrictModeMonitorProvider.enable(detectAll, penaltyLog, penaltyCallback)
    }
} 