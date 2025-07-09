/**
 * PerfScout: Unified Android Performance & Usage Metrics Library
 *
 * PerfScout provides safe, modular, and permissionless access to all vital app and device performance metrics
 * and monitoring features through a single, unified API surface.
 *
 * - All metrics are returned as [PerfResult] (Success/Error) for robust error handling.
 * - Main categories:
 *   - Device metrics: CPU, RAM, GPU, Battery, Thermal, Device Info
 *   - App metrics: App Memory, App Uptime, GC Stats, Startup Time
 *   - System metrics: Storage, Thread/Process, Network Quality
 *   - Advanced features: Frame Rendering/Jank, Media Quality Recommendation
 *   - Monitoring: Crash, ANR, Jank, StrictMode
 *
 * Crash monitoring:
 * - The crash listener will be called for any uncaught exception in the host app, any library, or PerfScout itself.
 * - It does not catch handled exceptions and does not prevent the app from crashing.
 * - It chains to any previous uncaught exception handler (e.g., Crashlytics).
 *
 * Startup time tracking:
 * - Requires explicit calls to [startupTime.recordActivityOnCreate] and [startupTime.recordFirstDraw] in your MainActivity lifecycle.
 *
 * All features are modular and can be used independently.
 */
package com.deelib.perfScout

import android.content.Context
import android.os.Build
import com.deelib.perfScout.anr.AnrInfo
import com.deelib.perfScout.anr.AnrMonitorProvider
import com.deelib.perfScout.battery.BatteryInfoProvider
import com.deelib.perfScout.core.PerfResult
import com.deelib.perfScout.cpu.CpuInfoProvider
import com.deelib.perfScout.crash.CrashInfo
import com.deelib.perfScout.crash.CrashMonitorProvider
import com.deelib.perfScout.device.DeviceInfoProvider
import com.deelib.perfScout.frame.FrameRenderingInfo
import com.deelib.perfScout.frame.FrameRenderingInfoProvider
import com.deelib.perfScout.gc.GcStatsInfoProvider
import com.deelib.perfScout.jank.JankInfo
import com.deelib.perfScout.jank.JankMonitorProvider
import com.deelib.perfScout.memory.AppMemoryInfoProvider
import com.deelib.perfScout.network.NetworkQualityInfoProvider
import com.deelib.perfScout.ram.RamInfoProvider
import com.deelib.perfScout.storage.StorageUsageInfoProvider
import com.deelib.perfScout.strictmode.StrictModeMonitorProvider
import com.deelib.perfScout.strictmode.StrictModeViolationInfo
import com.deelib.perfScout.thermal.ThermalInfoProvider
import com.deelib.perfScout.thread.ThreadProcessInfoProvider
import com.deelib.perfScout.uptime.AppUptimeInfoProvider
import com.deelib.perfScout.gpu.GpuInfoProvider
import com.deelib.perfScout.media.MediaQualityProvider
import com.deelib.perfScout.startup.StartupTimeInfo
import com.deelib.perfScout.startup.StartupTimeInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

inline fun <T> safeCall(errorMessage: String, block: () -> T): PerfResult<T> = try {
    PerfResult.Success(block())
} catch (e: Exception) {
    PerfResult.Error("$errorMessage: ${e.message}", e)
}

interface StartupTimeDelegate {
    fun recordActivityOnCreate(context: Context)
    fun recordFirstDraw(context: Context)
    suspend fun getAsync(context: Context): PerfResult<StartupTimeInfo>
    fun get(context: Context): PerfResult<StartupTimeInfo> =
        kotlinx.coroutines.runBlocking { getAsync(context) }
}


inline fun <T> CoroutineScope.launchWithCallback(
    crossinline block: suspend () -> T,
    crossinline callback: (T) -> Unit
) {
    launch {
        val result = block()
        withContext(Dispatchers.Main) { callback(result) }
    }
}

interface MetricProvider<ContextArg, InfoType> {
    fun fetch(context: ContextArg): InfoType
    fun errorMessage(): String

