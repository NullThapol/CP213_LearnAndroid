package com.example.lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. ViewModel (Mock ข้อมูลตามตัวอักษร A-Z และมี State isLoading)
class ContactListViewModel : ViewModel() {
    private val _contacts = MutableStateFlow<List<String>>(emptyList())
    val contacts: StateFlow<List<String>> = _contacts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPage = 0
    private val alphabets = ('A'..'Z').toList()

    init {
        loadMoreContacts() // โหลดข้อมูลชุดแรก (ตัว A) ขึ้นมาก่อน
    }

    fun loadMoreContacts() {
        // ป้องกันไม่ให้โหลดซ้ำซ้อนถ้ากำลังโหลดอยู่ หรือข้อมูลครบ A-Z แล้ว
        if (_isLoading.value || currentPage >= alphabets.size) return

        _isLoading.value = true
        
        // จำลองการโหลดข้อมูลด้วย Coroutines
        viewModelScope.launch {
            delay(2000) // 3. หน่วงเวลา 2 วินาทีเพื่อจำลองการเรียก API
            
            val currentLetter = alphabets[currentPage]
            // สร้างรายชื่อสมมติตั้งแต่ 1-10 ของตัวอักษรนั้นๆ
            val newContacts = (1..10).map { "$currentLetter - Contact Name $it" }
            
            _contacts.value = _contacts.value + newContacts
            currentPage++
            _isLoading.value = false
        }
    }
}

// 2. Composable Screen สำหรับ Contact List
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(viewModel: ContactListViewModel = viewModel()) {
    // Collect State จาก ViewModel
    val contacts by viewModel.contacts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // จัดกลุ่มของรายชื่อตามตัวอักษรเพื่อใช้ทำ Sticky Header
    val groupedContacts = contacts.groupBy { it.first().uppercaseChar() }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            groupedContacts.forEach { (initial, contactsInGroup) ->
                // Sticky Header 
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = initial.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // สมาชิก Contact Items ในกลุ่มนั้นๆ
                items(contactsInGroup) { contact ->
                    Text(
                        text = contact,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    HorizontalDivider() // เส้นคั่น
                }
            }

            // 4. แสดงผลตอนที่กำลังโหลดเพิ่มเติมเมื่อเลื่อนมาสุด
            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (contacts.isNotEmpty()) {
                    LaunchedEffect(contacts.size) {
                        viewModel.loadMoreContacts()
                    }
                }
            }
        }
    }
}

class Part2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                ContactListScreen()
            }
        }
    }
}