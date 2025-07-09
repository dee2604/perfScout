package com.deelib.perfScout.battery

enum class BatteryHealth {
    UNKNOWN, GOOD, OVERHEAT, DEAD, OVER_VOLTAGE, UNSPECIFIED_FAILURE, COLD
}

enum class BatteryStatus {
    UNKNOWN, CHARGING, DISCHARGING, NOT_CHARGING, FULL
}

data class BatteryInfo(
    val level: Int?,
    val status: BatteryStatus,
    val health: BatteryHealth,
    val temperature: Float?,
    val voltage: Int?,
    val technology: String?
) 