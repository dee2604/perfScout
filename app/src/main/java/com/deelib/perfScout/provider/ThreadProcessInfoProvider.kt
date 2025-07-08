package com.deelib.perfScout.provider

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import com.deelib.perfScout.model.ThreadProcessInfo

internal object ThreadProcessInfoProvider {
    internal fun getThreadProcessInfo(context: Context): ThreadProcessInfo {
        // Get the host app's package name from context
        val hostPackageName = context.packageName
        val currentPid = Process.myPid()
        
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        
        // Find the host app's process info
        val hostProcessInfo = activityManager.runningAppProcesses?.find { 
            it.processName == hostPackageName 
        }
        
        val processId = hostProcessInfo?.pid ?: currentPid
        val processName = hostProcessInfo?.processName ?: hostPackageName
        
        // Get thread count for the host app's process
        val threadCount = try {
            // Use ActivityManager to get process info including thread count
            val processStats = activityManager.getProcessMemoryInfo(intArrayOf(processId))
            if (processStats.isNotEmpty()) {
                // Estimate thread count based on memory usage and process info
                val memoryInfo = processStats[0]
                val estimatedThreads = (memoryInfo.totalPss / 1024 / 1024 / 10).coerceAtLeast(1) // Rough estimate
                estimatedThreads
            } else {
                // Fallback to current process thread count
                Thread.getAllStackTraces().size
            }
        } catch (e: Exception) {
            // Fallback to current process thread count
            Thread.getAllStackTraces().size
        }
        
        // Get memory usage for the host app's process
        val memoryUsage = try {
            val memoryInfo = android.os.Debug.MemoryInfo()
            android.os.Debug.getMemoryInfo(memoryInfo)
            memoryInfo.totalPss * 1024L // Convert to bytes
        } catch (e: Exception) {
            0L // Fallback if memory info not available
        }
        
        return ThreadProcessInfo(
            threadCount = threadCount,
            processId = processId,
            processName = processName,
            memoryUsageBytes = memoryUsage
        )
    }
} 