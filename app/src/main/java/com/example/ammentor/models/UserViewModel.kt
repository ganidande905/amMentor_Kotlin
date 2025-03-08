package com.example.ammentor.models
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ammentor.db
import com.example.ammentor.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    init {
        fetchUserData()
    }
    private fun fetchUserData() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    _user.value = user
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching user data: ${e.message}")
            }
    }
    fun updateYear(selectedYear: String) {
        val currentUser = auth.currentUser ?: return
        db.collection("Users").document(currentUser.uid)
            .update("year", selectedYear)
            .addOnSuccessListener {
                Log.d("ProfileViewModel", "Year updated to Firestore: $selectedYear")
                _user.value = _user.value?.copy(year = selectedYear)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", " Failed to update year: ${exception.message}")
            }
    }
}