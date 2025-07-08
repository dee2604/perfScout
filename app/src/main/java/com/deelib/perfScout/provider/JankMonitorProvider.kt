package com.deelib.perfScout.provider

import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import com.deelib.perfScout.model.JankInfo

internal object JankMonitorProvider {
    private var listener: ((JankInfo) -> Unit)? = null
    private var isMonitoring = false
    private val frameTimes = mutableListOf<Long>()
    private var lastFrameTimeNanos: Long = 0L
    private val windowMs = 5000L
    private val mainHandler = Handler(Looper.getMainLooper())
    private val choreographer: Choreographer = Choreographer.getInstance()

    internal fun setListener(jankListener: ((JankInfo) -> Unit)?) {
        listener = jankListener
        if (jankListener != null && !isMonitoring) {
            startMonitoring()
        } else if (jankListener == null && isMonitoring) {
            isMonitoring = false
        }
    }

    private fun startMonitoring() {
        isMonitoring = true
        frameTimes.clear()
        lastFrameTimeNanos = 0L
        fun frameCallback(frameTimeNanos: Long) {
            if (!isMonitoring) return
            if (lastFrameTimeNanos > 0) {
                val frameTimeMs = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000
                frameTimes.add(frameTimeMs)
            }
            lastFrameTimeNanos = frameTimeNanos
            choreographer.postFrameCallback(::frameCallback)
        }
        choreographer.postFrameCallback(::frameCallback)
        // Periodically report jank info
        mainHandler.postDelayed(object : Runnable {
            override fun run() {
                if (!isMonitoring) return
                if (frameTimes.isNotEmpty()) {
                    val total = frameTimes.size
                    val slow = frameTimes.count { it > 16 }
                    val frozen = frameTimes.count { it > 700 }
                    val slowPercent = if (total > 0) slow * 100.0 / total else 0.0
                    val frozenPercent = if (total > 0) frozen * 100.0 / total else 0.0
                    listener?.invoke(
                        JankInfo(
                            totalFrames = total,
                            slowFrames = slow,
                            frozenFrames = frozen,
                            slowFramePercent = slowPercent,
                            frozenFramePercent = frozenPercent
                        )
                    )
                    frameTimes.clear()
                }
                mainHandler.postDelayed(this, windowMs)
            }
        }, windowMs)
    }
} 