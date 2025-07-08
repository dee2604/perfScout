package com.deelib.perfScout.provider

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import com.deelib.perfScout.model.NetworkUsageInfo

internal object NetworkUsageInfoProvider {
    internal fun getNetworkUsageInfo(context: Context): NetworkUsageInfo {
        return try {
            val bytesSent = TrafficStats.getTotalTxBytes()
            val bytesReceived = TrafficStats.getTotalRxBytes()
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetwork
            val networkType = if (activeNetwork != null) {
                val nc = cm.getNetworkCapabilities(activeNetwork)
                when {
                    nc == null -> "Unknown"
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                    else -> "Other"
                }
            } else {
                "None"
            }
            NetworkUsageInfo(
                bytesSent = bytesSent,
                bytesReceived = bytesReceived,
                networkType = networkType
            )
        } catch (e: Exception) {
            // Fallback values if network info cannot be retrieved
            NetworkUsageInfo(
                bytesSent = 0L,
                bytesReceived = 0L,
                networkType = "Unknown"
            )
        }
    }
} 