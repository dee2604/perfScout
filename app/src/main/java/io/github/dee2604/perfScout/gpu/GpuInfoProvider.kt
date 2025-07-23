package io.github.dee2604.perfScout.gpu
import android.opengl.GLES10
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.opengles.GL10


internal object GpuInfoProvider {
    private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098

    fun getGpuInfo(): GpuInfo {
        return try {
            val egl = EGLContext.getEGL() as EGL10
            val display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
            val version = IntArray(2)
            egl.eglInitialize(display, version)

            val configs = arrayOfNulls<EGLConfig>(1)
            val numConfigs = IntArray(1)
            egl.eglChooseConfig(display, intArrayOf(EGL10.EGL_NONE), configs, 1, numConfigs)

            val attribList = intArrayOf(
                EGL_CONTEXT_CLIENT_VERSION, 1,
                EGL10.EGL_NONE
            )

            val surfaceAttribs = intArrayOf(
                EGL10.EGL_WIDTH, 1,
                EGL10.EGL_HEIGHT, 1,
                EGL10.EGL_NONE
            )

            val surface = egl.eglCreatePbufferSurface(display, configs[0], surfaceAttribs)
            val context = egl.eglCreateContext(display, configs[0], EGL10.EGL_NO_CONTEXT, attribList)
            egl.eglMakeCurrent(display, surface, surface, context)

            val vendor = GLES10.glGetString(GL10.GL_VENDOR) ?: "Unknown"
            val renderer = GLES10.glGetString(GL10.GL_RENDERER) ?: "Unknown"
            val glVersion = GLES10.glGetString(GL10.GL_VERSION) ?: "Unknown"

            // Cleanup
            egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)
            egl.eglDestroySurface(display, surface)
            egl.eglDestroyContext(display, context)
            egl.eglTerminate(display)

            GpuInfo(vendor, renderer, glVersion)
        } catch (e: Exception) {
            GpuInfo(
                vendor = "Unknown GPU Vendor (no EGL context)",
                renderer = "Unknown GPU Renderer (no EGL context)",
                version = "Unknown GPU Version (no EGL context)"
            )
        }
    }
}


