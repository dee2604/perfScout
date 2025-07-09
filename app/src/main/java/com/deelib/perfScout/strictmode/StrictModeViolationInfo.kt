package com.deelib.perfScout.strictmode

data class StrictModeViolationInfo(
    val policy: String,
    val message: String?,
    val stackTrace: String,
    val timestamp: Long
) 