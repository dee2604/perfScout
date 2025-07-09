package com.deelib.perfScout.thermal

import android.content.Context

object ThermalInfoProvider {
    fun getThermalInfo(context: Context): ThermalInfo {
        return ThermalProvider.getThermalInfo(context)
    }
} 