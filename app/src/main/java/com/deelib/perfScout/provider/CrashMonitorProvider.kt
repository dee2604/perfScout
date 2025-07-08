package com.deelib.perfScout.provider

import com.deelib.perfScout.model.CrashInfo

internal object CrashMonitorProvider {
    private var listener: ((CrashInfo) -> Unit)? = null
    private var previousHandler: Thread.UncaughtExceptionHandler? = null
    private var isInstalled = false

    internal fun setListener(crashListener: ((CrashInfo) -> Unit)?) {
        listener = crashListener
        if (!isInstalled && crashListener != null) {
            installHandler()
        }
    }

    private fun installHandler() {
        previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            listener?.invoke(
                CrashInfo(
                    timestamp = System.currentTimeMillis(),
                    threadName = thread.name,
                    exceptionMessage = throwable.message,
                    stackTrace = throwable.stackTraceToString()
                )
            )
            // Chain to previous handler (e.g., Crashlytics)
            previousHandler?.uncaughtException(thread, throwable)
        }
        isInstalled = true
    }
} 