package com.deelib.perfScout.cpu

data class CpuInfo(
    val model: String?,
    val cores: Int,
    val minFreqHz: Long?,
    val maxFreqHz: Long?,
    val abi: String?
) 