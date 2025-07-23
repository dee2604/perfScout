package io.github.dee2604.perfScout.jank

import android.os.Handler
import android.os.Looper
import android.view.Choreographer

object JankMonitorProvider {
    private var listener: ((JankInfo) -> Unit)? = null
    private var installed = false
    private var handler: Handler? = null
    private var frameCount = 0
    private var slowFrames = 0
    private var frozenFrames = 0
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            frameCount++
            val frameTimeMs = (System.nanoTime() - frameTimeNanos) / 1_000_000.0
            if (frameTimeMs > 16.67) slowFrames++
            if (frameTimeMs > 700.0) frozenFrames++
            Choreographer.getInstance().postFrameCallback(this)
        }
    }
    private val reportRunnable = object : Runnable {
        override fun run() {
            val slowPercent = if (frameCount > 0) slowFrames * 100.0 / frameCount else 0.0
            val frozenPercent = if (frameCount > 0) frozenFrames * 100.0 / frameCount else 0.0
            listener?.invoke(
                JankInfo(
                    totalFrames = frameCount,
                    slowFrames = slowFrames,
                    frozenFrames = frozenFrames,
                    slowFramePercent = slowPercent,
                    frozenFramePercent = frozenPercent
                )
            )
            frameCount = 0
            slowFrames = 0
            frozenFrames = 0
            handler?.postDelayed(this, 5000)
        }
    }
    fun setListener(l: ((JankInfo) -> Unit)?) {
        listener = l
        if (!installed && l != null) {
            handler = Handler(Looper.getMainLooper())
            Choreographer.getInstance().postFrameCallback(frameCallback)
            handler?.postDelayed(reportRunnable, 5000)
            installed = true
        } else if (l == null && installed) {
            handler?.removeCallbacks(reportRunnable)
            installed = false
        }
    }
} 