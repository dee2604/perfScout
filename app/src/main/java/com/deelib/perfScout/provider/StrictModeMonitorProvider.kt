package com.deelib.perfScout.provider

import android.os.StrictMode
import android.os.Build
import com.deelib.perfScout.model.StrictModeViolationInfo

internal object StrictModeMonitorProvider {
    internal fun enable(
        detectAll: Boolean = true,
        penaltyLog: Boolean = true,
        penaltyCallback: ((StrictModeViolationInfo) -> Unit)? = null
    ) {
        val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
        val vmPolicyBuilder = StrictMode.VmPolicy.Builder()

        if (detectAll) {
            threadPolicyBuilder.detectAll()
            vmPolicyBuilder.detectAll()
        } else {
            threadPolicyBuilder.detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
            vmPolicyBuilder.detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
        }

        if (penaltyLog) {
            threadPolicyBuilder.penaltyLog()
            vmPolicyBuilder.penaltyLog()
        }

        if (penaltyCallback != null && Build.VERSION.SDK_INT >= 28) {
            val mainHandler = android.os.Handler(android.os.Looper.getMainLooper())
            val mainExecutor = java.util.concurrent.Executor { runnable -> mainHandler.post(runnable) }
            threadPolicyBuilder.penaltyListener(mainExecutor) { violation ->
                val throwable = violation as Throwable
                penaltyCallback(
                    StrictModeViolationInfo(
                        policy = "ThreadPolicy",
                        message = throwable.message,
                        stackTrace = throwable.stackTraceToString(),
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
            vmPolicyBuilder.penaltyListener(mainExecutor) { violation ->
                val throwable = violation as Throwable
                penaltyCallback(
                    StrictModeViolationInfo(
                        policy = "VmPolicy",
                        message = throwable.message,
                        stackTrace = throwable.stackTraceToString(),
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
        // On API <28, penaltyListener is not available, so only penaltyLog will be used.

        StrictMode.setThreadPolicy(threadPolicyBuilder.build())
        StrictMode.setVmPolicy(vmPolicyBuilder.build())
    }
} 