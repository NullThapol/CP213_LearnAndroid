package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// 1. ViewModel ทำหน้าที่จัดเตรียม One-time event
class SideEffectViewModel : ViewModel() {
    // ใช้ Channel ซึ่งเหมาะกับการทำ One-time Event 
    // ดีกว่า StateFlow ตรงที่พอดึงข้อมูล (consume) ไปแล้ว ข้อความจะไม่ติดค้างเวลาหมุนจอ 
    private val _errorChannel = Channel<String>()
    val errorFlow = _errorChannel.receiveAsFlow()

    fun triggerError() {
        viewModelScope.launch {
            // จำลองว่าเราไปเรียก API แล้วพัง หรือเกิด Error บางอย่างในระบบ
            val timeRandom = System.currentTimeMillis() % 1000
            _errorChannel.send("Network connection failed! (Error Code: $timeRandom)")
        }
    }
}

// 2. Composable Screen หลัก
@Composable
fun SideEffectScreen(viewModel: SideEffectViewModel = viewModel()) {
    // State สำหรับควบคุม Snackbar ให้โผล่และหายไป
    val snackbarHostState = remember { SnackbarHostState() }

    // 3. Side Effect ขั้นเทพ: LaunchedEffect จะทำงานใน Coroutine Scope
    // เราสั่งให้ผูกติดกับหน้าจอนี้ไปเลย และรอรับ Flow (Observe ค่า Error)
    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            // เมื่อได้รับ event Error โผล่มา ให้เรียกคำสั่ง showSnackbar ทันที
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "Dismiss"
            )
        }
    }

    Scaffold(
        // ผูก snackbarHostState เข้ากับ Scaffold เพื่อวาด UI ของ Snackbar ด้านล่างจอ
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // 4. ปุ่มเพื่อคลิกจำลองเหตุการณ์
            Button(onClick = { viewModel.triggerError() }) {
                Text("Trigger Error")
            }
        }
    }
}

class Part5SideEffectsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                SideEffectScreen()
            }
        }
    }
}
