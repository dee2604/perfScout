package com.deelib.perfScout.core

import android.content.Context
import com.deelib.perfScout.core.PerfResult
import kotlinx.coroutines.CoroutineScope

interface MetricDelegate<T> {
    fun get(): PerfResult<T>
    suspend fun getAsync(): PerfResult<T>
}

interface MetricDelegateWithContext<C, R> {
    fun get(context: C): PerfResult<R>
    suspend fun getAsync(context: C): PerfResult<R>
}

interface MetricProvider<ContextArg, InfoType> {
    fun fetch(context: ContextArg): InfoType
    fun errorMessage(): String

    fun sync(context: ContextArg): PerfResult<InfoType> =
        safeCall(errorMessage()) { fetch(context) }

    suspend fun async(context: ContextArg): PerfResult<InfoType> =
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) { sync(context) }

    fun async(
        context: ContextArg,
        callback: (PerfResult<InfoType>) -> Unit,
        scope: CoroutineScope
    ) =
        scope.launchWithCallback({ async(context) }, callback)
} 