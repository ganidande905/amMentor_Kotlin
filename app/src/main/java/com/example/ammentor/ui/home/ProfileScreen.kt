package com.example.ammentor.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ammentor.models.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()
    val githubProfileUrl = "https://github.com/${user?.username}.png"
    var expanded by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(user?.year ?: "Select Year") }
    val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = Color.Gray
        ) {
            Image(
                painter = rememberAsyncImagePainter(githubProfileUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "GitHub: ${user?.username ?: "Unknown"}",
            fontSize = 18.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box {
            Button(
                onClick = { expanded = true }
            ) {
                Text(selectedYear)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                years.forEach { year ->
                    DropdownMenuItem(
                        text = { Text(year) },
                        onClick = {
                            selectedYear = year
                            expanded = false
                            viewModel.updateYear(year)
                        }
                    )
                }
            }
        }
    }
}