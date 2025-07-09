package com.deelib.perfScout.anr

import android.os.Handler
import android.os.Looper

object AnrMonitorProvider {
    private var listener: ((AnrInfo) -> Unit)? = null
    private var isMonitoring = false
    private var lastPosted = 0L
    private var lastHandled = 0L
    private val mainHandler = Handler(Looper.getMainLooper())
    private const val checkIntervalMs = 2000L
    private const val anrTimeoutMs = 5000L

    internal fun setListener(anrListener: ((AnrInfo) -> Unit)?) {
        listener = anrListener
        if (anrListener != null && !isMonitoring) {
            startMonitoring()
        } else if (anrListener == null && isMonitoring) {
            isMonitoring = false
        }
    }

    private fun startMonitoring() {
        isMonitoring = true
        Thread {
            while (isMonitoring) {
                val postTime = System.currentTimeMillis()
                var handled = false
                mainHandler.post {
                    lastHandled = System.currentTimeMillis()
                    handled = true
                }
                Thread.sleep(anrTimeoutMs)
                if (!handled) {
                    // Main thread did not handle the post in time: ANR detected
                    val stack = Looper.getMainLooper().thread.stackTrace.joinToString("\n")
                    listener?.invoke(
                        AnrInfo(
                            timestamp = System.currentTimeMillis(),
                            durationMs = anrTimeoutMs,
                            mainThreadStack = stack
                        )
                    )
                }
                Thread.sleep(checkIntervalMs)
            }
        }.start()
    }
}
