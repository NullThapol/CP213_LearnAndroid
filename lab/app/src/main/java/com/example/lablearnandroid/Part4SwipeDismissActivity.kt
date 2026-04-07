package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

// 1. ViewModel ควบคุม State ของรายการ
class TodoViewModel : ViewModel() {
    // ใช้ mutableStateListOf เพื่อให้ UI อัปเดตเมื่อมีการเพิ่ม/ลบ
    private val _todoItems = mutableStateListOf(
        "✅ Buy Groceries",
        "👨‍💻 Finish Android Lab project",
        "📞 Call parents",
        "📖 Read 10 pages of a book",
        "🏃‍♂️ Exercise for 30 minutes"
    )
    val todoItems: List<String> get() = _todoItems

    fun removeItem(item: String) {
        _todoItems.remove(item)
    }
}

// 2. Composable Screen หลัก
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeDismissScreen(viewModel: TodoViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // ใช้ key = { it } เพื่อให้การจัดการ animation การลบแม่นยำขึ้น
            items(
                items = viewModel.todoItems,
                key = { item -> item }
            ) { item ->
                
                // เก็บสถานะว่า Item นี้ถูกปัดพร้อมลบแล้วหรือยัง จะได้เล่น Animation ถูก
                var isRemoved by remember { mutableStateOf(false) }
                
                // ใช้ rememberSwipeToDismissBoxState (Material 3) ควบคุมการเลื่อน
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                            isRemoved = true // 4. เมื่อปัดสุดไป EndToStart สั่งให้ข้อมูลลบ
                            true
                        } else {
                            false
                        }
                    }
                )

                // เพื่อความลื่นไหล เราอาจจะหน่วงเวลาสักนิดให้ Swipe-off animation เล่นจบ แล้วค่อยลบจาก ViewModel
                LaunchedEffect(isRemoved) {
                    if (isRemoved) {
                        delay(200)
                        viewModel.removeItem(item)
                    }
                }

                AnimatedVisibility(
                    visible = !isRemoved,
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 200)) + fadeOut()
                ) {
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            // 3. ปัดไปเจอสีพื้นหลังเปลี่ยนเป็นสีแดง และไอคอนถังขยะ
                            val direction = dismissState.dismissDirection
                            
                            val backgroundColor by animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336) // Red
                                    else -> Color.Transparent
                                },
                                label = "ColorAnimation"
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp) // จัดให้พอดีกับการจัดขอบ Card
                                    .background(backgroundColor, shape = CardDefaults.shape)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                // โชว์ถังขยะสีขาวเมื่อทิศทางการปัดเป็นแบบ EndToStart (ขวามาซ้าย)
                                if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Icon",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        enableDismissFromStartToEnd = false,    // ปิดการปัด ซ้าย -> ขวา
                        enableDismissFromEndToStart = true      // เปิดแค่ ปัด ขวา -> ซ้าย
                    ) {
                        // ตัว Item Card โชว์ข้อความปกติ
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                text = item,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

class Part4SwipeDismissActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                SwipeDismissScreen()
            }
        }
    }
}
