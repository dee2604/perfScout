package com.deelib.perfScout.provider

import android.opengl.GLES10
import com.deelib.perfScout.model.GpuInfo
import javax.microedition.khronos.opengles.GL10

internal object GpuInfoProvider {
    /**
     * Returns GPU info using GLES10.glGetString().
     * Note: This only works if called from a thread with an active OpenGL context (e.g., after a GL surface is created).
     * If called outside a GL context, it will return fallback values.
     */
    internal fun getGpuInfo(): GpuInfo {
        return try {
            val vendor = GLES10.glGetString(GL10.GL_VENDOR) ?: "Unknown"
            val renderer = GLES10.glGetString(GL10.GL_RENDERER) ?: "Unknown"
            val version = GLES10.glGetString(GL10.GL_VERSION) ?: "Unknown"
            
            // Additional fallback if OpenGL returns null
            GpuInfo(
                vendor = if (vendor == "Unknown" || vendor.isBlank()) "Unknown GPU Vendor (no OpenGL context)" else vendor,
                renderer = if (renderer == "Unknown" || renderer.isBlank()) "Unknown GPU Renderer (no OpenGL context)" else renderer,
                version = if (version == "Unknown" || version.isBlank()) "Unknown GPU Version (no OpenGL context)" else version
            )
        } catch (e: Exception) {
            // Fallback values when OpenGL is not available
            GpuInfo(
                vendor = "Unknown GPU Vendor (no OpenGL context)",
                renderer = "Unknown GPU Renderer (no OpenGL context)", 
                version = "Unknown GPU Version (no OpenGL context)"
            )
        }
    }

    /**
     * Returns GPU info using a provided GL10 instance (from a valid OpenGL context).
     * Host apps using OpenGL can call this from their renderer for accurate info.
     */
    internal fun getGpuInfo(gl: GL10): GpuInfo {
        return try {
            val vendor = gl.glGetString(GL10.GL_VENDOR) ?: "Unknown"
            val renderer = gl.glGetString(GL10.GL_RENDERER) ?: "Unknown"
            val version = gl.glGetString(GL10.GL_VERSION) ?: "Unknown"
            GpuInfo(
                vendor = if (vendor == "Unknown" || vendor.isBlank()) "Unknown GPU Vendor (GL10)" else vendor,
                renderer = if (renderer == "Unknown" || renderer.isBlank()) "Unknown GPU Renderer (GL10)" else renderer,
                version = if (version == "Unknown" || version.isBlank()) "Unknown GPU Version (GL10)" else version
            )
        } catch (e: Exception) {
            GpuInfo(
                vendor = "Unknown GPU Vendor (GL10)",
                renderer = "Unknown GPU Renderer (GL10)",
                version = "Unknown GPU Version (GL10)"
            )
        }
    }
} 