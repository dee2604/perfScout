package io.github.dee2604.perfScout.thermal

import android.content.Context

object ThermalInfoProvider {
    fun getThermalInfo(context: Context): ThermalInfo {
        return ThermalProvider.getThermalInfo(context)
    }
} 