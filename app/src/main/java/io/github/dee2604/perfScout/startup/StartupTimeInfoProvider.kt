package io.github.dee2604.perfScout.startup

import android.content.Context

internal object StartupTimeInfoProvider {
    private var hostAppStartTime: Long? = null
    private var hostActivityOnCreateTime: Long? = null
    private var hostFirstDrawTime: Long? = null
    private var hostPackageName: String? = null

    fun recordActivityOnCreate(context: Context) {
        val packageName = context.packageName
        if (hostPackageName == null) {
            hostPackageName = packageName
            hostAppStartTime = System.currentTimeMillis()
        }
        if (hostPackageName == packageName) {
            hostActivityOnCreateTime = System.currentTimeMillis()
        }
    }

    fun recordFirstDraw(context: Context) {
        val packageName = context.packageName
        if (hostPackageName == packageName) {
            hostFirstDrawTime = System.currentTimeMillis()
        }
    }

    internal fun getStartupTimeInfo(): StartupTimeInfo {
        val now = System.currentTimeMillis()

        // Calculate cold start time (from host app start to activity onCreate)
        val coldStart = if (hostAppStartTime != null && hostActivityOnCreateTime != null) {
            hostActivityOnCreateTime!! - hostAppStartTime!!
        } else {
            // Fallback: estimate based on typical startup times
            null
        }

        // Calculate time to first draw (from activity onCreate to first draw)
        val timeToFirstDraw = if (hostActivityOnCreateTime != null && hostFirstDrawTime != null) {
            hostFirstDrawTime!! - hostActivityOnCreateTime!!
        } else {
            // If first draw hasn't been recorded yet, estimate based on current time
            hostActivityOnCreateTime?.let { now - it }
        }

        // Warm start is not tracked in this simple implementation
        val warmStart: Long? = null

        return StartupTimeInfo(
            coldStartMs = coldStart,
            warmStartMs = warmStart,
            timeToFirstDrawMs = timeToFirstDraw
        )
    }

    /**
     * Returns startup time info for the given package name.
     * Note: Only works for the current host app process.
     */
    internal fun getStartupTimeInfoForPackage(packageName: String): StartupTimeInfo? {
        // Only return info if it's the host app's package
        if (packageName == hostPackageName) {
            return getStartupTimeInfo()
        }
        return null
    }
} 