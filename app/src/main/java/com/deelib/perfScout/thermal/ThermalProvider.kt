package com.deelib.perfScout.thermal

import android.content.Context

object ThermalProvider {
    internal fun getThermalInfo(context: Context): ThermalInfo {
        var cpuTemp: Double? = null
        var gpuTemp: Double? = null
        var batteryTemp: Double? = null
        var thermalZone: String? = null

        try {
            // Try to read CPU temperature from thermal zones
            val thermalZones = listOf(
                "/sys/class/thermal/thermal_zone0/temp",
                "/sys/class/thermal/thermal_zone1/temp",
                "/sys/class/thermal/thermal_zone2/temp",
                "/sys/devices/virtual/thermal/thermal_zone0/temp"
            )

            for (zone in thermalZones) {
                try {
                    val temp = java.io.File(zone).readText().trim().toDoubleOrNull()
                    if (temp != null && temp > 0) {
                        cpuTemp = temp / 1000.0 // Convert from millidegrees to Celsius
                        thermalZone = zone
                        break
                    }
                } catch (e: Exception) {
                    // Continue to next thermal zone
                }
            }

            // Try to read GPU temperature
            val gpuThermalZones = listOf(
                "/sys/class/thermal/thermal_zone1/temp",
                "/sys/class/thermal/thermal_zone2/temp",
                "/sys/devices/platform/gpu/temp"
            )

            for (zone in gpuThermalZones) {
                try {
                    val temp = java.io.File(zone).readText().trim().toDoubleOrNull()
                    if (temp != null && temp > 0) {
                        gpuTemp = temp / 1000.0
                        break
                    }
                } catch (e: Exception) {
                    // Continue to next thermal zone
                }
            }

            // Try to read battery temperature
            val batteryThermalZones = listOf(
                "/sys/class/thermal/thermal_zone3/temp",
                "/sys/class/power_supply/battery/temp",
                "/sys/class/power_supply/battery/batt_temp"
            )

            for (zone in batteryThermalZones) {
                try {
                    val temp = java.io.File(zone).readText().trim().toDoubleOrNull()
                    if (temp != null && temp > 0) {
                        batteryTemp = temp / 1000.0
                        break
                    }
                } catch (e: Exception) {
                    // Continue to next thermal zone
                }
            }

        } catch (e: Exception) {
            // Thermal info not available
        }

        // Determine thermal status
        val maxTemp = listOfNotNull(cpuTemp, gpuTemp, batteryTemp).maxOrNull() ?: 0.0
        val thermalStatus = when {
            maxTemp >= 80.0 -> "CRITICAL"
            maxTemp >= 70.0 -> "HOT"
            maxTemp >= 50.0 -> "WARM"
            else -> "NORMAL"
        }

        val isOverheating = maxTemp >= 80.0

        return ThermalInfo(
            cpuTemperature = cpuTemp,
            gpuTemperature = gpuTemp,
            batteryTemperature = batteryTemp,
            thermalZone = thermalZone,
            isOverheating = isOverheating,
            thermalStatus = thermalStatus
        )
    }
} 