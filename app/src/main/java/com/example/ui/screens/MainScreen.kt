package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToAbout: () -> Unit,
    onNavigateToAudioPlayer: () -> Unit,
    onNavigateToVideoPlayer: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomNavController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                DrawerContent(
                    onClose = { scope.launch { drawerState.close() } },
                    onNavigateToAbout = {
                        scope.launch { drawerState.close() }
                        onNavigateToAbout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Rakib", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(" Media Hub", color = Color.White)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Crown icon maybe premium? */ }) {
                            Icon(Icons.Default.Star, contentDescription = "Premium", tint = Color.Yellow)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            bottomBar = {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White
                ) {
                    val items = listOf(
                        BottomNavItem("gallery", "Gallery", Icons.Default.Image),
                        BottomNavItem("videos", "Videos", Icons.Default.PlayArrow),
                        BottomNavItem("audio", "Audio", Icons.Default.MusicNote),
                        BottomNavItem("files", "Files", Icons.Default.Folder)
                    )

                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                bottomNavController.navigate(item.route) {
                                    popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha=0.2f)
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = bottomNavController,
                startDestination = "gallery",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("gallery") { GalleryScreen() }
                composable("videos") { VideosScreen(onVideoClick = onNavigateToVideoPlayer) }
                composable("audio") { AudioScreen(onAudioClick = onNavigateToAudioPlayer) }
                composable("files") { FilesScreen() }
            }
        }
    }
}

data class BottomNavItem(val route: String, val title: String, val icon: ImageVector)

@Composable
fun DrawerContent(onClose: () -> Unit, onNavigateToAbout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                // Example user image logic
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("রাকিবুল", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Developer", fontSize = 12.sp, color = Color.Gray)
                Text("Kapilmuni, Bangladesh", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Divider(color = Color.DarkGray)

        // Storage info
        DrawerItem(icon = Icons.Default.Storage, title = "ইন্টারনাল স্টোরেজ", onClick = {})
        Row(modifier = Modifier.padding(horizontal = 48.dp, vertical = 4.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("ব্যবহৃত স্টোরেজ", color = Color.Gray, fontSize = 12.sp)
            Text("64.53 GB", color = Color.White, fontSize = 12.sp)
        }
        Row(modifier = Modifier.padding(horizontal = 48.dp, vertical = 4.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("খালি স্টোরেজ", color = Color.Gray, fontSize = 12.sp)
            Text("63.47 GB", color = Color.White, fontSize = 12.sp)
        }

        Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))

        DrawerItem(icon = Icons.Default.Settings, title = "অ্যাপ সেটিংস", onClick = {})
        DrawerItem(icon = Icons.Default.Palette, title = "থিম সেটিংস", onClick = {})
        DrawerItem(icon = Icons.Default.Info, title = "আমাদের সম্পর্কে", onClick = onNavigateToAbout)
        DrawerItem(icon = Icons.Default.Share, title = "শেয়ার অ্যাপ", onClick = {})
        DrawerItem(icon = Icons.Default.Star, title = "রেট দিন", onClick = {})
    }
}

@Composable
fun DrawerItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = Color.White, fontSize = 16.sp)
    }
}
