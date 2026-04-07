package com.example.lablearnandroid

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. ViewModel จัดการ State ของ URL
class WebViewModel : ViewModel() {
    private val _urlState = MutableStateFlow("https://www.google.com")
    val urlState: StateFlow<String> = _urlState.asStateFlow()

    fun updateUrl(newUrl: String) {
        // ตรวจสอบและเติม https:// ให้แบบคร่าวๆ หากพิมพมาแค่ชื่อเว็บเฉยๆ
        val finalUrl = if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) {
            newUrl
        } else {
            "https://$newUrl"
        }
        _urlState.value = finalUrl
    }
}

// 2. Composable Screen สำหรับ WebView
@Composable
fun WebViewScreen(viewModel: WebViewModel = viewModel()) {
    // ดึงค่า URL ตัวล่าสุดจาก ViewModel
    val currentUrl by viewModel.urlState.collectAsState()
    
    // State สำหรับกล่องข้อความพิมพ์ URL
    var textInput by remember { mutableStateOf(currentUrl) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 4. สร้าง TextField และปุ่ม Go
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = { Text("URL") }
                )
                
                Button(
                    onClick = { viewModel.updateUrl(textInput) },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Go")
                }
            }

            // การเรียกใช้งานระบบ View ดั้งเดิมบนเครื่อง (Legacy Android) ใน Compose
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    // สิ่งที่จะสร้างขึ้นมาครั้งแรกเพียงครั้งเดียว
                    WebView(context).apply {
                        // 3. ใช้ WebViewClient เพื่อให้หน้าเว็บโหลดอยู่ภายในแอป ไม่เด้งเปิด Chrome ภายนอก
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true // อนุญาต JS ให้เว็บสมัยใหม่ทำงานรูปได้สมบูรณ์
                    }
                },
                update = { webView ->
                    // block นี้จะถูก Recompose / Trigger ทุกครั้งที่ State ที่อ่านด้านใน (currentUrl) มีการเปลี่ยนแปลง
                    webView.loadUrl(currentUrl)
                }
            )
        }
    }
}

class Part6WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                WebViewScreen()
            }
        }
    }
}
