package com.example.lablearnandroid.architecture.mvc

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lablearnandroid.R

class CounterClassicActivity : AppCompatActivity() {

    private lateinit var tvCount: TextView
    private lateinit var btnIncrement: Button

    // Model
    private val model = MvcCounterModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_classic)

        // View
        tvCount = findViewById(R.id.tvCount)
        btnIncrement = findViewById(R.id.btnIncrement)

        // Controller Logic
        btnIncrement.setOnClickListener {
            model.incrementCounter()
            val newCount = model.getCount()
            updateView(newCount)
        }
    }

    private fun updateView(count: Int) {
        tvCount.text = "Count: $count"
    }
}
