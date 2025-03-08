

package com.example.ammentor.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ammentor.R
import com.example.ammentor.data.LeaderboardEntry
import com.example.ammentor.data.leaderboardData
import com.google.firebase.firestore.FirebaseFirestore


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LeaderboardScreen() {
//    var leaderboard by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }
//    var selectedTab by remember { mutableStateOf(0) }
//    var selectedTask by remember { mutableStateOf(1) }
//
//
//    LaunchedEffect(Unit) {
//        FirebaseFirestore.getInstance().collection("leaderboard")
//            .get()
//            .addOnSuccessListener { result ->
//                leaderboard = result.documents.mapNotNull { it.toObject(LeaderboardEntry::class.java) }
//                    .sortedByDescending { it.progress }
//            }
//            .addOnFailureListener { e ->
//                println("Error fetching leaderboard: ${e.message}")
//            }
//    }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Leaderboard", fontSize = 22.sp, color = Color.White) }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
//        ) {
//            // Tabs for Switching Leaderboards
//            TabRow(
//                selectedTabIndex = selectedTab,
//                containerColor = Color(0xFF121212),
//            ) {
//                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) { Text("Overall") }
//                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) { Text("Task Specific") }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if (selectedTab == 1) {
//                TaskSelector(selectedTask) { selectedTask = it }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            LazyColumn {
//                itemsIndexed(leaderboard) { index, user ->
//                    LeaderboardCard(user, index + 1, user.progress)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TaskSelector(selectedTask: Int, onTaskSelected: (Int) -> Unit) {
//    var expanded by remember { mutableStateOf(false) }
//    val tasks = (1..9).toList()
//
//    Box(
//        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { expanded = true }
//    ) {
//        Text(text = "Task $selectedTask", fontSize = 16.sp, color = Color.White)
//
//        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//            tasks.forEach { task ->
//                DropdownMenuItem(
//                    text = { Text("Task $task") },
//                    onClick = {
//                        onTaskSelected(task)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun LeaderboardCard(user: LeaderboardEntry, rank: Int, progress: Float) {
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(8.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = "#$rank", fontSize = 20.sp, color = Color(0xFFFFD700), modifier = Modifier.width(40.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = user.name, fontSize = 16.sp, color = Color.White)
//                Text(text = user.badge, fontSize = 12.sp, color = Color.Gray)
//            }
//            Text(text = "${(progress * 100).toInt()}%", fontSize = 16.sp, color = Color.White)
//        }
//    }
//}
//

//package com.example.ammentor.ui




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedTask by remember { mutableStateOf(1) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Leaderboard",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom=5.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF121212),
                modifier = Modifier.clip(RoundedCornerShape(15.dp))

            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Overall") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Task Specific") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (selectedTab == 1) {
                TaskSelector(selectedTask) { selectedTask = it }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Show Leaderboard
            if (selectedTab == 0) {
                FullLeaderboard(leaderboardData)
            } else {
                TaskLeaderboard(leaderboardData, selectedTask)
            }
        }
    }
}
@Composable
fun TaskSelector(selectedTask: Int, onTaskSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val tasks = (1..9).toList()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212), RoundedCornerShape(12.dp))
            .clickable { expanded = true }
    ) {
        Text(
            text = "Task $selectedTask",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.Black)
        ) {
            tasks.forEach { task ->
                DropdownMenuItem(
                    text = { Text("Task $task", color = Color.White) },
                    onClick = {
                        onTaskSelected(task)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Composable
fun FullLeaderboard(users: List<LeaderboardEntry>) {
    LazyColumn {
        itemsIndexed(users.sortedByDescending { it.progress }) { index, user ->
            LeaderboardCard(user, index + 1, user.progress)
        }
    }
}
@Composable
fun TaskLeaderboard(users: List<LeaderboardEntry>, taskIndex: Int) {
    val sortedUsers = users
        .filter { taskIndex - 1 < it.taskProgress.size }
        .sortedByDescending { it.taskProgress.getOrElse(taskIndex - 1) { 0f } }

    LazyColumn {
        itemsIndexed(sortedUsers) { index, user ->
            LeaderboardCard(user, index + 1, user.taskProgress[taskIndex - 1])
        }
    }
}
@Composable
fun LeaderboardCard(user: LeaderboardEntry, rank: Int, progress: Float) {
    val profileImage = painterResource(id = R.drawable.profile_placeholder)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$rank",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                modifier = Modifier.width(40.dp)
            )

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Image(
                    painter = profileImage,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = user.badge,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}