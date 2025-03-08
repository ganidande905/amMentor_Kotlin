package com.example.ammentor.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color

import com.example.ammentor.ui.auth.LoginScreen
import com.example.ammentor.ui.auth.RegisterScreen
import com.example.ammentor.ui.home.AchievementScreen
import com.example.ammentor.ui.home.DashboardScreen
import com.example.ammentor.ui.home.LeaderboardScreen
import com.example.ammentor.ui.home.ProfileScreen

@Composable
fun AppNavigation() {
    var selectedScreen by remember { mutableStateOf("login") }
    var isAuthenticated by remember { mutableStateOf(false) }
    val showBottomNav = isAuthenticated && selectedScreen !in listOf("login", "register")
    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(selectedScreen) { selectedScreen = it }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                "login" -> LoginScreen(
                    onLoginSuccess = {
                        isAuthenticated = true
                        selectedScreen = "dashboard"
                    },
                    onRegisterClick = { selectedScreen = "register" }
                )
                "register" -> RegisterScreen(
                    onRegisterSuccess = {
                        isAuthenticated = true
                        selectedScreen = "login"
                    },
                    onLoginClick = { selectedScreen = "login" }
                )
                else -> {
                    if (isAuthenticated) {
                        when (selectedScreen) {
                            "dashboard" -> DashboardScreen()
                            "achievements" -> AchievementScreen()
                            "leaderboard" -> LeaderboardScreen()
                            "profile" -> ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun BottomNavigationBar(selectedScreen: String, onScreenSelected: (String) -> Unit) {
    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
            selected = selectedScreen == "dashboard",
            onClick = { onScreenSelected("dashboard") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard", tint = if (selectedScreen == "dashboard") Color.Yellow else Color.White) },
        )
        NavigationBarItem(
            selected = selectedScreen == "achievements",
            onClick = { onScreenSelected("achievements") },
            icon = { Icon(Icons.Default.Star, contentDescription = "Achievements", tint = if (selectedScreen == "achievements") Color.Yellow else Color.White) },
        )
        NavigationBarItem(
            selected = selectedScreen == "leaderboard",
            onClick = { onScreenSelected("leaderboard") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Leaderboard", tint = if (selectedScreen == "leaderboard") Color.Yellow else Color.White) },
        )
        NavigationBarItem(
            selected = selectedScreen == "profile",
            onClick = { onScreenSelected("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = if (selectedScreen == "profile") Color.Yellow else Color.White) }
        )
    }
}