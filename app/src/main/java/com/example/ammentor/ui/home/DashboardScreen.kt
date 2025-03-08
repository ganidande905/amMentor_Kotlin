package com.example.ammentor.ui.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ammentor.data.Task
import com.example.ammentor.models.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: TaskViewModel = viewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            Box(modifier = Modifier.height(95.dp)){
                CenterAlignedTopAppBar(
                    title = { Text("amMentor", style = TextStyle(fontSize = 45.sp, fontWeight = FontWeight.Bold,),) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        ) {
            LazyColumn {
                items(tasks) { task ->
                    TaskCard(task) { selectedTask = it }
                }
            }
        }
    }
    selectedTask?.let { task ->
        TaskCompletionDialog(task, onDismiss = { selectedTask = null }, viewModel)
    }
}

@Composable
fun TaskCard(task: Task, onClick: (Task) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(task) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)) // Black Theme
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = task.name,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700)) // Yellow
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.description,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Deadline: ${task.deadline} days",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Light, color = Color.Gray)
            )
        }
    }
}

// 🔥 Task Completion Popup Dialog
@Composable
fun TaskCompletionDialog(task: Task, onDismiss: () -> Unit, viewModel: TaskViewModel) {
    var completionDays by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Complete Task",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
            )
        },
        text = {
            Column {
                Text("How many days did you take to complete '${task.name}'?")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = completionDays,
                    onValueChange = { completionDays = it },
                    placeholder = { Text("Enter days") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val daysTaken = completionDays.text.toIntOrNull()
                    if (daysTaken != null) {
                        viewModel.updateLeaderboard(task, daysTaken)
                        Toast.makeText(context, "Leaderboard Updated!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Enter valid days!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text("Submit", color = Color.Black)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancel")
            }
        },
        containerColor = Color.Black
    )
}