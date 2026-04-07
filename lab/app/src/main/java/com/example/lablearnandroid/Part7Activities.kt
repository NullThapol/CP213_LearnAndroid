package com.example.lablearnandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat

class Part7MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("This is MainActivity", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                        
                        Button(onClick = { openDetailActivity() }) {
                            Text("Open DetailActivity (Slide Up)")
                        }
                    }
                }
            }
        }
    }

    private fun openDetailActivity() {
        // 1. เปิด DetailActivity ผ่าน Intent พร้อมส่ง String ไปรอก่อน
        val intent = Intent(this, Part7DetailActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "Hello from MainActivity!")
        }
        
        // 2. ขยับจาก Transition พื้นฐาน ดึงไฟล์ Animation ที่เราเขียนไว้มาใช้
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_up, // หน้า Detail เคลื่อนตัวขึ้นมา
            R.anim.stay         // ตรึงหน้า Main ให้อยู่เฉยๆ ตอนโดนทับ
        )
        
        // สั่งเปิดพร้อมพก option ที่ใส่แอนิเมชันไปทำงานด้วย
        startActivity(intent, options.toBundle())
    }
}

class Part7DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // รับค่า String จาก Intent
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "No Message"

        setContent {
            MaterialTheme {
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("This is Detail Activity", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.titleLarge)
                        Text("Received message: $message", modifier = Modifier.padding(bottom = 16.dp))
                        
                        // 3. ปุ่มปิด
                        Button(onClick = { closeActivity() }) {
                            Text("Close (Slide Down)")
                        }
                    }
                }
            }
        }
    }

    private fun closeActivity() {
        // สั่งปิดหน้าจอ
        finish()
    }

    // Override การทำงานในจังหวะที่จอกำลังจะถูกทำลาย
    override fun finish() {
        super.finish()
        // สั่งเคลียร์ Animation ของขาออก ให้รูดมันหล่นกลับไปข้างล่าง
        overridePendingTransition(
            R.anim.stay,           // พอมันหลุดออกแล้วหน้า Main ขึ้น ให้ Main รอโผล่เฉยๆ
            R.anim.slide_out_down  // DetailActivity เลื่อนหล่นลงล่าง
        )
    }
}
