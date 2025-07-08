package com.deelib.perfScout.model

data class CpuInfo(
    val model: String?,
    val cores: Int,
    val minFreqHz: Long?,
    val maxFreqHz: Long?,
    val abi: String?
) 