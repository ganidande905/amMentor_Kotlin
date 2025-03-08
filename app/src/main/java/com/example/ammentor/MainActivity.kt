package com.example.ammentor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ammentor.nav.AppNavigation
import com.example.ammentor.data.MentorTrackerTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
val db = FirebaseFirestore.getInstance()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MentorTrackerTheme {
                AppNavigation()
            }
        }
    }
}