    fun sync(context: ContextArg): PerfResult<InfoType> =
        safeCall(errorMessage()) { fetch(context) }

    suspend fun async(context: ContextArg): PerfResult<InfoType> =
        withContext(Dispatchers.IO) { sync(context) }

    fun async(
        context: ContextArg,
        callback: (PerfResult<InfoType>) -> Unit,
        scope: CoroutineScope
    ) =
        scope.launchWithCallback({ async(context) }, callback)
}

class ContextMetricDelegate<C, R>(
    private val provider: MetricProvider<C, R>,
    private val scope: CoroutineScope
) {
    fun get(context: C): PerfResult<R> = provider.sync(context)
    suspend fun getAsync(context: C): PerfResult<R> = provider.async(context)
    fun getAsync(context: C, callback: (PerfResult<R>) -> Unit) =
        provider.async(context, callback, scope)
}

class SimpleMetricDelegate<R>(
    private val provider: MetricProvider<Unit, R>,
    private val scope: CoroutineScope
) {
    fun get(): PerfResult<R> = provider.sync(Unit)
    suspend fun getAsync(): PerfResult<R> = provider.async(Unit)
    fun getAsync(callback: (PerfResult<R>) -> Unit) =
        provider.async(Unit, callback, scope)
}

fun <C, R> metricProvider(fetch: (C) -> R, errorMsg: String) = object : MetricProvider<C, R> {
    override fun fetch(context: C): R = fetch(context)
    override fun errorMessage() = errorMsg
}

