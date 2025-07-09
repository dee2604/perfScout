package com.deelib.perfScout.network

data class NetworkUsageInfo(
    val bytesSent: Long?,
    val bytesReceived: Long?,
    val networkType: String?
) 