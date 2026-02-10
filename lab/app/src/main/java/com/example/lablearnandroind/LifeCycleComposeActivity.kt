package com.example.lablearnandroind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lablearnandroind.ui.theme.LabLearnAndroindTheme

class LifeCycleComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabLearnAndroindTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android2",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                var name by remember { mutableStateOf("") }
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    Column {

        Text(
            text = "Hello $name! say = "+inputText,
            modifier = modifier
        )

        TextField(
            value = inputText,
            onValueChange = {
                inputText = it
            }

        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    LabLearnAndroindTheme {
        Greeting2("Android")
    }
}
