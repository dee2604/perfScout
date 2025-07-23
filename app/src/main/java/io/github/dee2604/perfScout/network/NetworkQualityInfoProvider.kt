package io.github.dee2604.perfScout.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager

internal object NetworkQualityInfoProvider {
    internal fun getNetworkQualityInfo(context: Context): NetworkQualityInfo {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        var connectionType = "UNKNOWN"
        var signalStrength: Int? = null
        var networkSpeed = "UNKNOWN"
        if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    connectionType = "WIFI"
                    signalStrength = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        networkCapabilities.signalStrength
                    } else {
                        @Suppress("DEPRECATION")
                        val rssi = try {
                            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? android.net.wifi.WifiManager
                            wifiManager?.connectionInfo?.rssi
                        } catch (e: Exception) { null }
                        rssi
                    }
                    networkSpeed = when {
                        networkCapabilities.linkDownstreamBandwidthKbps >= 100000 -> "VERY_FAST"
                        networkCapabilities.linkDownstreamBandwidthKbps >= 50000 -> "FAST"
                        networkCapabilities.linkDownstreamBandwidthKbps >= 10000 -> "MEDIUM"
                        else -> "SLOW"
                    }
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    connectionType = "MOBILE"
                    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    signalStrength = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        telephonyManager.signalStrength?.level ?: 0
                    } else {
                        null
                    }
                    networkSpeed = when {
                        networkCapabilities.linkDownstreamBandwidthKbps >= 100000 -> "VERY_FAST"
                        networkCapabilities.linkDownstreamBandwidthKbps >= 50000 -> "FAST"
                        networkCapabilities.linkDownstreamBandwidthKbps >= 10000 -> "MEDIUM"
                        else -> "SLOW"
                    }
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    connectionType = "ETHERNET"
                    networkSpeed = "VERY_FAST"
                }
            }
        }
        val latency: Double? = when (connectionType) {
            "WIFI" -> when (networkSpeed) {
                "VERY_FAST" -> 5.0
                "FAST" -> 10.0
                "MEDIUM" -> 20.0
                else -> 50.0
            }
            "MOBILE" -> when (networkSpeed) {
                "VERY_FAST" -> 15.0
                "FAST" -> 25.0
                "MEDIUM" -> 40.0
                else -> 80.0
            }
            "ETHERNET" -> 2.0
            else -> null
        }
        val packetLoss: Double? = when (connectionType) {
            "WIFI" -> when (signalStrength) {
                in -50..0 -> 0.1
                in -70..-51 -> 0.5
                in -85..-71 -> 2.0
                else -> 5.0
            }
            "MOBILE" -> when (signalStrength) {
                in 4..4 -> 0.2
                in 3..3 -> 1.0
                in 2..2 -> 3.0
                in 0..1 -> 8.0
                null -> 8.0
                else -> 8.0
            }
            "ETHERNET" -> 0.05
            else -> null
        }
        val isStable = latency != null && latency < 100 && packetLoss != null && packetLoss < 5.0
        val qualityScore = when {
            latency == null || packetLoss == null -> 50
            latency < 20 && packetLoss < 1.0 -> 95
            latency < 50 && packetLoss < 3.0 -> 80
            latency < 100 && packetLoss < 5.0 -> 60
            else -> 25
        }
        return NetworkQualityInfo(
            latency = latency,
            packetLoss = packetLoss,
            connectionType = connectionType,
            signalStrength = signalStrength,
            networkSpeed = networkSpeed,
            isStable = isStable,
            qualityScore = qualityScore
        )
    }

} 