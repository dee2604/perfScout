package com.deelib.perfScout.device

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics

internal object DeviceInfoProvider {
    internal fun getDeviceInfo(context: Context): DeviceInfo {
        val displayMetrics = context.resources.displayMetrics
        val screenSize = "${displayMetrics.widthPixels}x${displayMetrics.heightPixels}"
        val screenDensity = when (displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> "LOW"
            DisplayMetrics.DENSITY_MEDIUM -> "MEDIUM"
            DisplayMetrics.DENSITY_HIGH -> "HIGH"
            DisplayMetrics.DENSITY_XHIGH -> "XHIGH"
            DisplayMetrics.DENSITY_XXHIGH -> "XXHIGH"
            DisplayMetrics.DENSITY_XXXHIGH -> "XXXHIGH"
            else -> "${displayMetrics.densityDpi}dpi"
        }

        return DeviceInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            osVersion = Build.VERSION.RELEASE,
            apiLevel = Build.VERSION.SDK_INT,
            screenSize = screenSize,
            screenDensity = screenDensity
        )
    }
} 