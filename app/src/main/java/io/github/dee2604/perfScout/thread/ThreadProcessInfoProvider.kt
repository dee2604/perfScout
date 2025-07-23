package io.github.dee2604.perfScout.thread

import android.app.ActivityManager
import android.content.Context
import android.os.Process

internal object ThreadProcessInfoProvider {
    internal fun getThreadProcessInfo(context: Context): ThreadProcessInfo {
        val hostPackageName = context.packageName
        val currentPid = Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val hostProcessInfo = activityManager.runningAppProcesses?.find { it.processName == hostPackageName }
        val processId = hostProcessInfo?.pid ?: currentPid
        val processName = hostProcessInfo?.processName ?: hostPackageName
        val threadCount = try {
            val processStats = activityManager.getProcessMemoryInfo(intArrayOf(processId))
            if (processStats.isNotEmpty()) {
                val memoryInfo = processStats[0]
                (memoryInfo.totalPss / 1024 / 1024 / 10).coerceAtLeast(1)
            } else {
                Thread.getAllStackTraces().size
            }
        } catch (e: Exception) {
            Thread.getAllStackTraces().size
        }
        val memoryUsage = try {
            val memoryInfo = android.os.Debug.MemoryInfo()
            android.os.Debug.getMemoryInfo(memoryInfo)
            memoryInfo.totalPss * 1024L
        } catch (e: Exception) {
            0L
        }
        return ThreadProcessInfo(
            threadCount = threadCount,
            processId = processId,
            processName = processName,
            memoryUsageBytes = memoryUsage
        )
    }
} 