package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Composable Component: กล่องสมมติสีเทาแทนรูปโปรไฟล์
@Composable
fun ProfilePicture(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text("Profile Pic", color = Color.DarkGray, fontWeight = FontWeight.Bold)
    }
}

// Composable Component: Text อธิบายข้อมูลส่วนตัว
@Composable
fun ProfileInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Android Learner", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "นี่คือตัวอย่างข้อมูลส่วนตัวสำหรับการทำ Adaptive Layout ในโหมดหน้าจอแนวตั้ง (Portrait) คุณจะเห็นข้อความนี้เรียงต่อลงมาจากรูป แต่ถ้าหมุนมือถือเป็นแนวนอน (Landscape) เมื่อไหร่ ข้อความและรูปภาพจะสลับมาอยู่ข้างกันอย่างสวยงามทันที!",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}

// 1. หน้าหลักที่ทำ Adaptive Layout รับผิดชอบตรวจสอบหน้าจอ
@Composable
fun AdaptiveProfileScreen() {
    Scaffold { paddingValues ->
        // ใช้ BoxWithConstraints เพื่อตรวจสอบพื้นที่ขนาดหน้าจอที่ใช้งานได้ (Available Space) แบบสดๆ
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // เช็คความกว้างหน้าจอ (maxWidth) ที่แอนดรอยด์ยิงมาให้ใน Scope
            if (maxWidth < 600.dp) {
                // 2. < 600.dp (แนวตั้ง/มือถือทั่วไป): แสดงผลแบบ Column (รูปบน ข้อมูลล่าง)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfilePicture()
                    Spacer(modifier = Modifier.height(24.dp))
                    ProfileInfo(modifier = Modifier.fillMaxWidth())
                }
            } else {
                // 3. > 600.dp (แนวนอน/แท็บเล็ต): แสดงผลแบบ Row (รูปซ้าย ข้อมูลขวา)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top // หรือ Alignment.CenterVertically
                ) {
                    ProfilePicture()
                    Spacer(modifier = Modifier.width(32.dp))
                    // กำหนดน้ำหนัก (weight) ให้ข้อมูลยืดตัวเต็มกล่องยาวที่เหลืออยู่ เพื่อไม่ให้ล้นจอ
                    ProfileInfo(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

class Part8AdaptiveLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AdaptiveProfileScreen()
            }
        }
    }
}
