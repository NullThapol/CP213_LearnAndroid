package com.example.lablearnandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lablearnandroid.sensor.SensorViewModel

class SensorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SensorScreen()
                }
            }
        }
    }
}

@Composable
fun SensorScreen(viewModel: SensorViewModel = viewModel()) {
    val context = LocalContext.current
    
    // 3. ใช้ collectAsState() เพื่อแปลง StateFlow ให้กลายเป็น Compose State ทันทีที่เซนเซอร์ขยับ ➡️ Compose รับรู้
    val accelData by viewModel.accelerometerData.collectAsState()
    val locationData by viewModel.locationData.collectAsState()

    var hasLocationPermission by remember { mutableStateOf(false) }
    var isTrackingLocation by remember { mutableStateOf(false) }

    // Launcher สำหรับขอ Permission เข้าถึงตำแหน่ง (GPS)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = isGranted
        if (isGranted) {
            isTrackingLocation = true
            viewModel.startLocationTracking()
        } else {
            Toast.makeText(context, "กรุณาอนุญาตตำแหน่งเพื่อดูพิกัด GPS", Toast.LENGTH_SHORT).show()
        }
    }

    // จัดการ Lifecycle การทำงาน เปิดและปิดเซนเซอร์/GPS เมื่อหน้าจอ Active
    DisposableEffect(Unit) {
        viewModel.startSensorTracking()
        
        // เช็คว่าเคยมี Permission หรือยัง
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            hasLocationPermission = true
            // หมายเหตุ: ไม่สั่ง viewModel.startLocationTracking() อัตโนมัติ รอผู้ใช้กดปุ่ม Start
        }
        
        onDispose {
            viewModel.stopSensorTracking()
            viewModel.stopLocationTracking()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sensor & MVVM Architecture", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(32.dp))

        // นำ State ไปแสดงผล 
        Text(text = "Accelerometer Sensor", style = MaterialTheme.typography.titleMedium)
        Text(text = "X: ${String.format("%.2f", accelData[0])}")
        Text(text = "Y: ${String.format("%.2f", accelData[1])}")
        Text(text = "Z: ${String.format("%.2f", accelData[2])}")
        
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "GPS Location:", style = MaterialTheme.typography.titleMedium)
        if (locationData != null) {
            Text(text = "Lat: ${locationData!!.latitude}")
            Text(text = "Lng: ${locationData!!.longitude}")
        } else {
            Text(text = "กำลังรอพิกัด หรืออาจยังไม่อนุญาต...")
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (!hasLocationPermission) {
                Button(onClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }) {
                    Text(text = "ขอสิทธิ์เข้าถึง GPS")
                }
            } else {
                Button(
                    onClick = {
                        isTrackingLocation = true
                        viewModel.startLocationTracking()
                    },
                    enabled = !isTrackingLocation
                ) {
                    Text("Start Tracking Location")
                }
                
                Button(
                    onClick = {
                        isTrackingLocation = false
                        viewModel.stopLocationTracking()
                    },
                    enabled = isTrackingLocation
                ) {
                    Text("Stop Tracking")
                }
            }
        }
    }
}
