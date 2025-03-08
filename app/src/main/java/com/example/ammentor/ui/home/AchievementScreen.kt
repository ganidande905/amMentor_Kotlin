package com.example.ammentor.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

data class Achievement(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val badge: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementScreen() {
    var achievements by remember { mutableStateOf<List<Achievement>>(emptyList()) }

    // Fetch Achievements from Firestore
    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("achievements")
            .get()
            .addOnSuccessListener { result ->
                achievements = result.documents.mapNotNull { it.toObject(Achievement::class.java) }
            }
            .addOnFailureListener { e ->
                println("Error fetching achievements: ${e.message}")
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Achievements", fontSize = 22.sp, color = Color.White) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
        ) {
            LazyColumn {
                items(achievements) { achievement ->
                    AchievementCard(achievement)
                }
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = achievement.title, fontSize = 20.sp, color = Color(0xFFFFD700))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = achievement.description, fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Badge: ${achievement.badge}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}