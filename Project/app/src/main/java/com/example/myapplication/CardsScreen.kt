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

@Composable
fun CardsScreen(
    card: String?, 
    isFavoritedInitially: Boolean,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorited by remember(card, isFavoritedInitially) { mutableStateOf(isFavoritedInitially) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (card != null) {
            val fortune = getCardFortune(card)
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth(0.9f).height(480.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    shadowElevation = 12.dp,
                    border = androidx.compose.foundation.BorderStroke(2.dp, fortune.color.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TODAY'S LUCK",
                            style = MaterialTheme.typography.labelLarge,
                            color = fortune.color,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        
                        Text(
                            text = fortune.name,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = fortune.color,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        Text(
                            text = "[ ${fortune.luckLevel} ]",
                            fontWeight = FontWeight.Bold,
                            color = fortune.color.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Divider(color = fortune.color.copy(alpha = 0.3f), thickness = 1.dp)
                        
                        Text(
                            text = fortune.description,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 24.dp),
                            lineHeight = 24.sp
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("Pros: ", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                                Text(fortune.pro, style = MaterialTheme.typography.bodyMedium)
                            }
                            if (fortune.con != "-") {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text("Cons: ", fontWeight = FontWeight.Bold, color = Color(0xFFF44336))
                                    Text(fortune.con, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        
                        // ปุ่ม Favorite ด้านใน Card
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
                            Text("Add to Favorites", style = MaterialTheme.typography.bodySmall)
                            IconButton(
                                onClick = { 
                                    onFavoriteToggle(card)
                                    isFavorited = !isFavorited
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorited) Color.Red else fortune.color,
                                    modifier = Modifier.size(32.dp)
                                )
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
