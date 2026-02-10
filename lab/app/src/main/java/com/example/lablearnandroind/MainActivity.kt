package com.example.lablearnandroind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "profile",
                        modifier = Modifier
                            .size(500.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(32.dp)
                            .clickable {
                                startActivity(Intent(this@MainActivity,PokedexActivity::class.java))
                            }

                    )
                    var str by remember { mutableStateOf(8) }
                    var agi by remember { mutableStateOf(8) }
                    var int by remember { mutableStateOf(8) }
                    //status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {

                        Column {
                            Button(onClick = { str++ }) {
                                Image(
                                    painter = painterResource(R.drawable.baseline_arrow_upward_24)
                                    , contentDescription = "up"
                                )

                            }
                            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text =  "Str", fontSize = 20.sp)
                            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text =  str.toString(), fontSize = 20.sp)
                            Button(onClick = { str-- }) {
                                Image(
                                    painter = painterResource(R.drawable.outline_arrow_downward_24)
                                    , contentDescription = "down"
                                )

                            }
                        }
                        Column {
                            Button(onClick = { agi++ }) {
                                Image(

                                    painter = painterResource(R.drawable.baseline_arrow_upward_24)
                                    , contentDescription = "up"
                                )

                            }
                            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text =  "Agi", fontSize = 20.sp)
                            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text =  agi.toString(), fontSize = 20.sp)
                            Button(onClick = { agi-- }) {
                                Image(
                                    painter = painterResource(R.drawable.outline_arrow_downward_24)
                                    , contentDescription = "down"
                                )

                            }
                        }
                        Column {
                            Button(onClick = { int++ }) {
                                Image(
                                    painter = painterResource(R.drawable.baseline_arrow_upward_24)
                                    , contentDescription = "up"
                                )

                            }
                            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text =  "Int", fontSize = 20.sp)
                            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text =  int.toString(), fontSize = 20.sp)
                            Button(onClick = { int-- }) {
                                Image(
                                    painter = painterResource(R.drawable.outline_arrow_downward_24)
                                    , contentDescription = "down"
                                )

                            }
                        }
                    }
                }
            }
        }

    override fun onStart() {
        super.onStart()
        Log.i("Lifecycle", "MainActivity : onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Lifecycle", "MainActivity : onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Lifecycle", "MainActivity : onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Lifecycle", "MainActivity : onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Lifecycle", "MainActivity : onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("Lifecycle", "MainActivity : onRestart")
    }
    }





