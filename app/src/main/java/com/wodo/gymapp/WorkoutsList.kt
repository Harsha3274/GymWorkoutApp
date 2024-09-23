package com.wodo.gymapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.wodo.gymapp.login.LoginActivity
import com.wodo.gymapp.utils.NavigationUtils

class WorkoutsList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts_list)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Set up the bottom navigation using NavigationUtils
        NavigationUtils.setupBottomNavigation(this, bottomNavigation)

        // Fetch username from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "User") ?: "User"

        // Display the username in the TextView
        val userGreetingTextView = findViewById<TextView>(R.id.userGreeting)
        userGreetingTextView.text = "Hi,\n$username"

        // Add margin between tabs
        addMarginsToTabs(tabLayout)

        // Setup ViewPager with TabLayout
        setupViewPagerAndTabs(viewPager, tabLayout)

    }

    // Function to add margins between tabs in TabLayout
    private fun addMarginsToTabs(tabLayout: TabLayout) {
        // Run after layout is rendered
        tabLayout.post {
            for (i in 0 until tabLayout.tabCount) {
                val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i) as LinearLayout
                val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(50, 20, 28, 0) // Add margins
                tab.layoutParams = layoutParams
                tab.requestLayout()
            }
        }
    }

    // Function to setup ViewPager with TabLayout
    private fun setupViewPagerAndTabs(viewPager: ViewPager2, tabLayout: TabLayout) {
        val adapter = WorkoutPagerAdapter(this)
        viewPager.adapter = adapter

        // Attach TabLayout with ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Beginner"
                1 -> "Intermediate"
                2 -> "Advanced"
                else -> "Unknown"
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        // Update the gym equipment badge dynamically in the bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNavigation)
    }

}
