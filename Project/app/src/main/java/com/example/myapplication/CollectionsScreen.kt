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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer

@Composable
fun CollectionsScreen(items: List<CollectionItem>, modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf<CollectionItem?>(null) }

    if (selectedItem != null) {
        val fortune = getCardFortune(selectedItem!!.name)
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            containerColor = when (fortune.luckLevel) {
                "Legendary" -> Color(0xFF121212)
                "Great Luck" -> when (fortune.name) {
                    "The Gilded Heart" -> Color(0xFFFFEBEE)
                    "The Merchant’s Greed" -> Color(0xFFE8F5E9)
                    "The Scholar’s Peak" -> Color(0xFFE3F2FD)
                    else -> MaterialTheme.colorScheme.surface
                }
                else -> MaterialTheme.colorScheme.surface
            },
            title = if (fortune.luckLevel == "Normal Luck" || fortune.luckLevel == "Bad Luck") {
                { Text(text = "Card Details", fontWeight = FontWeight.Bold) }
            } else null,
            text = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Legendary Style (Swapped Pattern: Now Triple Layer Runic Frame in Gold)
                    if (fortune.luckLevel == "Legendary") {
                        Box(modifier = Modifier.matchParentSize().background(Color(0xFF0A0A0A))) // Original Black
                        Box(modifier = Modifier.matchParentSize().border(6.dp, Color(0xFFD4AF37).copy(alpha = 0.4f))) // Outer Gold Glow
                        Box(modifier = Modifier.matchParentSize().padding(4.dp).border(2.dp, Color(0xFFD4AF37))) // Main Gold Border
                        Box(modifier = Modifier.matchParentSize().padding(10.dp).border(2.dp, Color(0xFFD4AF37).copy(alpha = 0.6f), androidx.compose.foundation.shape.CutCornerShape(12.dp))) // Inner Runic Accent
                    }

                    // Special Backgrounds for Great Luck
                    if (fortune.luckLevel == "Great Luck") {
                        when (fortune.name) {
                            "The Gilded Heart" -> {
                                Box(modifier = Modifier.matchParentSize().background(androidx.compose.ui.graphics.Brush.radialGradient(colors = listOf(Color(0xFFFFEBEE), Color(0xFFFF69B4).copy(alpha = 0.2f)))))
                                Box(modifier = Modifier.matchParentSize().border(8.dp, Color(0xFFFF69B4).copy(alpha = 0.1f))) // Outer Glow
                                Box(modifier = Modifier.matchParentSize().padding(4.dp).border(2.dp, Color(0xFFFF69B4))) // Main Frame
                                Box(modifier = Modifier.matchParentSize().padding(10.dp).border(1.dp, Color(0xFFFF69B4).copy(alpha = 0.5f))) // Inner Accent
                            }
                            "The Merchant’s Greed" -> {
                                Box(modifier = Modifier.matchParentSize().background(androidx.compose.ui.graphics.Brush.linearGradient(colors = listOf(Color(0xFFE8F5E9), Color(0xFFF1F8E9)))))
                                Box(modifier = Modifier.matchParentSize().border(6.dp, Color(0xFFD4AF37).copy(alpha = 0.4f))) // Outer Gold
                                Box(modifier = Modifier.matchParentSize().padding(6.dp).border(2.dp, Color(0xFF4CAF50))) // Emerald Line
                                Box(modifier = Modifier.matchParentSize().padding(12.dp).border(1.dp, Color(0xFFD4AF37))) // Inner Gold
                            }
                            "The Scholar’s Peak" -> {
                                // Swapped Pattern: Now Thin Double Border in Blue
                                Box(modifier = Modifier.matchParentSize().background(Color(0xFFE3F2FD))) // Original Blue
                                Box(modifier = Modifier.matchParentSize().border(4.dp, Color(0xFF2196F3).copy(alpha = 0.8f))) // Outer Blue Line
                                Box(modifier = Modifier.matchParentSize().padding(6.dp).border(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))) // Inner Thin Blue Line
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, 
                        modifier = Modifier.fillMaxWidth().padding(if (fortune.luckLevel == "Legendary") 20.dp else 16.dp)
                    ) {
                        // Title for Special Cards
                        if (fortune.luckLevel == "Legendary" || fortune.luckLevel == "Great Luck") {
                            Text(
                                text = if (fortune.luckLevel == "Legendary") "LEGENDARY ITEM" else "CARD DETAILS",
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color.copy(alpha = 0.8f),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                        Text(
                            text = fortune.name.uppercase(),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color,
                                letterSpacing = 2.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "--- ${fortune.luckLevel.uppercase()} ---",
                            color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37).copy(alpha = 0.7f) else fortune.color.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Divider(color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color.copy(alpha = 0.3f), thickness = 1.dp)

                        Text(
                            text = fortune.description,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = if (fortune.luckLevel == "Legendary") Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                            Text("PROS: ", fontWeight = FontWeight.Bold, color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else Color(0xFF4CAF50), fontSize = 12.sp)
                            Text(fortune.pro, style = MaterialTheme.typography.bodyMedium, color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface)
                        }
                        if (fortune.con != "-") {
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                                Text("CONS: ", fontWeight = FontWeight.Bold, color = Color(0xFFF44336), fontSize = 12.sp)
                                Text(fortune.con, style = MaterialTheme.typography.bodyMedium, color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Text(
                            text = "RECEIVED ON: ${selectedItem!!.date}",
                            fontSize = 10.sp,
                            color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 16.dp),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Obtained in ${selectedItem!!.pullCount} pulls",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37).copy(alpha = 0.9f) else fortune.color.copy(alpha = 0.7f),
                            letterSpacing = 1.sp
                        )
                        if (fortune.luckLevel == "Legendary" || fortune.luckLevel == "Great Luck") {
                            Button(
                                onClick = { selectedItem = null },
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color),
                                modifier = Modifier.padding(top = 16.dp).align(Alignment.End)
                            ) {
                                Text("CLOSE", color = if (fortune.luckLevel == "Legendary") Color.Black else Color.White)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (fortune.luckLevel == "Normal Luck" || fortune.luckLevel == "Bad Luck") {
                    Button(
                        onClick = { selectedItem = null }
                    ) {
                        Text("CLOSE")
                    }
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
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { index, item ->
                val fortune = getCardFortune(item.name)
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { selectedItem = item },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = when (fortune.luckLevel) {
                        "Legendary" -> Color(0xFF121212)
                        "Great Luck" -> when (fortune.name) {
                            "The Gilded Heart" -> Color(0xFFFFEBEE)
                            "The Merchant’s Greed" -> Color(0xFFE8F5E9)
                            "The Scholar’s Peak" -> Color(0xFFE3F2FD)
                            else -> MaterialTheme.colorScheme.surface
                        }
                        else -> MaterialTheme.colorScheme.surface
                    }),
                    border = androidx.compose.foundation.BorderStroke(2.dp, if (fortune.luckLevel == "Legendary" || fortune.luckLevel == "Great Luck") Color.Transparent else fortune.color.copy(alpha = 0.5f)),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Special Backgrounds for Great Luck in Grid
                        if (fortune.luckLevel == "Great Luck") {
                            when (fortune.name) {
                                "The Gilded Heart" -> Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Brush.radialGradient(colors = listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2)))))
                                "The Merchant’s Greed" -> Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Brush.linearGradient(colors = listOf(Color(0xFFE8F5E9), Color(0xFFF1F8E9)))))
                                "The Scholar’s Peak" -> Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFE1F5FE)))))
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                        Text(
                            text = fortune.luckLevel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Card ${index + 1}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = fortune.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }

                        Text(
                            text = item.date,
                            fontSize = 10.sp,
                            color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
    }
}

