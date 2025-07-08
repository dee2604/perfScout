package com.deelib.perfScout.provider

import android.os.Handler
import android.os.Looper
import com.deelib.perfScout.model.AppCpuUsageInfo

internal object AppCpuUsageInfoProvider {
    internal fun getAppCpuUsageInfo(callback: (AppCpuUsageInfo) -> Unit, intervalMs: Long = 1000): Unit {
        fun readCpuStat(): Pair<Long, Long> {
            val pid = android.os.Process.myPid()
            val procStat = "/proc/$pid/stat"
            val sysStat = "/proc/stat"
            
            val procCpu = try {
                val parts = java.io.File(procStat).readText().split(" ")
                if (parts.size > 14) {
                    parts[13].toLong() + parts[14].toLong() // utime + stime
                } else {
                    0L
                }
            } catch (e: Exception) { 
                0L 
            }
            
            val sysCpu = try {
                val cpuLine = java.io.File(sysStat).readLines().firstOrNull { it.startsWith("cpu ") }
                cpuLine?.split(Regex("\\s+"))?.drop(1)?.take(8)?.sumOf { it.toLongOrNull() ?: 0L } ?: 0L
            } catch (e: Exception) { 
                0L 
            }
            
            return procCpu to sysCpu
        }
        
        try {
            val (proc1, sys1) = readCpuStat()
            val cpuCores = Runtime.getRuntime().availableProcessors().coerceAtLeast(1)
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    val (proc2, sys2) = readCpuStat()
                    val procDelta = proc2 - proc1
                    val sysDelta = sys2 - sys1
                    // Multiply denominator by number of cores for per-core usage
                    val usage = if (sysDelta > 0) 100.0 * procDelta / (sysDelta * cpuCores) else 0.0
                    callback(AppCpuUsageInfo(usagePercent = usage))
                } catch (e: Exception) {
                    // Provide fallback if calculation fails
                    callback(AppCpuUsageInfo(usagePercent = 0.0))
                }
            }, intervalMs)
        } catch (e: Exception) {
            // Provide fallback if initial reading fails
            callback(AppCpuUsageInfo(usagePercent = 0.0))
        }
    }
} 