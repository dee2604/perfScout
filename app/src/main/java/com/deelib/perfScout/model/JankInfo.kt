package com.deelib.perfScout.model

data class JankInfo(
    val totalFrames: Int,
    val slowFrames: Int,
    val frozenFrames: Int,
    val slowFramePercent: Double,
    val frozenFramePercent: Double
) 