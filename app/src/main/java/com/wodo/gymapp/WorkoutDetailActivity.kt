package com.wodo.gymapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wodo.gymapp.model.Workout
import com.wodo.gymapp.utils.NavigationUtils

class WorkoutDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val backButton = findViewById<ImageView>(R.id.backButton)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val remainingWorkouts = intent.getParcelableArrayListExtra<Workout>("REMAINING_WORKOUTS")

        // Set up video player (sample video)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.exercise) // Replace with actual video
        videoView.setVideoURI(videoUri)


        // Set up the bottom navigation using NavigationUtils
        NavigationUtils.setupBottomNavigation(this, bottomNavigation)

        // Start playing the video automatically
        videoView.start()

        // Set up Back button
        backButton.setOnClickListener {
            val intent = Intent(this, WorkoutsList::class.java)
            startActivity(intent)
        }

        // Display remaining workouts if available
        remainingWorkouts?.let {
            setupRemainingWorkoutsRecyclerView(it)
        }
    }

    private fun setupRemainingWorkoutsRecyclerView(remainingWorkouts: ArrayList<Workout>) {
        val recyclerView = findViewById<RecyclerView>(R.id.nextWorkoutsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = RemainingWorkoutsAdapter(this, remainingWorkouts)
        recyclerView.adapter = adapter

        // Force layout to ensure all items are visible
        recyclerView.requestLayout()
    }

    override fun onResume() {
        super.onResume()
        // Update the gym equipment badge dynamically in the bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNavigation)
    }

}
