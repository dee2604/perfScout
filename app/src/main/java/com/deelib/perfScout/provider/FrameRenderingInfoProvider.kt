package com.deelib.perfScout.provider

import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import com.deelib.perfScout.model.FrameRenderingInfo
import java.util.concurrent.atomic.AtomicLong

internal object FrameRenderingInfoProvider {
    private var frameCount = 0
    private var droppedFrames = 0
    private var slowFrames = 0
    private val frameTimes = mutableListOf<Long>()
    private val lastFrameTime = AtomicLong(0)
    private var isMonitoring = false
    private var choreographer: Choreographer? = null
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (!isMonitoring) return
            
            try {
                val currentTime = System.nanoTime()
                val lastTime = lastFrameTime.getAndSet(currentTime)
                
                if (lastTime > 0) {
                    val frameTime = (currentTime - lastTime) / 1_000_000.0 // Convert to milliseconds
                    frameTimes.add(frameTime.toLong())
                    
                    // Keep only last 60 frames for analysis
                    if (frameTimes.size > 60) {
                        frameTimes.removeAt(0)
                    }
                    
                    // Detect slow frames (>16.67ms for 60fps)
                    if (frameTime > 16.67) {
                        slowFrames++
                    }
                    
                    // Detect dropped frames (>33.33ms for 30fps)
                    if (frameTime > 33.33) {
                        droppedFrames++
                    }
                }
                
                frameCount++
                choreographer?.postFrameCallback(this)
            } catch (e: Exception) {
                // Handle any exceptions gracefully
                isMonitoring = false
            }
        }
    }

    internal fun getFrameRenderingInfo(durationMillis: Long = 2000, callback: (FrameRenderingInfo) -> Unit) {
        if (isMonitoring) return
        
        try {
            // Reset counters
            frameCount = 0
            droppedFrames = 0
            slowFrames = 0
            frameTimes.clear()
            lastFrameTime.set(0)
            isMonitoring = true
            
            // Get choreographer instance
            choreographer = Choreographer.getInstance()
            
            // Start monitoring
            choreographer?.postFrameCallback(frameCallback)
            
            // Stop after duration
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    isMonitoring = false
                    choreographer?.removeFrameCallback(frameCallback)
                    
                    val averageFrameTime = if (frameTimes.isNotEmpty()) frameTimes.average() else 0.0
                    val jankPercentage = if (frameCount > 0) (droppedFrames + slowFrames) * 100.0 / frameCount else 0.0
                    val isSmooth = jankPercentage < 5.0 && averageFrameTime < 16.67
                    
                    // Calculate performance score (0-100)
                    val performanceScore = when {
                        jankPercentage < 2.0 && averageFrameTime < 16.67 -> 95
                        jankPercentage < 5.0 && averageFrameTime < 20.0 -> 80
                        jankPercentage < 10.0 && averageFrameTime < 25.0 -> 60
                        else -> 30
                    }
                    
                    callback(FrameRenderingInfo(
                        totalFrames = frameCount,
                        droppedFrames = droppedFrames,
                        slowFrames = slowFrames,
                        averageFrameTime = averageFrameTime,
                        jankPercentage = jankPercentage,
                        isSmooth = isSmooth,
                        performanceScore = performanceScore
                    ))
                } catch (e: Exception) {
                    // Provide fallback data if callback fails
                    callback(FrameRenderingInfo(
                        totalFrames = 0,
                        droppedFrames = 0,
                        slowFrames = 0,
                        averageFrameTime = 0.0,
                        jankPercentage = 0.0,
                        isSmooth = true,
                        performanceScore = 50
                    ))
                }
            }, durationMillis)
        } catch (e: Exception) {
            // Provide fallback data if monitoring fails to start
            callback(FrameRenderingInfo(
                totalFrames = 0,
                droppedFrames = 0,
                slowFrames = 0,
                averageFrameTime = 0.0,
                jankPercentage = 0.0,
                isSmooth = true,
                performanceScore = 50
            ))
        }
    }
} 