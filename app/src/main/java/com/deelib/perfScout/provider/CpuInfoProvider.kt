package com.deelib.perfScout.provider

import com.deelib.perfScout.model.CpuInfo
import android.os.Build
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.BufferedReader

internal object CpuInfoProvider {
    internal fun getCpuInfo(): CpuInfo {
        // Get CPU model from /proc/cpuinfo
        var model: String? = null
        try {
            val reader = BufferedReader(InputStreamReader(FileInputStream("/proc/cpuinfo")))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line!!.startsWith("Hardware") || line.startsWith("model name")) {
                    model = line.split(":").getOrNull(1)?.trim()
                    break
                }
            }
            reader.close()
        } catch (e: Exception) {
            // Fallback to Build.MODEL if /proc/cpuinfo is not accessible
            model = Build.MODEL
        }

        // If still null, use a default value
        if (model.isNullOrBlank()) {
            model = "Unknown CPU"
        }

        // Get number of cores
        val cores = Runtime.getRuntime().availableProcessors()

        // Get min/max freq (Hz) from sysfs
        fun readFreq(path: String): Long? {
            return try {
                File(path).readText().trim().toLong()
            } catch (e: Exception) {
                null
            }
        }
        
        val maxFreqHz = readFreq("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")
        val minFreqHz = readFreq("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq")

        // Get ABI
        val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown"

        return CpuInfo(
            model = model,
            cores = cores,
            minFreqHz = minFreqHz,
            maxFreqHz = maxFreqHz,
            abi = abi
        )
    }
} 