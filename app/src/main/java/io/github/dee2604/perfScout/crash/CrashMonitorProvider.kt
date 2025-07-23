package io.github.dee2604.perfScout.crash

object CrashMonitorProvider {
    private var listener: ((CrashInfo) -> Unit)? = null
    private var installed = false
    private var previousHandler: Thread.UncaughtExceptionHandler? = null

    fun setListener(l: ((CrashInfo) -> Unit)?) {
        listener = l
        if (!installed && l != null) {
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
                previousHandler?.uncaughtException(thread, throwable)
            }
            installed = true
        } else if (l == null && installed) {
            Thread.setDefaultUncaughtExceptionHandler(previousHandler)
            installed = false
        }
    }
} 