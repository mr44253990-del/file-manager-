package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.SettingsRepository
import com.google.accompanist.permissions.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsScreen(onComplete: () -> Unit, settingsRepository: SettingsRepository) {
    val coroutineScope = rememberCoroutineScope()
    
    val permissionsToRequest = mutableListOf(
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_IMAGES
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(permissionsToRequest)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "প্রয়োজনীয় অনুমতি", // Required Permissions in Bengali
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "অ্যাপের কাজ করার জন্য নিচের অনুমতিগুলো দিন",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            PermissionCard(title = "Storage", desc = "লোকাল ফোল্ডার ও ফাইল এক্সেস করতে")
            PermissionCard(title = "Audio", desc = "অডিও প্লেয়ার হিসেবে কাজ করতে")
            PermissionCard(title = "Media", desc = "ভিডিও ও ছবি দেখতে")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionCard(title = "Notification", desc = "প্লেয়ার কন্ট্রোল নোটিফিকেশন দেখাতে")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (multiplePermissionsState.allPermissionsGranted) {
                        coroutineScope.launch {
                            settingsRepository.setOnboardingCompleted(true)
                            onComplete()
                        }
                    } else {
                        multiplePermissionsState.launchMultiplePermissionRequest()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = if (multiplePermissionsState.allPermissionsGranted) "চালিয়ে যান" else "সব অনুমতি দিন",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    
    // Automatically proceed if permissions granted already
    LaunchedEffect(multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {
            settingsRepository.setOnboardingCompleted(true)
            onComplete()
        }
    }
}

@Composable
fun PermissionCard(title: String, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder
            Box(
                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primary.copy(alpha=0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(title.take(1), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = desc, color = Color.Gray, fontSize = 12.sp)
            }
            Button(
                onClick = { /* individual req logic usually handled by global or multiple */ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha=0.2f)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text("অনুমতি দিন", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp)
            }
        }
    }
}
