package io.github.dee2604.perfScout.device

data class DeviceInfo(
    val manufacturer: String?,
    val model: String?,
    val osVersion: String?,
    val apiLevel: Int?,
    val screenSize: String?,
    val screenDensity: String?
) 