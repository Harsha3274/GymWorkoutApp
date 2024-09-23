package com.wodo.gymapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wodo.gymapp.utils.NavigationUtils

class WorkoutDetailActivity : AppCompatActivity() {
//Should use recycler view for wokrout description
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val backButton = findViewById<ImageView>(R.id.backButton)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        NavigationUtils.setupBottomNavigation(this, bottomNavigation)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.exercise) // Change with actual video
        videoView.setVideoURI(videoUri)

        // Back button listener
        backButton.setOnClickListener {
            val intent = Intent(this, WorkoutsList::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Update the gym equipment badge dynamically in the bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNavigation)
    }
}
