package com.example.lablearnandroid

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage

class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GalleryScreen()
                }
            }
        }
    }
}

@Composable
fun GalleryScreen() {
    val context = LocalContext.current
    
    // State สำหรับเก็บรูปภาพ
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    // กำหนด Permission ตามเวอร์ชันของ Android (API 33+ ใช้ READ_MEDIA_IMAGES)
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // 3. Launcher สำหรับรับ Uri รูปภาพจากแกลเลอรี
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    // 2. Launcher สำหรับขอ Permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // ถ้ายอมรับ Permission ให้เปิดแกลเลอรีเพื่อเลือกรูป
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "กรุณาอนุญาตการเข้าถึงไฟล์เพื่อเลือกรูปภาพ", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 5. นำ Uri มาแสดงผลผ่าน Coil ตัว AsyncImage
        imageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 24.dp)
            )
        }

        // 4. ปุ่มเลือกรูปภาพ
        Button(onClick = {
            // เช็คว่าเคยอนุญาต Permission หรือยัง
            val isPermissionGranted = ContextCompat.checkSelfPermission(
                context, 
                permissionToRequest
            ) == PackageManager.PERMISSION_GRANTED
            
            if (isPermissionGranted) {
                // ถ้าอนุญาตแล้ว สั่ง Launcher แกลเลอรีให้ทำงาน
                galleryLauncher.launch("image/*")
            } else {
                // ถ้ายังไม่อนุญาต ให้เรียกสมุด Launcher ขอ Permission เด้งขึ้นมาถาม
                permissionLauncher.launch(permissionToRequest)
            }
        }) {
            Text(text = if (imageUri == null) "เลือกรูปภาพ" else "เปลี่ยนรูปภาพ")
        }
    }
}
