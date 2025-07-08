package com.deelib.perfScout.model

enum class BatteryHealth {
    UNKNOWN, GOOD, OVERHEAT, DEAD, OVER_VOLTAGE, UNSPECIFIED_FAILURE, COLD
}

enum class BatteryStatus {
    UNKNOWN, CHARGING, DISCHARGING, NOT_CHARGING, FULL
}

data class BatteryInfo(
    val level: Int?,
    val status: BatteryStatus?,
    val health: BatteryHealth?
) 