package com.deelib.perfScout.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deelib.perfScout.*
import com.deelib.perfScout.api.PerfResult
import com.deelib.perfScout.sample.ui.theme.PerfScoutTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerfScoutMetrics.startupTime.recordActivityOnCreate(this)
        enableEdgeToEdge()
        setContent {
            PerfScoutTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PerfScoutDashboard()
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            PerfScoutMetrics.startupTime.recordFirstDraw(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfScoutDashboard() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "PerfScout Demo",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SectionHeader("Device Performance")

        MetricFeature("CPU Info") {
            scope.launch {
                it.value = formatResult(PerfScoutMetrics.cpu.getAsync())
            }
        }

        MetricFeature("RAM Info") {
            it.value = formatResult(PerfScoutMetrics.ram.get(context))
        }

        MetricFeature("Thermal Info") {
            it.value = formatResult(PerfScoutMetrics.thermal.get(context))
        }

        MetricFeature("Battery Info") {
            it.value = formatResult(PerfScoutMetrics.battery.get(context))
        }

        MetricFeature("Device Info") {
            it.value = formatResult(PerfScoutMetrics.device.get(context))
        }

        SectionHeader("App Performance")

        MetricFeature("App Memory Info") {
            it.value = formatResult(PerfScoutMetrics.appMemory.get())
        }

        MetricFeature("App Uptime Info") {
            it.value = formatResult(PerfScoutMetrics.appUptime.get(context))
        }

        MetricFeature("GC Stats Info") {
            it.value = formatResult(PerfScoutMetrics.gcStats.get())
        }

        MetricFeature("Frame Rendering Info(3s)") {
            scope.launch {
                it.value = formatResult(PerfScoutMetrics.getFrameRenderingInfoAsync(3000))
            }
        }

        SectionHeader("Network")

        MetricFeature("Network Usage Info") {
            it.value = formatResult(PerfScoutMetrics.network.get(context))
        }

        SectionHeader("System Info")

        MetricFeature("Storage Usage Info") {
            it.value = formatResult(PerfScoutMetrics.storage.get(context))
        }

        MetricFeature("Thread/Process Info") {
            it.value = formatResult(PerfScoutMetrics.threadProcess.get(context))
        }

        SectionHeader("Advanced Features")

        MetricFeature("Startup Time Info") {
            it.value = formatResult(PerfScoutMetrics.startupTime.get(context))
        }

        MetricFeature("Media Quality Recommendation") {
            it.value = formatResult(PerfScoutMetrics.mediaQuality.get(context))
        }

        // Showcase Crash and ANR
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Crash & ANR Demo", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { throw RuntimeException("Deliberate crash for PerfScout demo!") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text("Trigger Crash (for demo)")
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        // Block main thread for 10 seconds to trigger ANR
                        val start = System.currentTimeMillis()
                        while (System.currentTimeMillis() - start < 10_000) {
                            // Busy wait
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text("Trigger ANR (10s block)")
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "WARNING: These buttons will crash or freeze the app to demonstrate PerfScout's monitoring features.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun MetricFeature(title: String, onClick: (MutableState<String>) -> Unit) {
    val result = remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.width(8.dp))
            Button(onClick = { onClick(result) }) {
                Text("Get $title")
            }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
    }

}

fun <T> formatResult(result: PerfResult<T>): String = when (result) {
    is PerfResult.Success -> result.info.toString()
    is PerfResult.Error -> "Error: ${result.message}"
}


