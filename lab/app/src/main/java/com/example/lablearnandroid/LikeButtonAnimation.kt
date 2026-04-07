package com.example.lablearnandroid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LikeButtonAnimation() {
    // เก็บ State พื้นฐานว่าปุ่มถูกกด Like แล้วหรือยัง
    var isLiked by remember { mutableStateOf(false) }

    // 1. Scale animation เมื่อกดปุ่ม ขนาดจะขยายขึ้นและเด้งกลับด้วย spring animation
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    // 2. Color animation เปลี่ยนสีพื้นหลังจาก สีเทา เป็น สีชมพู
    val backgroundColor by animateColorAsState(
        targetValue = if (isLiked) Color(0xFFE91E63) else Color.LightGray,
        label = "background_color"
    )

    Button(
        onClick = { isLiked = !isLiked },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier.scale(scale)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 3. AnimatedVisibility แสดง Icon รูปหัวใจ โผล่ขึ้นมาเมื่อสถานะเป็น 'Liked'
            AnimatedVisibility(visible = isLiked) {
                Row {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Heart Icon",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            
            Text(
                text = if (isLiked) "Liked" else "Like",
                color = if (isLiked) Color.White else Color.Black
            )
        }
    }
}
