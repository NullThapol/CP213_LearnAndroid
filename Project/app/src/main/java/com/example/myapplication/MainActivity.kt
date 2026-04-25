package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import android.media.SoundPool
import android.media.AudioAttributes
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

data class CollectionItem(val name: String, val date: String)

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    var collectedCard by remember { mutableStateOf<String?>(null) }
    val collectionsList = remember { androidx.compose.runtime.mutableStateListOf<CollectionItem>() }
    var firstClickBonusChance by remember { mutableStateOf(0.0f) }
    var generalBonusChance by remember { mutableStateOf(0.0f) }
    var roundsWithoutCard1 by remember { mutableStateOf(0) }
    val context = LocalContext.current

    // --- ระบบ Sound Effects (SFX) ระดับแอป ---
    val soundPool = remember {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        SoundPool.Builder().setMaxStreams(5).setAudioAttributes(attributes).build()
    }
    
    val soundClinkId = remember { 
        val id = context.resources.getIdentifier("sword_clink", "raw", context.packageName)
        if (id != 0) soundPool.load(context, id, 1) else -1
    }
    val soundSuccessId = remember { 
        val id = context.resources.getIdentifier("pull_success", "raw", context.packageName)
        if (id != 0) soundPool.load(context, id, 1) else -1
    }

    fun playSound(soundId: Int) {
        if (soundId != -1) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }
    // ------------------------------------

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val navItemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                NavigationBarItem(
                    icon = { Text("Today card") },
                    selected = currentRoute == "cards",
                    colors = navItemColors,
                    onClick = {
                        navController.navigate("cards") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Text("Play") },
                    selected = currentRoute == "play",
                    colors = navItemColors,
                    onClick = {
                        navController.navigate("play") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Text("Collections") },
                    selected = currentRoute == "collections",
                    colors = navItemColors,
                    onClick = {
                        navController.navigate("collections") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "play",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("cards") {
                val isCardInCollection = collectionsList.any { it.name == collectedCard }
                CardsScreen(
                    card = collectedCard,
                    isFavoritedInitially = isCardInCollection,
                    onFavoriteToggle = { cardName ->
                        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
                        val existingItem = collectionsList.find { it.name == cardName }
                        if (existingItem != null) {
                            collectionsList.remove(existingItem)
                        } else {
                            collectionsList.add(CollectionItem(cardName, today))
                        }
                    }
                )
            }
            composable("play") {
                PlayScreen(
                    onCardCollected = { collectedCard = it },
                    firstClickBonus = firstClickBonusChance,
                    generalBonus = generalBonusChance,
                    roundsWithoutCard1 = roundsWithoutCard1,
                    onUpdateBonus = { 
                        firstClickBonusChance = (firstClickBonusChance + 0.01f).coerceAtMost(1.0f) 
                        generalBonusChance = (generalBonusChance + 0.03f).coerceAtMost(1.0f)
                    },
                    onResetFirstBonus = { 
                        firstClickBonusChance = 0.0f 
                        roundsWithoutCard1 = 0 // รีเซ็ตตัวนับการันตีเมื่อได้ Card 1
                    },
                    onResetGeneralBonus = { generalBonusChance = 0.0f },
                    onIncrementRounds = { roundsWithoutCard1++ },
                    playSound = ::playSound,
                    soundClinkId = soundClinkId,
                    soundSuccessId = soundSuccessId
                )
            }
            composable("collections") {
                CollectionsScreen(items = collectionsList)
            }
        }
    }
}

