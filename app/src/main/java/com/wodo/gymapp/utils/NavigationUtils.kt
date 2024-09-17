package com.wodo.gymapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wodo.gymapp.HomeActivity
import com.wodo.gymapp.R
import com.wodo.gymapp.ReviewsActivity
import com.wodo.gymapp.SettingsActivity
import com.wodo.gymapp.WorkoutsList

object NavigationUtils {

    // Function to setup bottom navigation and handle item selection
    fun setupBottomNavigation(activity: Activity, bottomNavigation: BottomNavigationView) {
        val gymEquipmentCount = getGymEquipmentCount(activity)
        setGymEquipmentBadge(bottomNavigation, gymEquipmentCount)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (activity !is WorkoutsList) {
                        val intent = Intent(activity, WorkoutsList::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        activity.startActivity(intent)
                    }
                    true
                }
                R.id.nav_gym_equipment -> {
                    if (activity !is HomeActivity) {
                        val intent = Intent(activity, HomeActivity::class.java)
                        activity.startActivity(intent)
                    }
                    true
                }
                R.id.nav_reviews -> {
                    if (activity !is ReviewsActivity) {
                        val intent = Intent(activity, ReviewsActivity::class.java)
                        activity.startActivity(intent)
                    }
                    true
                }
                R.id.nav_settings -> {
                    if (activity !is SettingsActivity) {
                        val intent = Intent(activity, SettingsActivity::class.java)
                        activity.startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }
    }

    // Function to get the gym equipment count from SharedPreferences
    private fun getGymEquipmentCount(context: Context): Int {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("GymAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("selectedEquipmentCount", 0)
    }

    // Function to set the badge with the count on the gym equipment tab
    private fun setGymEquipmentBadge(bottomNavigation: BottomNavigationView, count: Int) {
        val badge = bottomNavigation.getOrCreateBadge(R.id.nav_gym_equipment)
        badge.isVisible = count > 0 // Show the badge only if there's equipment selected
        badge.number = count // Set the count
    }
}
