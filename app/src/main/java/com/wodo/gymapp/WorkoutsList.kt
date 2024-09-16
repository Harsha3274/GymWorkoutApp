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

class WorkoutsList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts_list)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val userProfile = findViewById<ImageView>(R.id.userProfile)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Fetch username from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "User")  // Default to "User"

        // Display the username in the TextView
        val userGreetingTextView = findViewById<TextView>(R.id.userGreeting)
        userGreetingTextView.text = "Hi, $username"

        // Setup ViewPager with the adapter
        setupViewPagerAndTabs(viewPager, tabLayout)

        // Add margin between tabs after setup
        addMarginsToTabs(tabLayout)

        // Setup bottom navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_gym_equipment -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_reviews -> {
                    val intent = Intent(this, ReviewsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewPagerAndTabs(viewPager: ViewPager2, tabLayout: TabLayout) {
        val adapter = WorkoutPagerAdapter(this)
        viewPager.adapter = adapter

        // Set up the TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Beginner"
                1 -> "Intermediate"
                2 -> "Advanced"
                else -> ""
            }
        }.attach()
    }

    private fun addMarginsToTabs(tabLayout: TabLayout) {
        // Delay the execution to make sure the tabs are laid out before modifying
        tabLayout.post {
            for (i in 0 until tabLayout.tabCount) {
                val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i) as LinearLayout
                val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(50, 20, 48, 0) // Set left-right margins (adjust as necessary)
                tab.layoutParams = layoutParams
                tab.requestLayout()
            }
        }
    }
}