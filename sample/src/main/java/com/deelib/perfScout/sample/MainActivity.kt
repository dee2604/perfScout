package com.deelib.perfScout.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deelib.perfScout.PerfScout
import com.deelib.perfScout.sample.ui.theme.PerfScoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Record startup time for PerfScout
        PerfScout.recordActivityOnCreate(this)
        
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
            // Record first draw time when window gets focus (after first draw)
            PerfScout.recordFirstDraw(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfScoutDashboard() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
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
        
        // Device Performance Section
        Text("Device Performance", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
        FeatureSection("CPU Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getCpuInfo().toString()
            }) { Text("Get CPU Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("RAM Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getRamInfo(context).toString()
            }) { Text("Get RAM Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("GPU Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getGpuInfo().toString()
            }) { Text("Get GPU Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("Thermal Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getThermalInfo(context).toString()
            }) { Text("Get Thermal Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("Battery Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getBatteryInfo(context).toString()
            }) { Text("Get Battery Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("Device Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getDeviceInfo(context).toString()
            }) { Text("Get Device Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        
        // App Performance Section
        Text("App Performance", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
        FeatureSection("App Memory Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getAppMemoryInfo().toString()
            }) { Text("Get App Memory Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("App CPU Usage") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                PerfScout.getAppCpuUsageInfo({ cpuInfo ->
                    result.value = cpuInfo.toString()
                }, 1000)
            }) { Text("Get App CPU Usage (1s)") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("App Uptime Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getAppUptimeInfo(context).toString()
            }) { Text("Get App Uptime Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("GC Stats Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getGcStatsInfo().toString()
            }) { Text("Get GC Stats Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("Frame Rendering Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                PerfScout.getFrameRenderingInfo(3000) { frameInfo ->
                    result.value = frameInfo.toString()
                }
            }) { Text("Get Frame Rendering Info (3s)") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        
        // Network Section
        Text("Network", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
        FeatureSection("Network Usage Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getNetworkUsageInfo(context).toString()
            }) { Text("Get Network Usage Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("Network Quality Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getNetworkQualityInfo(context).toString()
            }) { Text("Get Network Quality Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }

        
        // System Info Section
        Text("System Info", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
        FeatureSection("Storage Usage Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getStorageUsageInfo(context).toString()
            }) { Text("Get Storage Usage Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("Thread/Process Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getThreadProcessInfo(context).toString()
            }) { Text("Get Thread/Process Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }


        
        // Advanced Features Section
        Text("Advanced Features", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))

        FeatureSection("Startup Time Info") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                result.value = PerfScout.getStartupTimeInfoForPackage(context.packageName).toString()
            }) { Text("Get Startup Time Info") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }
        FeatureSection("Media Quality Recommendation") {
            val result = remember { mutableStateOf("") }
            Button(onClick = {
                val quality = PerfScout.getMediaQualityRecommendation(context)
                val analysis = PerfScout.getMediaQualityAnalysis(context)
                result.value = "Recommended Quality: $quality\nAnalysis: $analysis"
            }) { Text("Get Media Quality Recommendation") }
            if (result.value.isNotEmpty()) Text(result.value, Modifier.padding(top = 8.dp))
        }


    }
}

@Composable
fun FeatureSection(title: String, content: @Composable () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
