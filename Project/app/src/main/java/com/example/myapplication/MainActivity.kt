package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.foundation.border

import androidx.compose.ui.graphics.Color

data class CardFortune(
    val name: String,
    val luckLevel: String,
    val description: String,
    val pro: String,
    val con: String,
    val color: Color
)

fun getCardFortune(cardName: String): CardFortune {
    return when (cardName) {
        "Card 1" -> CardFortune(
            "The Absolute Destiny", "Legendary", 
            "วันนี้สวรรค์เข้าข้างคุณในทุกย่างก้าว ความสำเร็จรอคุณอยู่ทุกที่",
            "ดีเยี่ยมทุกด้าน (การงาน, การเงิน, ความรัก, สุขภาพ)", "-", Color(0xFFFFD700)
        )
        "Card 2" -> CardFortune(
            "The Gilded Heart", "Great Luck", 
            "ความรักเบ่งบานจนคนรอบข้างอิจฉา แต่อย่าเปย์คนรักจนหมดตัวล่ะ",
            "ความรักระดับสูงสุด", "การเงินรั่วไหลเล็กน้อย (เปย์หนัก)", Color(0xFFFF69B4)
        )
        "Card 3" -> CardFortune(
            "The Merchant’s Greed", "Great Luck", 
            "เงินทองไหลมาเทมาดั่งสายน้ำ แต่อาจจะมีลืมเศษเงินทอนบ้างนะ",
            "โชคลาภ/การเงินดีเยี่ยม", "อาจจะลืมเศษเงินทอนไว้ที่ร้านค้า", Color(0xFF4CAF50)
        )
        "Card 4" -> CardFortune(
            "The Scholar’s Peak", "Great Luck", 
            "สมองแจ่มใส ทำงานหรือสอบอะไรก็ผ่านฉลุย แต่ระวังลืมเวลานอน",
            "การงาน/การเรียนโดดเด่น", "สุขภาพ (ระวังพักผ่อนไม่เพียงพอ)", Color(0xFF2196F3)
        )
        "Card 5" -> CardFortune(
            "The Balanced Path", "Normal Luck", 
            "สุขภาพร่างกายแข็งแรงดีมาก แต่อาจมีเรื่องผิดใจกับคนรักเล็กน้อย",
            "สุขภาพดี/ร่างกายแข็งแรง", "ความรัก (มีเรื่องเข้าใจผิดเล็กๆ)", Color(0xFF8BC34A)
        )
        "Card 6" -> CardFortune(
            "The Social Butterfly", "Normal Luck", 
            "ไปไหนก็มีแต่คนยิ้มให้ เพื่อนฝูงห้อมล้อม แต่ระวังงานจะคั่งค้าง",
            "มนุษยสัมพันธ์/เพื่อนฝูงดี", "การงาน (มีความขี้เกียจครอบงำ)", Color(0xFF00BCD4)
        )
        "Card 7" -> CardFortune(
            "The Steady Hand", "Normal Luck", 
            "จิตใจสงบนิ่ง มีสมาธิดีเยี่ยม แต่อาจจะพลาดโอกาสลาภลอยไปบ้าง",
            "ความนิ่ง/การตัดสินใจดี", "โชคลาภ (ลาภลอยยังมาไม่ถึง)", Color(0xFF9E9E9E)
        )
        "Card 8" -> CardFortune(
            "The Broken Compass", "Bad Luck", 
            "วันนี้ทำอะไรก็ติดขัดไปหมด แต่ดวงเก็บเงินที่ตกตามพื้นดีเป็นพิเศษ",
            "ดวงดีเรื่อง: เก็บเศษเหรียญตามพื้น", "การงาน/การเรียน (ติดขัดขั้นสุด)", Color(0xFF795548)
        )
        "Card 9" -> CardFortune(
            "The Stormy Heart", "Bad Luck", 
            "ความรักดูหม่นหมองเหมือนฝนจะตก แต่ดวงเดาพยากรณ์อากาศแม่นมาก",
            "ดวงดีเรื่อง: ทายว่าฝนจะตกตอนไหน", "ความรัก (นก/เหงา/โดนเมิน)", Color(0xFF607D8B)
        )
        "Card 10" -> CardFortune(
            "The Empty Purse", "Bad Luck", 
            "กระเป๋าตังค์แห้งเหี่ยวหยิบอะไรก็พลาด แต่ดวงกดลุ้นของกินฟรีดีมาก",
            "ดวงดีเรื่อง: ได้ของกินแถม/น้ำฟรี", "การเงิน (กระเป๋ารั่ว/เสียเงินฟรี)", Color(0xFF37474F)
        )
        else -> CardFortune("Unknown", "N/A", "ไม่มีข้อมูล", "-", "-", Color.Gray)
    }
}

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

