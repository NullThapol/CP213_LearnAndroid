package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Divider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color

@Composable
fun CollectionsScreen(items: List<CollectionItem>, modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf<CollectionItem?>(null) }

    if (selectedItem != null) {
        val fortune = getCardFortune(selectedItem!!.name)
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            title = {
                Text(
                    text = "Fortune Detail",
                    style = MaterialTheme.typography.titleLarge,
                    color = fortune.color,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = fortune.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = fortune.color,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "[ ${fortune.luckLevel} ]",
                        fontWeight = FontWeight.Bold,
                        color = fortune.color.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Divider(color = fortune.color.copy(alpha = 0.3f))
                    
                    Text(
                        text = fortune.description,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        Text("Pros: ", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                        Text(fortune.pro, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (fortune.con != "-") {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                            Text("Cons: ", fontWeight = FontWeight.Bold, color = Color(0xFFF44336))
                            Text(fortune.con, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Text(
                        text = "Received on: ${selectedItem!!.date}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedItem = null },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = fortune.color)
                ) {
                    Text("Close", color = Color.White)
                }
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Your Collection",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // ปรับเป็น 2 คอลัมน์เพื่อให้การ์ดใหญ่ขึ้น
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { index, item -> // ใช้ itemsIndexed เพื่อเอาลำดับการ Fav
                val fortune = getCardFortune(item.name)
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { selectedItem = item },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, fortune.color.copy(alpha = 0.5f)),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = fortune.luckLevel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = fortune.color,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Card ${index + 1}", // แสดงลำดับตามที่กด Fav มา
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = fortune.color,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = fortune.name, // ยังคงโชว์ฉายาการ์ดเพื่อให้รู้ว่าเป็นใบไหน
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }

                        Text(
                            text = item.date,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}
