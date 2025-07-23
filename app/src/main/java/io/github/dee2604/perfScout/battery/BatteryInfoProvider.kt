package io.github.dee2604.perfScout.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import io.github.dee2604.perfScout.battery.BatteryHealth
import io.github.dee2604.perfScout.battery.BatteryInfo
import io.github.dee2604.perfScout.battery.BatteryStatus

internal object BatteryInfoProvider {
    internal fun getBatteryInfo(context: Context): BatteryInfo {
        return try {
            val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val percent = if (level != null && scale != null && scale > 0) (level * 100 / scale) else 0
            val statusInt = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val healthInt = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: BatteryManager.BATTERY_HEALTH_UNKNOWN

            val status = when (statusInt) {
                BatteryManager.BATTERY_STATUS_CHARGING -> BatteryStatus.CHARGING
                BatteryManager.BATTERY_STATUS_DISCHARGING -> BatteryStatus.DISCHARGING
                BatteryManager.BATTERY_STATUS_FULL -> BatteryStatus.FULL
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> BatteryStatus.NOT_CHARGING
                else -> BatteryStatus.UNKNOWN
            }

            val health = when (healthInt) {
                BatteryManager.BATTERY_HEALTH_GOOD -> BatteryHealth.GOOD
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> BatteryHealth.OVERHEAT
                BatteryManager.BATTERY_HEALTH_DEAD -> BatteryHealth.DEAD
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> BatteryHealth.OVER_VOLTAGE
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> BatteryHealth.UNSPECIFIED_FAILURE
                BatteryManager.BATTERY_HEALTH_COLD -> BatteryHealth.COLD
                else -> BatteryHealth.UNKNOWN
            }

            BatteryInfo(
                level = percent,
                status = status,
                health = health,
                temperature = null,
                voltage = null,
                technology = null
            )
        } catch (e: Exception) {
            BatteryInfo(
                level = 0,
                status = BatteryStatus.UNKNOWN,
                health = BatteryHealth.UNKNOWN,
                temperature = null,
                voltage = null,
                technology = null
            )
        }
    }
} 