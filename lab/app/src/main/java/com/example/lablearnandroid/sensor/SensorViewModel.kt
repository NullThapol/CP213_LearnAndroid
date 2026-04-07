package com.example.lablearnandroid.sensor

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SensorViewModel(application: Application) : AndroidViewModel(application) {
    
    // สร้าง Instance ของ Tracker ใน ViewModel เพื่อผูก LifeCycle
    private val sensorTracker = SensorTracker(application)
    private val locationTracker = LocationTracker(application)

    // ตัวแปรสถานะ MutableStateFlow เป็นเหมือนท่อน้ำเพื่อกักเก็บและกระจายข้อมูล 
    private val _accelerometerData = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    val accelerometerData: StateFlow<FloatArray> = _accelerometerData.asStateFlow()

    private val _locationData = MutableStateFlow<Location?>(null)
    val locationData: StateFlow<Location?> = _locationData.asStateFlow()

    init {
        // ผูก Callback จาก Tracker เข้ากับ ViewModel
        // เมื่อได้รับค่าใหม่จากขั้นตอนที่ 1 ให้ส่งค่านั้นเข้ามาอัปเดตใน StateFlow (ขั้นตอนที่ 2)
        sensorTracker.onSensorChanged = { newValue ->
            _accelerometerData.value = newValue
        }
        
        locationTracker.onLocationChanged = { newLocation ->
            _locationData.value = newLocation
        }
    }

    fun startSensorTracking() {
        sensorTracker.startTracking()
    }

    fun stopSensorTracking() {
        sensorTracker.stopTracking()
    }

    fun startLocationTracking() {
        locationTracker.startTracking()
    }

    fun stopLocationTracking() {
        locationTracker.stopTracking()
    }

    // เมื่อ ComponentActivity ถูกทำลาย ระบบจะเรียก onCleared เพื่อป้องกันแอปแครช/Memory Leak
    override fun onCleared() {
        super.onCleared()
        sensorTracker.stopTracking()
        locationTracker.stopTracking()
    }
}
