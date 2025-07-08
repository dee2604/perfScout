package com.deelib.perfScout.model

data class NetworkUsageInfo(
    val bytesSent: Long?,
    val bytesReceived: Long?,
    val networkType: String?
) 