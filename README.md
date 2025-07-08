# PerfScout

**PerfScout** is a modular Android library that provides easy, permission-less access to vital app and device performance metrics with a single method call per feature.

---

## Features at a Glance

- CPU, RAM, GPU, Battery, Thermal, Network, Storage, Thread/Process, App Memory, App CPU, GC, App Uptime, Device Info
- Startup Time (cold start, with clear setup instructions)
- ANR Detection (callback, safe, non-intrusive)
- Crash Monitoring (callback, chains to previous handler)
- Jank (Slow/Frozen Frame) Monitoring (callback, rolling window)
- Memory Leak Warning (simple heuristic, callback)
- Media Quality Recommendation (for multimedia apps)
- StrictMode Violation Reporting (opt-in, callback, API 28+)
- Network Quality (backward compatible, no ANR risk)

---

## Quick Start

```kotlin
// Add to your Application or MainActivity
PerfScout.setCrashListener { crashInfo ->
    Log.e("PerfScout", "Crash detected: $crashInfo")
}
PerfScout.setAnrListener { anrInfo ->
    Log.w("PerfScout", "ANR detected: $anrInfo")
}
PerfScout.setJankListener { jankInfo ->
    Log.w("PerfScout", "Jank detected: $jankInfo")
}
PerfScout.monitorMemoryLeaks(context) { isLeaking, usageHistory ->
    if (isLeaking) Log.w("PerfScout", "Possible memory leak: $usageHistory")
}
PerfScout.enableStrictMode(penaltyCallback = { violation ->
    Log.w("PerfScout", "StrictMode violation: $violation")
})
```

---

## Setup for Host Apps

### 1. Add as a dependency

#### Maven Central (Recommended)
```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.deekshasinghal:perfscout:1.0.0")
}
```

#### Local Maven Repository (Development)
```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.deekshasinghal:perfscout:1.0.0")
}

// Add local Maven repository
repositories {
    mavenLocal()
}
```

> _If you're using this as a module, add to your `settings.gradle` and `build.gradle` as appropriate._

### 2. Startup Time Tracking Setup (Optional)

For accurate startup time measurements, add these calls to your MainActivity:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerfScout.recordActivityOnCreate(this)
        // ... rest of onCreate
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            PerfScout.recordFirstDraw(this)
        }
    }
}
```

**Note:** Startup time tracking is optional. If you don't add these calls, startup time methods will return null values.

---

## Usage Examples

### Basic Metrics
```kotlin
val cpuInfo = PerfScout.getCpuInfo()
val ramInfo = PerfScout.getRamInfo(context)
val gpuInfo = PerfScout.getGpuInfo()
val batteryInfo = PerfScout.getBatteryInfo(context)
val thermalInfo = PerfScout.getThermalInfo(context)
val appMemory = PerfScout.getAppMemoryInfo()
val appUptime = PerfScout.getAppUptimeInfo(context)
val gcStats = PerfScout.getGcStatsInfo()
val networkUsage = PerfScout.getNetworkUsageInfo(context)
val networkQuality = PerfScout.getNetworkQualityInfo(context)
val storageUsage = PerfScout.getStorageUsageInfo(context)
```

### Advanced Features
```kotlin
// Startup time tracking (requires setup in MainActivity)
val startupInfo = PerfScout.getStartupTimeInfo()
val startupInfoForPackage = PerfScout.getStartupTimeInfoForPackage(packageName)

// Frame rendering analysis
PerfScout.getFrameRenderingInfo(3000) { frameInfo ->
    println("Performance Score: ${frameInfo.performanceScore}")
    println("Jank Percentage: ${frameInfo.jankPercentage}%")
}

// App CPU usage monitoring
PerfScout.getAppCpuUsageInfo({ cpuInfo ->
    println("CPU Usage: ${cpuInfo.usagePercent}%")
}, 1000)

// Memory leak warning (simple heuristic)
PerfScout.monitorMemoryLeaks(context) { isLeaking, usageHistory ->
    if (isLeaking) Log.w("PerfScout", "Possible memory leak: $usageHistory")
}

// Crash monitoring
PerfScout.setCrashListener { crashInfo ->
    Log.e("PerfScout", "Crash detected: $crashInfo")
}

// ANR monitoring
PerfScout.setAnrListener { anrInfo ->
    Log.w("PerfScout", "ANR detected: $anrInfo")
}

// Jank monitoring
PerfScout.setJankListener { jankInfo ->
    Log.w("PerfScout", "Jank detected: $jankInfo")
}

// StrictMode violation reporting (opt-in, for debug builds)
PerfScout.enableStrictMode(penaltyCallback = { violation ->
    Log.w("PerfScout", "StrictMode violation: $violation")
})

// GPU info with OpenGL context (for apps using OpenGL)
val gl10: GL10 = // ... get from your renderer
val gpuInfo = PerfScout.getGpuInfo(gl10)

// Media quality recommendation for multimedia apps
val mediaQuality = PerfScout.getMediaQualityRecommendation(context)
val mediaAnalysis = PerfScout.getMediaQualityAnalysis(context)
```

---

## Notes & Best Practices

- Frame rendering analysis requires UI thread access and should be used during user interaction.
- Thermal information availability varies by device and Android version.
- Network quality measurements may not work on all devices or network configurations.
- GPU info requires OpenGL context for accurate data. Use `getGpuInfo(gl10)` for apps with OpenGL.
- All features are modular and can be used independently.
- Startup time tracking requires manual lifecycle callbacks in MainActivity.
- Memory leak warning is a simple heuristic: it triggers if memory usage grows by a set threshold over a rolling window. For deep leak analysis, use LeakCanary in your app.
- Crash monitoring uses a global uncaught exception handler and will not interfere with existing crash reporting (it chains to previous handlers).
- Jank monitoring reports the percentage of slow (>16ms) and frozen (>700ms) frames every 5 seconds.
- StrictMode violation reporting is opt-in and should only be enabled in debug builds. It helps catch bad practices that can lead to Vitals issues.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

© 2024 PerfScout 