private val metricScopes = ConcurrentHashMap<String, CoroutineScope>()
private fun scopeFor(name: String): CoroutineScope =
    metricScopes.getOrPut(name) { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

/**
 * PerfScout: Unified Android Performance & Usage Metrics Library
 *
 * PerfScout provides safe, modular, and permissionless access to all vital app and device performance metrics
 * and monitoring features through a single, unified API surface.
 *
 * - All metrics are returned as [PerfResult] (Success/Error) for robust error handling.
 * - Main categories:
 *   - Device metrics: CPU, RAM, GPU, Battery, Thermal, Device Info
 *   - App metrics: App Memory, App Uptime, GC Stats, Startup Time
 *   - System metrics: Storage, Thread/Process, Network Quality
 *   - Advanced features: Frame Rendering/Jank, Media Quality Recommendation
 *   - Monitoring: Crash, ANR, Jank, StrictMode
 *
 * Crash monitoring:
 * - The crash listener will be called for any uncaught exception in the host app, any library, or PerfScout itself.
 * - It does not catch handled exceptions and does not prevent the app from crashing.
 * - It chains to any previous uncaught exception handler (e.g., Crashlytics).
 *
 * Startup time tracking:
 * - Requires explicit calls to [startupTime.recordActivityOnCreate] and [startupTime.recordFirstDraw] in your MainActivity lifecycle.
 *
 * All features are modular and can be used independently.
 */
object PerfScout {
    val cpu = SimpleMetricDelegate(
        metricProvider({ CpuInfoProvider.getCpuInfo() }, "Failed to get CPU info"),
        scopeFor("cpu")
    )

    val appMemory = SimpleMetricDelegate(
        metricProvider(
            { AppMemoryInfoProvider.getAppMemoryInfo() },
            "Failed to get app memory info"
        ),
        scopeFor("appMemory")
    )

    val gcStats = SimpleMetricDelegate(
        metricProvider({ GcStatsInfoProvider.getGcStatsInfo() }, "Failed to get GC stats info"),
        scopeFor("gcStats")
    )

    val ram = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> RamInfoProvider.getRamInfo(ctx) },
            "Failed to get RAM info"
        ),
        scopeFor("ram")
    )

    val battery = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> BatteryInfoProvider.getBatteryInfo(ctx) },
            "Failed to get battery info"
        ),
        scopeFor("battery")
    )

    val device = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> DeviceInfoProvider.getDeviceInfo(ctx) },
            "Failed to get device info"
        ),
        scopeFor("device")
    )

    val storage = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> StorageUsageInfoProvider.getStorageUsageInfo(ctx) },
            "Failed to get storage info"
        ),
        scopeFor("storage")
    )

    val network = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> NetworkQualityInfoProvider.getNetworkQualityInfo(ctx) },
            "Failed to get network info"
        ),
        scopeFor("network")
    )

    val appUptime = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> AppUptimeInfoProvider.getAppUptimeInfo(ctx) },
            "Failed to get app uptime info"
        ),
        scopeFor("appUptime")
    )

    val threadProcess = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> ThreadProcessInfoProvider.getThreadProcessInfo(ctx) },
            "Failed to get thread/process info"
        ),
        scopeFor("threadProcess")
    )

    val thermal = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> ThermalInfoProvider.getThermalInfo(ctx) },
            "Failed to get thermal info"
        ),
        scopeFor("thermal")
    )

    val gpu = SimpleMetricDelegate(
        metricProvider({ GpuInfoProvider.getGpuInfo() }, "Failed to get GPU info"),
        scopeFor("gpu")
    )

    val mediaQuality = ContextMetricDelegate(
        metricProvider(
            { ctx: Context -> MediaQualityProvider.getMediaQualityRecommendation(ctx) },
            "Failed to get media quality recommendation"
        ),
        scopeFor("mediaQuality")
    )

    val startupTime: StartupTimeDelegate = object : StartupTimeDelegate {
        override fun recordActivityOnCreate(context: Context) {
            StartupTimeInfoProvider.recordActivityOnCreate(context)
        }

        override fun recordFirstDraw(context: Context) {
            StartupTimeInfoProvider.recordFirstDraw(context)
        }

        override suspend fun getAsync(context: Context): PerfResult<StartupTimeInfo> {
            return safeCall("Failed to get startup time info") {
                StartupTimeInfoProvider.getStartupTimeInfo()
            }
        }
    }


    suspend fun getFrameRenderingInfoAsync(durationMillis: Long = 2000): PerfResult<FrameRenderingInfo> =
        suspendCancellableCoroutine { cont ->
            FrameRenderingInfoProvider.getFrameRenderingInfo(durationMillis) { result ->
                if (cont.isActive) cont.resume(PerfResult.Success(result), null)
            }
        }

    /**
     * Registers a global crash listener for uncaught exceptions.
     *
     * - The listener will be called for **any uncaught exception** in the host app, any library, or PerfScout itself.
     * - Does **not** catch exceptions handled by try/catch.
     * - Does **not** prevent the app from crashing; the process will still terminate after your callback.
     * - Chains to any previous uncaught exception handler (e.g., Crashlytics, Sentry, system default).
     *
     * | Scenario                        | Will listener be called? |
     * |----------------------------------|:-----------------------:|
     * | Uncaught exception in host code  | Yes                    |
     * | Uncaught exception in library    | Yes                    |
     * | Uncaught exception in PerfScout  | Yes                    |
     * | Exception caught by try/catch    | No                     |
     * | ANR (App Not Responding)         | No (use setAnrListener)|
     *
     * @param listener Callback invoked with [CrashInfo] when an uncaught exception occurs.
     */
    fun setCrashListener(listener: ((CrashInfo) -> Unit)?) =
        CrashMonitorProvider.setListener(listener)

    /**
     * Registers a listener for ANR (Application Not Responding) events only.
     * @param listener Callback invoked with [AnrInfo] when an ANR is detected.
     */
    fun setAnrListener(listener: ((AnrInfo) -> Unit)?) = AnrMonitorProvider.setListener(listener)
    fun setJankListener(listener: ((JankInfo) -> Unit)?) = JankMonitorProvider.setListener(listener)
    fun enableStrictMode(
        detectAll: Boolean = true,
        penaltyLog: Boolean = true,
        penaltyCallback: ((StrictModeViolationInfo) -> Unit)? = null
    ) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        StrictModeMonitorProvider.enable(detectAll, penaltyLog, penaltyCallback)
    } else {
        TODO("VERSION.SDK_INT < P")
    }
}
