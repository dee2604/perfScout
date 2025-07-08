package com.deelib.perfScout.model

data class NetworkQualityInfo(
    val latency: Double?, // in milliseconds
    val packetLoss: Double?, // percentage
    val connectionType: String, // "WIFI", "MOBILE", "ETHERNET", "UNKNOWN"
    val signalStrength: Int?, // -100 to 0 dBm for WiFi, 0-31 for mobile
    val networkSpeed: String?, // "SLOW", "MEDIUM", "FAST", "VERY_FAST"
    val isStable: Boolean,
    val qualityScore: Int // 0-100
) 