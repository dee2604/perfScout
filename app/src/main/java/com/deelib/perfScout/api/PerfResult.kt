package com.deelib.perfScout.api

sealed class PerfResult<T> {
    data class Success<T>(val info: T) : PerfResult<T>()
    data class Error<T>(val message: String, val throwable: Throwable? = null) : PerfResult<T>()
} 