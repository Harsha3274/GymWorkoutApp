package com.wodo.gymapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wodo.gymapp.utils.NavigationUtils

class WorkoutDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Set up the bottom navigation using NavigationUtils
        NavigationUtils.setupBottomNavigation(this, bottomNavigation)
    }

    override fun onResume() {
        super.onResume()
        // Update the gym equipment badge dynamically in the bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNavigation)
    }
}
