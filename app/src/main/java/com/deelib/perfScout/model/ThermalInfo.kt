package com.deelib.perfScout.model

data class ThermalInfo(
    val cpuTemperature: Double?, // in Celsius
    val gpuTemperature: Double?, // in Celsius
    val batteryTemperature: Double?, // in Celsius
    val thermalZone: String?, // thermal zone info
    val isOverheating: Boolean,
    val thermalStatus: String // "NORMAL", "WARM", "HOT", "CRITICAL"
) 