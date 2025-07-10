package com.deelib.perfScout.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public inline fun <T> safeCall(errorMessage: String, block: () -> T): PerfResult<T> = try {
    PerfResult.Success(block())
} catch (e: Exception) {
    PerfResult.Error("$errorMessage: ${e.message}", e)
}


public inline fun <T> CoroutineScope.launchWithCallback(
    crossinline block: suspend () -> T,
    crossinline callback: (T) -> Unit
) {
    launch {
        val result = block()
        withContext(Dispatchers.Main) { callback(result) }
    }
}