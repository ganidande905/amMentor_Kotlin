package com.example.ammentor.data

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val completedTasks: List<Int> = emptyList(),
    val year: String = ""
)