package com.example.ammentor.firebase

import android.util.Log
import com.example.ammentor.data.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await



object FirebaseService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun fetchTasks(): List<Task> {
        return try {
            val snapshot = db.collection("Tasks").get().await()

            if (snapshot.isEmpty) {
                Log.e("FirebaseService", "No tasks found")
                return emptyList()
            }

            Log.d("FirebaseService", "Tasks Found: ${snapshot.documents.size}")

            snapshot.documents.map { doc ->
                Log.d("FirebaseService", "Task Data: ${doc.data}")

                Task(
                    id = doc.getLong("id")?.toInt() ?: 0,
                    name = doc.getString("name") ?: "No Name",
                    description = doc.getString("description") ?: "No Description",
                    deadline = doc.getLong("deadline") ?.toInt() ?: 0,

                )
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", " Error fetching tasks: ${e.message}")
            emptyList()
        }
    }
    fun registerUser(email: String, password: String, username: String, githubId: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@addOnCompleteListener
                    val user = hashMapOf(
                        "uid" to uid,
                        "username" to username,
                        "email" to email,
                        "githubId" to githubId,
                        "registeredAt" to System.currentTimeMillis(),
                        "completedTasks" to listOf<Int>()
                    )

                    FirebaseFirestore.getInstance().collection("users")
                        .document(uid)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("Register", "User data saved")
                        }
                        .addOnFailureListener {
                            Log.e("Register", "Error saving user", it)
                        }
                } else {
                    Log.e("Register", "Registration failed: ${task.exception?.message}")
                }
            }
    }
    fun fetchUserData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("username") ?: "Unknown"
                    val githubId = document.getString("githubId") ?: "No GitHub ID"
                    Log.d("UserData", "Welcome, $username ($githubId)")
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error fetching user data", it)
            }
    }

}
