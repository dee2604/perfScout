# PerfScout

[![JitPack](https://jitpack.io/v/deekshasinghal326/perfScout.svg)](https://jitpack.io/#deekshasinghal326/perfScout)

**PerfScout** provides unified, safe, and modular access to all vital app and device performance metrics and monitoring features through a single, easy-to-use API.

- All metrics are returned as `PerfResult` (Success/Error) for robust error handling.
- Main categories:
  - **Device metrics:** CPU, RAM, GPU, Battery, Thermal, Device Info
  - **App metrics:** App Memory, App Uptime, GC Stats, Startup Time
  - **System metrics:** Storage, Thread/Process, Network Quality
  - **Advanced features:** Frame Rendering/Jank, Media Quality Recommendation
  - **Monitoring:** Crash, ANR, Jank, StrictMode
- **Crash monitoring:**
  - The crash listener will be called for any uncaught exception in the host app, any library, or PerfScout itself.
  - It does not catch handled exceptions and does not prevent the app from crashing.
  - It chains to any previous uncaught exception handler (e.g., Crashlytics).
- **Startup time tracking:**
  - Requires explicit calls to `startupTime.recordActivityOnCreate` and `startupTime.recordFirstDraw` in your MainActivity lifecycle.
- All features are modular and can be used independently.

---

## Features at a Glance

- CPU, RAM, GPU, Battery, Thermal, Network, Storage, Thread/Process, App Memory, App CPU, GC, App Uptime, Device Info
- Startup Time (cold start, with clear setup instructions)
- ANR Detection (callback, safe, non-intrusive)
- Crash Monitoring (callback, chains to previous handler)
- Jank (Slow/Frozen Frame) Monitoring (callback, rolling window)
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
PerfScout.enableStrictMode(penaltyCallback = { violation ->
    Log.w("PerfScout", "StrictMode violation: $violation")
})
```

---

## Setup for Host Apps

### 1. Add as a dependency

#### JitPack (Recommended)
```kotlin
// build.gradle.kts
dependencies {
    implementation("com.github.deekshasinghal326:perfScout:1.0.1")
}
```

#### Local Maven Repository (Development)
```kotlin
// build.gradle.kts
dependencies {
    implementation("com.github.deekshasinghal326:perfScout:1.0.1")
}

// in settings.gradle
repositories {
    maven { url = uri("https://jitpack.io") }
}
```

> _If you're using this as a module, add to your `settings.gradle` and `build.gradle` as appropriate._

### 2. Startup Time Tracking Setup (Optional)

For accurate startup time measurements, add these calls to your MainActivity:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerfScout.startupTime.recordActivityOnCreate(this)
        // ... rest of onCreate
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            PerfScout.startupTime.recordFirstDraw(this)
        }
    }
}
```

**Note:** Startup time tracking is optional. If you don't add these calls, startup time methods will return null values.

---

## Usage Examples (New API)

### Basic Metrics
```kotlin
// All metrics return PerfResult<T> (Success or Error)
val cpuResult = PerfScout.cpu.get() // or getAsync()
val ramResult = PerfScout.ram.get(context)
val gpuResult = PerfScout.gpu.get()
val batteryResult = PerfScout.battery.get(context)
val thermalResult = PerfScout.thermal.get(context)
val appMemoryResult = PerfScout.appMemory.get()
val appUptimeResult = PerfScout.appUptime.get(context)
val gcStatsResult = PerfScout.gcStats.get()
val networkResult = PerfScout.network.get(context)
val storageResult = PerfScout.storage.get(context)
val threadResult = PerfScout.threadProcess.get(context)
val mediaQualityResult = PerfScout.mediaQuality.get(context)
```

**Handling results:**
```kotlin
when (cpuResult) {
    is PerfResult.Success -> Log.d("PerfScout", "CPU: ${cpuResult.info}")
    is PerfResult.Error -> Log.e("PerfScout", "Error: ${cpuResult.message}")
}
```

### Startup Time Tracking

Add these calls to your `MainActivity`:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    PerfScout.startupTime.recordActivityOnCreate(this)
    // ...
}
override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) {
        PerfScout.startupTime.recordFirstDraw(this)
    }
}
```

Get startup time info:
```kotlin
val startupResult = PerfScout.startupTime.get(context)
```

---

### Frame Rendering/Jank

```kotlin
lifecycleScope.launch {
    val frameResult = PerfScout.getFrameRenderingInfoAsync(3000)
    when (frameResult) {
        is PerfResult.Success -> Log.d("PerfScout", "Frame Info: ${frameResult.info}")
        is PerfResult.Error -> Log.e("PerfScout", "Error: ${frameResult.message}")
    }
}
```

---

### Crash & ANR Monitoring

**Set up listeners:**
```kotlin
PerfScout.setCrashListener { crashInfo ->
    Log.e("PerfScout", "Crash detected: $crashInfo")
}
PerfScout.setAnrListener { anrInfo ->
    Log.w("PerfScout", "ANR detected: $anrInfo")
}
```

**How Crash Monitoring Works:**
- The crash listener will be called for **any uncaught exception** in the host app, any library, or PerfScout itself.
- It does **not** catch exceptions that are handled by try/catch.
- It does **not** prevent the app from crashing; the process will still terminate after your callback.
- PerfScout will **chain to any previous uncaught exception handler** (such as Crashlytics, Sentry, or the system default), so it will not interfere with other crash reporting tools.

| Scenario                        | Will PerfScout crash listener be called? |
|----------------------------------|:---------------------------------------:|
| Uncaught exception in host code  | Yes                                     |
| Uncaught exception in library    | Yes                                     |
| Uncaught exception in PerfScout  | Yes                                     |
| Exception caught by try/catch    | No                                      |
| ANR (App Not Responding)         | No (use ANR listener)                   |

**Demo in Sample App:**
- In the "Advanced Features" section, use the "Trigger Crash" button to throw a deliberate exception.
- Use the "Trigger ANR" button to block the main thread for 10 seconds.
- Your listeners will be called when these events occur.

---

### StrictMode, Jank, and More

```kotlin
PerfScout.setJankListener { jankInfo ->
    Log.w("PerfScout", "Jank detected: $jankInfo")
}
PerfScout.enableStrictMode(penaltyCallback = { violation ->
    Log.w("PerfScout", "StrictMode violation: $violation")
})
```

---

## Notes & Best Practices

- All metrics are safe: if a metric is unavailable, you get a `PerfResult.Error`.
- Startup time tracking requires the host app to call the lifecycle methods.
- Crash/ANR demo buttons are for testing only—remove in production.
- Frame rendering analysis requires UI thread access and should be used during user interaction.
- Thermal information availability varies by device and Android version.
- Network quality measurements may not work on all devices or network configurations.
- GPU info may not be available on all devices.
- All features are modular and can be used independently.
- Memory leak warning is a simple heuristic: it triggers if memory usage grows by a set threshold over a rolling window. For deep leak analysis, use LeakCanary in your app.
- Crash monitoring uses a global uncaught exception handler and will not interfere with existing crash reporting (it chains to previous handlers).
- Jank monitoring reports the percentage of slow (>16ms) and frozen (>700ms) frames every 5 seconds.
- StrictMode violation reporting is opt-in and should only be enabled in debug builds. It helps catch bad practices that can lead to Vitals issues.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

© 2024 PerfScout 
