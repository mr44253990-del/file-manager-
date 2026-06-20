package com.example

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*

@Composable
fun AppNavigation(settingsRepository: SettingsRepository) {
    val navController = rememberNavController()
    val onboardingCompleted by settingsRepository.isOnboardingCompleted.collectAsState(initial = null)

    if (onboardingCompleted == null) {
        // Still loading preference
        return
    }

    val startDestination = if (onboardingCompleted == true) "main" else "splash"

    NavHost(navController = navController, startDestination = startDestination, modifier = Modifier.fillMaxSize()) {
        composable("splash") {
            SplashScreen(onSplashComplete = {
                navController.navigate("permissions") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        
        composable("permissions") {
            PermissionsScreen(onComplete = {
                navController.navigate("main") {
                    popUpTo("permissions") { inclusive = true }
                }
            }, settingsRepository = settingsRepository)
        }
        
        composable("main") {
            MainScreen(
                onNavigateToAbout = { navController.navigate("about") },
                onNavigateToAudioPlayer = { navController.navigate("audio_player") },
                onNavigateToVideoPlayer = { navController.navigate("video_player") }
            )
        }
        
        composable("about") {
            AboutDeveloperScreen(onBack = { navController.popBackStack() })
        }
        
        composable("audio_player") {
            AudioPlayerScreen(onBack = { navController.popBackStack() })
        }
        
        composable("video_player") {
            VideoPlayerScreen(onBack = { navController.popBackStack() })
        }
    }
}