@Composable
fun PlayScreen(
    onCardCollected: (String) -> Unit,
    firstClickBonus: Float = 0f,
    generalBonus: Float = 0f,
    roundsWithoutCard1: Int = 0,
    onUpdateBonus: () -> Unit = {},
    onResetFirstBonus: () -> Unit = {},
    onResetGeneralBonus: () -> Unit = {},
    onIncrementRounds: () -> Unit = {},
    playSound: (Int) -> Unit = {},
    soundClinkId: Int = -1,
    soundSuccessId: Int = -1,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    
    val shakeOffset = remember { Animatable(0f) }
    val yOffset = remember { Animatable(0f) } // สำหรับทำให้ดาบลอยขึ้น
    var clickCount by remember { mutableStateOf(0) }
    
    // Once a Day Mode
    var onceADayEnabled by remember { mutableStateOf(false) }
    var hasPulledToday by remember { mutableStateOf(false) }

    // Gacha States
    var pulledCard by remember { mutableStateOf<String?>(null) }
    var showCardDialog by remember { mutableStateOf(false) }
    var isPullingSuccess by remember { mutableStateOf(false) } // ล็อกการกดทันทีที่สำเร็จ
    val cardList = List(10) { "Card ${it + 1}" }

    if (showCardDialog && pulledCard != null) {
        AlertDialog(
            onDismissRequest = { 
                showCardDialog = false
                // รีเซ็ตตำแหน่งดาบและปลดล็อกเมื่อกลับลงมา
                coroutineScope.launch { 
                    yOffset.animateTo(0f)
                    isPullingSuccess = false
                }
            },
            title = { Text("You Found a Legendary Sword!") },
            text = { 
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = pulledCard!!,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                    Text("It was embedded deep in the stone!")
                }
            },
            confirmButton = {
                Button(onClick = { 
                    onCardCollected(pulledCard!!) // ส่งการ์ดที่ได้กลับไป
                    showCardDialog = false 
                    // รีเซ็ตตำแหน่งดาบและปลดล็อกเมื่อกดเก็บ
                    coroutineScope.launch { 
                        yOffset.animateTo(0f)
                        isPullingSuccess = false
                    }
                }) {
                    Text("Collect")
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
        ) {
        // Logo Area with piercing sword
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .height(140.dp),
            contentAlignment = Alignment.Center
        ) {
            // ดาบแนวนอนที่เสียบโลโก้
            Image(
                painter = painterResource(id = R.drawable.sword),
                contentDescription = "Horizontal Sword",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .rotate(90f) // หมุนดาบเป็นแนวนอน
                    .offset(y = (-5).dp) // ปรับตำแหน่งเล็กน้อยให้ดูพอดี
            )
            
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Sword Of Destiny Logo",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(100.dp),
                alignment = Alignment.Center
            )
        }



        // แสดงจำนวนการกด
        Text(
            text = "Pull Count: $clickCount",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // วางดาบไว้ตรงกลางและเลื่อนลงมาให้ปักในหิน
            Image(
                painter = painterResource(id = R.drawable.sword),
                contentDescription = "Sword",
                modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .align(Alignment.Center)
                    .offset(x = shakeOffset.value.dp, y = yOffset.value.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // ลบเอฟเฟกต์การกดสีเทาออก
                        enabled = clickCount < 20 && !isPullingSuccess && (!onceADayEnabled || !hasPulledToday) // ล็อกถ้าโหมดวันละครั้งเปิดอยู่และดึงไปแล้ว
                    ) {
                        clickCount++
                        
                        // --- ปรับปรุงเรทให้สมดุล (Smooth & Fair Balance) ---
                        val currentChance = when {
                            // ถ้าติด Hard Pity (รอบที่ 7) ให้กดทีเดียวออก 100%
                            roundsWithoutCard1 >= 6 && clickCount == 1 -> 1.0f
                            
                            clickCount == 1 -> 0.02f + firstClickBonus // พื้นฐาน 2% สำหรับ Card 1
                            clickCount >= 20 -> 1.0f // การันตี 100%
                            clickCount < 10 -> 0.07f + generalBonus // ครั้งที่ 2-9 โอกาส 7%
                            else -> {
                                // ครั้งที่ 10-19: เริ่ม 15% และ +10% ทุกๆ 3 ครั้ง (Soft Pity)
                                0.15f + generalBonus + ((clickCount - 10) / 3) * 0.10f
                            }
                        }
                        
                        // สุ่มการ์ด
                        if (kotlin.random.Random.nextFloat() < currentChance) {
                            // ระบบเลือกการ์ด: 
                            // เช็ค Hard Pity (ถ้าครบ 6 รอบแล้วยังไม่ได้ Card 1 รอบที่ 7 จะการันตี)
                            val nextCard = if (roundsWithoutCard1 >= 6 || clickCount == 1) {
                                onResetFirstBonus() // รีเซ็ตโบนัสเป็น 0% และรีเซ็ตตัวนับรอบ (Pity Reset)
                                cardList[0] // มอบ Card 1
                            } else {
                                // ไม่ได้ Card 1 ในรอบนี้ -> เพิ่มตัวนับการันตีรอบถัดไป
                                onIncrementRounds()
                                onResetGeneralBonus() // รีเซ็ตโบนัสทั่วไป (เพราะดึงสำเร็จแล้ว)
                                
                                // สุ่ม Card 2-10 ตามช่วงเวลาที่ดึงได้
                                when {
                                    clickCount <= 7 -> cardList.subList(1, 4).random() // Card 2-4
                                    clickCount <= 15 -> cardList.subList(1, 8).random() // Card 2-8
                                    else -> cardList.subList(7, 10).random() // Card 8-10
                                }
                            }
                            
                            // ถ้าได้การ์ดช้า (เกิน 10 ครั้ง) รอบถัดไปจะสะสมแต้มบุญ +1% และ +3%
                            if (clickCount > 10) {
                                onUpdateBonus()
                            }

                            playSound(soundSuccessId) // เล่นเสียงตอนดึงสำเร็จ
                            isPullingSuccess = true // ล็อกการกดทันที
                            clickCount = 0 // รีเซ็ตการนับเมื่อได้การ์ด (Pity Reset)
                            
                            // ทำให้ดาบลอยขึ้นก่อนแล้วค่อยโชว์การ์ด
                            coroutineScope.launch {
                                yOffset.animateTo(
                                    targetValue = -350f, // ลอยสูงขึ้นกว่าเดิม
                                    animationSpec = tween(durationMillis = 800)
                                )
                                pulledCard = nextCard
                                showCardDialog = true
                                if (onceADayEnabled) hasPulledToday = true // บันทึกว่าดึงไปแล้วในโหมดรายวัน
                            }
                        }

                        coroutineScope.launch {
                            playSound(soundClinkId) // เล่นเสียงตอนกดดาบ (ดาบสั่น)
                            // ทำการสั่น (ไป-กลับ) - ลดระยะการสั่นเหลือ 5f
                            repeat(3) {
                                shakeOffset.animateTo(
                                    targetValue = 5f,
                                    animationSpec = tween(durationMillis = 40)
                                )
                                shakeOffset.animateTo(
                                    targetValue = -5f,
                                    animationSpec = tween(durationMillis = 40)
                                )
                            }
                            shakeOffset.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 40)
                            )
                        }
                    }
            )

        } // ปิด Box (inner) สำหรับดาบ
    } // ปิด Column

    // วางหินไว้ด้านล่างสุดของ Box (ทับดาบส่วนล่าง)
    Image(
        painter = painterResource(id = R.drawable.rock),
        contentDescription = "Rock",
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .align(Alignment.BottomCenter)
    )

    // Once a Day Toggle - ขวาสุดบนสุดของ Box
    Row(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Daily", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.width(4.dp))
        Switch(
            checked = onceADayEnabled,
            onCheckedChange = { 
                onceADayEnabled = it
                if (!it) hasPulledToday = false
            },
            modifier = Modifier.scale(0.7f)
        )
    }
} // ปิด Box (outer)
} // ปิด PlayScreen