package io.github.dee2604.perfScout.jank

data class JankInfo(
    val totalFrames: Int,
    val slowFrames: Int,
    val frozenFrames: Int,
    val slowFramePercent: Double,
    val frozenFramePercent: Double
) 