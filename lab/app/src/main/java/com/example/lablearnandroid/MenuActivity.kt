package com.example.lablearnandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import com.example.lablearnandroid.architecture.mvi.MviCounterActivity
import com.example.lablearnandroid.architecture.mvvm.MvvmCounterActivity

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold { paddingValues ->
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    
                    Button(onClick = { startWithAnim(SensorActivity::class.java, R.anim.slide_in_right, R.anim.slide_out_left) }) {
                        Text("SensorActivity (Slide Right)")
                    }
                    
                    Button(onClick = { startWithAnim(GalleryActivity::class.java, R.anim.slide_in_left, R.anim.slide_out_right) }) {
                        Text("GalleryActivity (Slide Left)")
                    }
                    
                    Button(onClick = { startWithAnim(RPGCardActivity::class.java, R.anim.slide_in_up, R.anim.stay) }) {
                        Text("RPGCardActivity (Slide Up)")
                    }
                    
                    Button(onClick = { startWithAnim(PokedexActivity::class.java, R.anim.zoom_in, R.anim.stay) }) {
                        Text("PokedexActivity (Zoom In)")
                    }
                    
                    Button(onClick = { startWithAnim(LifeCycleComposeActivity::class.java, R.anim.fade_in, R.anim.fade_out) }) {
                        Text("LifeCycleComposeActivity (Fade In)")
                    }
                    
                    Button(onClick = { startWithAnim(MviCounterActivity::class.java, R.anim.slide_in_right, R.anim.slide_out_left) }) {
                        Text("MviCounterActivity (Slide Right)")
                    }
                    
                    Button(onClick = { startWithAnim(MvvmCounterActivity::class.java, R.anim.slide_in_left, R.anim.slide_out_right) }) {
                        Text("MvvmCounterActivity (Slide Left)")
                    }
                    
                    Button(onClick = { startWithAnim(SharedPreferencesActivity::class.java, R.anim.slide_in_up, R.anim.stay) }) {
                        Text("SharedPreferencesActivity (Slide Up)")
                    }
                    
                    Button(onClick = { startWithAnim(Part1AnimationActivity::class.java, R.anim.zoom_in, R.anim.stay) }) {
                        Text("Part 1: AnimationActivity (Zoom In)")
                    }
                    
                    Button(onClick = { startWithAnim(Part2Activity::class.java, R.anim.fade_in, R.anim.fade_out) }) {
                        Text("Part 2: Contact List (Fade In)")
                    }
                    
                    Button(onClick = { startWithAnim(Part3DonutChartActivity::class.java, R.anim.slide_in_right, R.anim.slide_out_left) }) {
                        Text("Part 3: Donut Chart (Slide Right)")
                    }
                    
                    Button(onClick = { startWithAnim(Part4SwipeDismissActivity::class.java, R.anim.slide_in_left, R.anim.slide_out_right) }) {
                        Text("Part 4: Swipe to Dismiss (Slide Left)")
                    }
                    
                    Button(onClick = { startWithAnim(Part5SideEffectsActivity::class.java, R.anim.slide_in_up, R.anim.stay) }) {
                        Text("Part 5: Compose Side Effects (Slide Up)")
                    }
                    
                    Button(onClick = { startWithAnim(Part6WebViewActivity::class.java, R.anim.zoom_in, R.anim.stay) }) {
                        Text("Part 6: View Interoperability (Zoom In)")
                    }
                    
                    Button(onClick = { startWithAnim(Part7MainActivity::class.java, R.anim.fade_in, R.anim.fade_out) }) {
                        Text("Part 7: Activity Transitions (Fade In)")
                    }
                    
                    Button(onClick = { startWithAnim(Part8AdaptiveLayoutActivity::class.java, R.anim.slide_in_up, R.anim.stay) }) {
                        Text("Part 8: Adaptive Layouts (Slide Up)")
                    }
                }
            }
        }
    }

    // ฟังชันลัดสำหรับเรียก startActivity พร้อมกับแนบ Animation Options แบบที่เราต้องการ
    private fun <T> startWithAnim(activityClass: Class<T>, enterAnim: Int, exitAnim: Int) {
        val intent = Intent(this, activityClass)
        // สร้าง Bundle ที่ระบุให้หน้าปัจจุบัน (Exit) และหน้าใหม่ (Enter) ขยับยังไง
        val options = ActivityOptionsCompat.makeCustomAnimation(this, enterAnim, exitAnim)
        startActivity(intent, options.toBundle())
    }
}
