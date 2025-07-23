package io.github.dee2604.perfScout.strictmode

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.os.StrictMode.OnThreadViolationListener
import android.os.StrictMode.ThreadPolicy
import androidx.annotation.RequiresApi
import java.util.concurrent.Executor

object StrictModeMonitorProvider {
    private var listener: ((StrictModeViolationInfo) -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.P)
    fun enable(
        detectAll: Boolean = true,
        penaltyLog: Boolean = true,
        penaltyCallback: ((StrictModeViolationInfo) -> Unit)? = null
    ) {
        listener = penaltyCallback

        val builder = ThreadPolicy.Builder()
        if (detectAll) builder.detectAll()
        if (penaltyLog) builder.penaltyLog()

        val mainExecutor = Executor { command ->
            Handler(Looper.getMainLooper()).post(command)
        }

        builder.penaltyListener(mainExecutor, OnThreadViolationListener { violation ->
            val info = StrictModeViolationInfo(
                policy = "ThreadPolicy",
                message = violation.toString(),
                stackTrace = Thread.currentThread().stackTrace.joinToString("\n"),
                timestamp = System.currentTimeMillis()
            )
            listener?.invoke(info)
        })

        StrictMode.setThreadPolicy(builder.build())
    }
}