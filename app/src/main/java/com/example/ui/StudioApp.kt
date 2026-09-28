package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.R
import com.example.data.Quest
import com.example.logic.ClockEngine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioApp(viewModel: StudioViewModel) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = currentRoute == "dashboard",
                    onClick = { navController.navigate("dashboard") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Surfing, contentDescription = "Ocean") },
                    label = { Text("Ocean") },
                    selected = currentRoute == "ocean",
                    onClick = { navController.navigate("ocean") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MusicNote, contentDescription = "Music") },
                    label = { Text("Music") },
                    selected = currentRoute == "music",
                    onClick = { navController.navigate("music") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Layers, contentDescription = "AR Story") },
                    label = { Text("AR Story") },
                    selected = currentRoute == "ar_story",
                    onClick = { navController.navigate("ar_story") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Mentor") },
                    label = { Text("Mentor") },
                    selected = currentRoute == "mentor",
                    onClick = { navController.navigate("mentor") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") { DashboardScreen(viewModel, navController) }
            composable("ocean") { OceanSessionsScreen(viewModel) }
            composable("music") { MusicPlayerScreen(viewModel) }
            composable("ar_story") { ARStoryScreen(viewModel) }
            composable("mentor") { MentorScreen(viewModel) }
            composable("vault") { VaultScreen(viewModel) }
        }
    }
}

@Composable
fun DashboardScreen(viewModel: StudioViewModel, navController: NavController) {
    val fluidTime by viewModel.fluidTime.collectAsStateWithLifecycle()
    val quests by viewModel.quests.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Box(modifier = Modifier.height(240.dp).fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner_1790601902518),
                    contentDescription = "Port Waikato Hero",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        "West Coast Node",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Fluid Motion Time: ${if (fluidTime.isApex) fluidTime.apexName else "H${fluidTime.hour} M${fluidTime.minute}"}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        item {
            SectionHeader("Active Quests")
        }

        items(quests.filter { !it.isCompleted }) { quest ->
            QuestItem(quest) { viewModel.completeQuest(quest) }
        }

        item {
            SectionHeader("Stoke Level")
            StokeLevelCard()
        }
    }
}

@Composable
fun OceanSessionsScreen(viewModel: StudioViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ocean Sessions", style = MaterialTheme.typography.headlineMedium)
        Text("Port Waikato Coastal Node", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(64.dp))
                Text("Camera Active: Extracting Vectors...", modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Start extraction */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Start Vector Capture")
        }
    }
}

@Composable
fun MusicPlayerScreen(viewModel: StudioViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sovereign Music Studio", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        BeatCard("Dub / Reggae", "75–85 BPM", Icons.Default.MusicNote)
        BeatCard("Trip-Hop", "85–100 BPM", Icons.Default.Audiotrack)
        BeatCard("Funky House", "120–125 BPM", Icons.Default.FlashOn)
        
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { /* Logic */ }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate New Beat")
        }
    }
}

@Composable
fun ARStoryScreen(viewModel: StudioViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("AR Story Builder", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column {
                Text("Overlay Configuration", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Community Void Opacity: 20%")
                Slider(value = 0.2f, onValueChange = {}, enabled = false)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Build Story */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Launch AR Session")
        }
    }
}

@Composable
fun MentorScreen(viewModel: StudioViewModel) {
    var message by remember { mutableStateOf("") }
    val chatHistory by viewModel.mentorChat.collectAsStateWithLifecycle()
    val isThinking by viewModel.isMentorThinking.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (chatHistory.isEmpty()) {
                Text(
                    "Stoke level high! I'm your Creative Mentor. Want to lay down a beat or turn a wave into art?",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(chatHistory) { (role, text) ->
                        ChatMessage(role, text)
                    }
                    if (isThinking) {
                        item {
                            Text("Mentor is paddling back out...", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("What vibe are you feeling?") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = {
                    if (message.isNotBlank() && !isThinking) {
                        viewModel.askMentor(message)
                        message = ""
                    }
                },
                shape = CircleShape,
                containerColor = if (isThinking) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun VaultScreen(viewModel: StudioViewModel) {
    val memory by viewModel.memory.collectAsStateWithLifecycle()
    val ledgers by viewModel.ledgers.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item { Text("West Coast Node Vault", style = MaterialTheme.typography.headlineMedium) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        item { Text("Artist Ledgers", style = MaterialTheme.typography.titleLarge) }
        items(ledgers) { ledger ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ledger.artistName, fontWeight = FontWeight.Bold)
                        Text("Harmony Notes: ${ledger.harmonyNotes}")
                    }
                    Text("Shavings: ${ledger.shavings}", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Text("System Memory", style = MaterialTheme.typography.titleLarge) }
        items(memory) { mem ->
            Text("${mem.key}: ${mem.value}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun QuestItem(quest: Quest, onComplete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(quest.title, fontWeight = FontWeight.Bold)
                Text(quest.description, style = MaterialTheme.typography.bodyMedium)
                Text("+${quest.rewardNotes} Harmony Notes", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
            Checkbox(checked = quest.isCompleted, onCheckedChange = { if (it) onComplete() })
        }
    }
}

@Composable
fun StokeLevelCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Stoke level is high today!", fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator(
                progress = { 0.85f },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}

@Composable
fun BeatCard(title: String, bpm: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(bpm)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* Play/Generate */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }
    }
}

@Composable
fun ChatMessage(role: String, text: String) {
    val isUser = role == "User"
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text,
                modifier = Modifier.padding(12.dp),
                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
