package com.deelib.perfScout.uptime

import android.content.Context

object AppUptimeInfoProvider {
    private var hostAppStartTime: Long? = null
    private var hostPackageName: String? = null

    fun getAppUptimeInfo(context: Context): AppUptimeInfo {
        val packageName = context.packageName

        // Initialize host app start time if not set
        if (hostPackageName == null) {
            hostPackageName = packageName
            hostAppStartTime = System.currentTimeMillis()
        }

        // Calculate uptime for the host app
        val uptime = if (hostAppStartTime != null && hostPackageName == packageName) {
            System.currentTimeMillis() - hostAppStartTime!!
        } else {
            // Fallback: estimate uptime based on typical app lifecycle
            0L
        }

        return AppUptimeInfo(uptimeMs = uptime)
    }
} 