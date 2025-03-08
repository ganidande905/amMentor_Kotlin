package com.example.ammentor.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ammentor.data.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("Tasks").get().await()
                val taskList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Task::class.java)
                }
                _tasks.value = taskList
            } catch (e: Exception) {
                println("Error fetching tasks: ${e.message}")
            }
        }
    }
    fun updateLeaderboard(task: Task, daysTaken: Int) {
        val db = FirebaseFirestore.getInstance()
        val userId = "0"

        val taskRef = db.collection("Leaderboard").document(userId)

        taskRef.get().addOnSuccessListener { doc ->
            val currentPoints = doc.getLong("points")?.toInt() ?: 0
            val newPoints = if (daysTaken <= task.deadline) {
                currentPoints + 10
            } else {
                currentPoints + 5
            }

            taskRef.set(mapOf("points" to newPoints), SetOptions.merge())
        }
    }
}
