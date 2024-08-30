package com.wodo.gymapp

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.VideoView
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity

class WorkoutDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        // Retrieve workout name, description, and video from intent
        val workoutName = intent.getStringExtra("WORKOUT_NAME") ?: "Workout Detail"
        val workoutDescription = intent.getStringExtra("WORKOUT_DESCRIPTION") ?: "Description not available."
        val workoutVideoResId = intent.getIntExtra("WORKOUT_VIDEO", R.raw.sample_video)

        // Set workout name
        findViewById<TextView>(R.id.workoutDetailTitle).text = workoutName

        // Set workout description
        findViewById<TextView>(R.id.workoutDescription).text = workoutDescription

        // Setup the video view with media controller
        val workoutVideo = findViewById<VideoView>(R.id.workoutVideo)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(workoutVideo)
        workoutVideo.setMediaController(mediaController)

        // Load video from resource
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + workoutVideoResId)
        workoutVideo.setVideoURI(videoUri)

        // Start video playback
        workoutVideo.start()
    }
}
