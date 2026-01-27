package com.example.lablearnandroind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.lablearnandroind.ui.theme.LabLearnAndroindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                Column(modifier = Modifier.fillMaxSize()
                    .background(color = Color.Gray)
                    .height(32.dp)
                    .padding(32.dp)) {
                    //hp
                    Box(modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(color = Color.Red)
                    )
                    {
                        Text(
                            text = "HP",
                            modifier = Modifier.align(alignment = Alignment.CenterStart)
                                .fillMaxWidth(fraction =  0.62f)
                                .background(color = Color.Green  )
                                .padding(8.dp)
                        )
                    }

                    //image

                    //status
                }
            }
        }
    }

