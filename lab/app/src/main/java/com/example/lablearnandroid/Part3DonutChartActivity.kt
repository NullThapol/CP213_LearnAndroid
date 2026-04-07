package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

class Part3DonutChartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        DonutChart(
                            values = listOf(30f, 40f, 30f),
                            colors = listOf(Color(0xFFFF9800), Color(0xFF4CAF50), Color(0xFF2196F3)),
                            modifier = Modifier.size(250.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DonutChart(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    // เก็บ State ขององศาที่จะวาด (ตั้งแต่ 0 ถึง 360)
    val sweepAngleTracker = remember { Animatable(0f) }

    // เริ่ม Animation เมื่อ Composable ถูกแสดงขึ้นมาครั้งแรก
    LaunchedEffect(Unit) {
        sweepAngleTracker.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 1500, // ใช้เวลา 1.5 วินาที
                easing = FastOutSlowInEasing
            )
        )
    }

    // Canvas สำหรับวาดกราฟิกอิสระ
    Canvas(modifier = modifier) {
        val totalValue = values.sum()
        var startAngle = -90f // เริ่มต้นวาดจากจุดบนสุด (12 นาฬิกา)
        var drawnAngle = 0f   // มุมที่วาดไปแล้วทั้งหมด

        values.forEachIndexed { index, value ->
            // คำนวณองศาเต็มของแต่ละส่วนแบ่ง
            val sliceAngle = (value / totalValue) * 360f
            
            // คำนวณขีดจำกัดสูงสุดขององศาที่อนุญาตให้วาดได้ตาม Animation ณ ปัจจุบัน
            val maxAllowedSweep = sweepAngleTracker.value - drawnAngle
            
            // มุมที่จะนำไปใช้วาดจริง (กั้นไม่ให้วาดเกิน sliceAngle ของตัวมันเอง)
            val sweepAngle = maxAllowedSweep.coerceIn(0f, sliceAngle)
            
            if (sweepAngle > 0f) {
                drawArc(
                    color = colors.getOrElse(index) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false, // false พร้อมกับ Stroke จะทำให้ตรงกลางกลวง (โดนัท)
                    style = Stroke(
                        width = 50.dp.toPx(), // ความหนาของเส้น
                        cap = StrokeCap.Butt  // ลักษณะปลายเส้นที่ตัดตรง
                    )
                )
            }
            
            startAngle += sliceAngle // เปลี่ยนจุดจบของชิ้นนี้ เป็นจุดเริ่มต้นชิ้นถัดไป
            drawnAngle += sliceAngle // จำไว้ว่าวาดวงกลมมุมเต็มไปแล้วกี่องศา
        }
    }
}
