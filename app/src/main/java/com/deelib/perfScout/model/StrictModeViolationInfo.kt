package com.deelib.perfScout.model

data class StrictModeViolationInfo(
    val policy: String,
    val message: String?,
    val stackTrace: String,
    val timestamp: Long
) 