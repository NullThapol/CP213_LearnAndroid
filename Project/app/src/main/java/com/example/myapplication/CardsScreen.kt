package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Divider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.rotate

@Composable
fun CardsScreen(
    card: String?, 
    pullId: Int = 0,
    pullCount: Int = 0,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorited by remember(pullId) { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (card != null) {
            val fortune = getCardFortune(card)
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth(0.9f).height(480.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp),
                    color = when (fortune.luckLevel) {
                        "Legendary" -> Color(0xFF0A0A0A)
                        "Great Luck" -> when (fortune.name) {
                            "The Gilded Heart" -> Color(0xFFFFEBEE)
                            "The Merchant’s Greed" -> Color(0xFFE8F5E9)
                            "The Scholar’s Peak" -> Color(0xFFE3F2FD)
                            else -> MaterialTheme.colorScheme.surface
                        }
                        else -> MaterialTheme.colorScheme.surface
                    },
                    tonalElevation = 4.dp,
                    shadowElevation = 12.dp,
                    border = if (fortune.luckLevel == "Legendary" || fortune.luckLevel == "Great Luck") null else androidx.compose.foundation.BorderStroke(2.dp, fortune.color.copy(alpha = 0.5f))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Legendary Style (Swapped Pattern: Now Triple Layer Runic Frame in Gold)
                        if (fortune.luckLevel == "Legendary") {
                            Box(modifier = Modifier.matchParentSize().background(Color(0xFF0A0A0A))) // Original Black
                            Box(modifier = Modifier.matchParentSize().border(6.dp, Color(0xFFD4AF37).copy(alpha = 0.4f))) // Outer Gold Glow
                            Box(modifier = Modifier.matchParentSize().padding(4.dp).border(2.dp, Color(0xFFD4AF37))) // Main Gold Border
                            Box(modifier = Modifier.matchParentSize().padding(10.dp).border(2.dp, Color(0xFFD4AF37).copy(alpha = 0.6f), androidx.compose.foundation.shape.CutCornerShape(12.dp))) // Inner Runic Accent
                        }

                        // Unique Styles for Great Luck Cards
                        if (fortune.luckLevel == "Great Luck") {
                            when (fortune.name) {
                                "The Gilded Heart" -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                androidx.compose.ui.graphics.Brush.radialGradient(
                                                    colors = listOf(Color(0xFFFFEBEE), Color(0xFFFF69B4).copy(alpha = 0.3f))
                                                )
                                            )
                                    )
                                    // Complex Frame
                                    Box(modifier = Modifier.fillMaxSize().border(8.dp, Color(0xFFFF69B4).copy(alpha = 0.2f))) // Outer soft glow
                                    Box(modifier = Modifier.fillMaxSize().padding(4.dp).border(2.dp, Color(0xFFFF69B4))) // Main border
                                    Box(modifier = Modifier.fillMaxSize().padding(10.dp).border(1.dp, Color(0xFFFF69B4).copy(alpha = 0.5f))) // Inner accent
                                    // Corner accents (simulated)
                                    Box(modifier = Modifier.fillMaxSize().padding(2.dp).border(4.dp, Color(0xFFFF69B4), androidx.compose.foundation.shape.CutCornerShape(20.dp)))
                                }
                                "The Merchant’s Greed" -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                androidx.compose.ui.graphics.Brush.linearGradient(
                                                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFD4AF37).copy(alpha = 0.1f), Color(0xFFC8E6C9))
                                                )
                                            )
                                    )
                                    // Layered Gold/Emerald Frame
                                    Box(modifier = Modifier.fillMaxSize().border(6.dp, Color(0xFFD4AF37).copy(alpha = 0.6f))) // Outer Gold
                                    Box(modifier = Modifier.fillMaxSize().padding(6.dp).border(2.dp, Color(0xFF4CAF50))) // Emerald Line
                                    Box(modifier = Modifier.fillMaxSize().padding(12.dp).border(1.dp, Color(0xFFD4AF37))) // Inner Gold Line
                                }
                                "The Scholar’s Peak" -> {
                                    // Thin Blue Double-Border (Swapped Pattern)
                                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE3F2FD))) // Solid Light Blue
                                    Box(modifier = Modifier.fillMaxSize().border(4.dp, Color(0xFF2196F3).copy(alpha = 0.8f))) // Outer Blue Line
                                    Box(modifier = Modifier.fillMaxSize().padding(6.dp).border(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))) // Inner Blue Line
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxSize().padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TODAY'S LUCK",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 6.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            
                            Text(
                                text = fortune.name.uppercase(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 20.dp),
                                letterSpacing = 2.sp
                            )

                            Text(
                                text = "--- ${fortune.luckLevel.uppercase()} ---",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37).copy(alpha = 0.7f) else fortune.color.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                                letterSpacing = 1.sp
                            )
                            
                            Divider(
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color.copy(alpha = 0.3f), 
                                thickness = if (fortune.luckLevel == "Legendary") 2.dp else 1.dp,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            
                            Text(
                                text = fortune.description,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFE0E0E0) else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 30.dp),
                                lineHeight = 28.sp,
                                fontWeight = if (fortune.luckLevel == "Legendary") FontWeight.Medium else FontWeight.Normal
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).background(if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else Color(0xFF4CAF50)))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("PROS: ", fontWeight = FontWeight.Bold, color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else Color(0xFF4CAF50), fontSize = 12.sp)
                                    Text(fortune.pro, style = MaterialTheme.typography.bodyMedium, color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurface)
                                }
                                if (fortune.con != "-") {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).background(Color(0xFFF44336)))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("CONS: ", fontWeight = FontWeight.Bold, color = Color(0xFFF44336), fontSize = 12.sp)
                                        Text(fortune.con, style = MaterialTheme.typography.bodyMedium, color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Divider(color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37).copy(alpha = 0.2f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                            if (pullCount > 0) {
                                Text(
                                    text = "Pulled in $pullCount clicks",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37).copy(alpha = 0.8f) else fortune.color.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 12.dp),
                                    letterSpacing = 1.sp
                                )
                            }
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                            ) {
                                Text(
                                    text = if (isFavorited) "ADDED TO COLLECTION" else "ADD TO FAVORITES",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isFavorited) Color(0xFF4CAF50) else if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37).copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface,
                                    letterSpacing = 1.sp
                                )
                                IconButton(
                                    onClick = { 
                                        if (!isFavorited) {
                                            onFavoriteToggle(card)
                                            isFavorited = true
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (isFavorited) Color.Red else if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No card collected today.\nGo to Play and pull the sword!",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
