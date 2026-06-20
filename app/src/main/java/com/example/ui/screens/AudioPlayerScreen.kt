package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("এখন চলছে", color = Color.White, fontSize=16.sp) }, // "Now Playing"
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = "More", tint=Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.size(240.dp).clip(CircleShape).background(Color.DarkGray))
            Spacer(modifier = Modifier.height(32.dp))
            Text("Moner Manush", fontSize = 24.sp, fontWeight = FontWeight.Bold, color=Color.White)
            Text("Arijit Singh", fontSize = 16.sp, color=Color.Gray)
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Progress Bar simulation
            Slider(value = 0.3f, onValueChange = {}, colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary, activeTrackColor = MaterialTheme.colorScheme.primary))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("01:45", color = Color.Gray, fontSize = 12.sp)
                Text("04:20", color = Color.Gray, fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Controls
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal=16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Like", tint=Color.White)
                Icon(Icons.Default.SkipPrevious, contentDescription = "Prev", tint=Color.White, modifier = Modifier.size(40.dp))
                FloatingActionButton(onClick = {}, containerColor=MaterialTheme.colorScheme.primary) { // Pause
                    Icon(Icons.Default.Pause, contentDescription = "Pause", tint=Color.White, modifier = Modifier.size(36.dp))
                }
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint=Color.White, modifier = Modifier.size(40.dp))
                Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint=Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