data class CollectionItem(val name: String, val date: String, val pullCount: Int)

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    var collectedCard by remember { mutableStateOf<String?>(null) }
    val collectionsList = remember { 
        androidx.compose.runtime.mutableStateListOf<CollectionItem>().apply {
            addAll(List(10) { index -> 
                CollectionItem("Card ${index + 1}", "Initial Collection", 0) 
            })
        }
    }
    var firstClickBonusChance by remember { mutableStateOf(0.0f) }
    var generalBonusChance by remember { mutableStateOf(0.0f) }
    var roundsWithoutCard1 by remember { mutableStateOf(0) }
    var pullId by remember { mutableStateOf(0) } // ใช้สำหรับรีเซ็ตหน้า CardsScreen
    var finalClickCount by remember { mutableStateOf(0) } // เพิ่มที่ระดับแอปเพื่อให้แชร์ข้ามหน้าได้
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
                CardsScreen(
                    card = collectedCard,
                    pullId = pullId,
                    pullCount = finalClickCount, // ส่งจำนวนครั้งที่กดไปแสดง
                    onFavoriteToggle = { cardName ->
                        val today = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
                        collectionsList.add(CollectionItem(cardName, today, finalClickCount))
                    }
                )
            }
            composable("play") {
                PlayScreen(
                    onCardCollected = { 
                        collectedCard = it 
                        pullId++ // เพิ่ม pullId ทุกครั้งที่ได้การ์ดใบใหม่
                    },
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
                    finalClickCount = finalClickCount,
                    onSetFinalCount = { finalClickCount = it },
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
    finalClickCount: Int = 0,
    onSetFinalCount: (Int) -> Unit = {},
    playSound: (Int) -> Unit = {},
    soundClinkId: Int = -1,
    soundSuccessId: Int = -1,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    
    val shakeOffset = remember { Animatable(0f) }
    val yOffset = remember { Animatable(0f) } // สำหรับทำให้ดาบลอยขึ้น
    var clickCount by remember { mutableStateOf(0) }
    
    // Infinite Mode (Default is False, meaning Daily Limit is ON)
    var isInfiniteMode by remember { mutableStateOf(false) }
    var hasPulledToday by remember { mutableStateOf(false) }

    // Gacha States
    var pulledCard by remember { mutableStateOf<String?>(null) }
    var showCardDialog by remember { mutableStateOf(false) }
    var isPullingSuccess by remember { mutableStateOf(false) }
    val cardList = List(10) { "Card ${it + 1}" }

    if (showCardDialog && pulledCard != null) {
        val fortune = getCardFortune(pulledCard!!)
        AlertDialog(
            onDismissRequest = { 
                showCardDialog = false
                coroutineScope.launch { 
                    yOffset.animateTo(0f)
                    isPullingSuccess = false
                }
            },
            containerColor = when (fortune.luckLevel) {
                "Legendary" -> Color(0xFF0A0A0A)
                "Great Luck" -> when (fortune.name) {
                    "The Gilded Heart" -> Color(0xFFFFEBEE)
                    "The Merchant’s Greed" -> Color(0xFFE8F5E9)
                    "The Scholar’s Peak" -> Color(0xFFE3F2FD)
                    else -> MaterialTheme.colorScheme.surface
                }
                else -> MaterialTheme.colorScheme.surface
            },
            title = if (fortune.luckLevel == "Normal Luck" || fortune.luckLevel == "Bad Luck") {
                { Text(text = "Sword Found!", fontWeight = FontWeight.Bold) }
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
                                Box(modifier = Modifier.matchParentSize().padding(12.dp).border(1.dp, Color(0xFFD4AF37))) // Inner GoldLine
                            }
                            "The Scholar’s Peak" -> {
                                Box(modifier = Modifier.matchParentSize().background(androidx.compose.ui.graphics.Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFE1F5FE)))))
                                Box(modifier = Modifier.matchParentSize().border(4.dp, Color(0xFF2196F3).copy(alpha = 0.2f)))
                                Box(modifier = Modifier.matchParentSize().padding(4.dp).border(2.dp, Color(0xFF2196F3)))
                                Box(modifier = Modifier.matchParentSize().padding(10.dp).border(2.dp, Color(0xFF2196F3).copy(alpha = 0.6f), androidx.compose.foundation.shape.CutCornerShape(10.dp)))
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
                                text = if (fortune.luckLevel == "Legendary") "CONGRATULATIONS!" else "SWORD FOUND!",
                                color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color.copy(alpha = 0.8f),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                        Text(
                            text = fortune.name.uppercase(),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 24.sp,
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
                            fontSize = 12.sp,
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
                        
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).background(if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else Color(0xFF4CAF50)))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PROS: ", fontWeight = FontWeight.Bold, color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else Color(0xFF4CAF50), fontSize = 12.sp)
                            Text(fortune.pro, style = MaterialTheme.typography.bodyMedium, color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface)
                        }

                        if (fortune.con != "-") {
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFFF44336)))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("CONS: ", fontWeight = FontWeight.Bold, color = Color(0xFFF44336), fontSize = 12.sp)
                                Text(fortune.con, style = MaterialTheme.typography.bodyMedium, color = if (fortune.luckLevel == "Legendary") Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        
                        Text(
                            text = "Got it in $finalClickCount pulls!",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37).copy(alpha = 0.6f) else fortune.color.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        if (fortune.luckLevel == "Legendary" || fortune.luckLevel == "Great Luck") {
                            Button(
                                onClick = { 
                                    onCardCollected(pulledCard!!)
                                    showCardDialog = false 
                                    coroutineScope.launch { 
                                        yOffset.animateTo(0f)
                                        isPullingSuccess = false
                                    }
                                },
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = if (fortune.luckLevel == "Legendary") Color(0xFFD4AF37) else fortune.color),
                                modifier = Modifier.padding(top = 16.dp).align(Alignment.End)
                            ) {
                                Text("COLLECT", color = if (fortune.luckLevel == "Legendary") Color.Black else Color.White)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (fortune.luckLevel == "Normal Luck" || fortune.luckLevel == "Bad Luck") {
                    Button(
                        onClick = { 
                            onCardCollected(pulledCard!!)
                            showCardDialog = false 
                            coroutineScope.launch { 
                                yOffset.animateTo(0f)
                                isPullingSuccess = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = fortune.color)
                    ) {
                        Text("Collect")
                    }
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
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
                        enabled = clickCount < 20 && !isPullingSuccess && (isInfiniteMode || !hasPulledToday) // ล็อกถ้าไม่ได้เปิดโหมด Infinite และดึงไปแล้วในวันนี้
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
                                
                                // สุ่ม Card ตามช่วงเวลาที่ดึงได้แบบแบ่งแยกชัดเจน
                                when {
                                    clickCount <= 7 -> cardList.subList(1, 4).random() // Card 2-4 (Great Luck)
                                    clickCount <= 15 -> cardList.subList(4, 7).random() // Card 5-7 (Normal Luck)
                                    else -> cardList.subList(7, 10).random() // Card 8-10 (Bad Luck)
                                }
                            }
                            
                            // ถ้าได้การ์ดช้า (เกิน 10 ครั้ง) รอบถัดไปจะสะสมแต้มบุญ +1% และ +3%
                            if (clickCount > 10) {
                                onUpdateBonus()
                            }

                            playSound(soundSuccessId) // เล่นเสียงตอนดึงสำเร็จ
                            onSetFinalCount(clickCount) // บันทึกจำนวนครั้งที่กดจนสำเร็จ
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
                                if (!isInfiniteMode) hasPulledToday = true // บันทึกว่าดึงไปแล้วถ้าอยู่ในโหมดปกติ (ไม่ใช่ Infinite)
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

    // Infinite Mode Toggle - ขวาสุดบนสุดของ Box
    Row(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Infinite", fontSize = 12.sp, color = Color.Yellow)
        Spacer(modifier = Modifier.width(4.dp))
        Switch(
            checked = isInfiniteMode,
            onCheckedChange = { 
                isInfiniteMode = it
                if (it) hasPulledToday = false
            },
            modifier = Modifier.scale(0.7f)
        )
    }
} // ปิด Box (outer)
} // ปิด PlayScreen)