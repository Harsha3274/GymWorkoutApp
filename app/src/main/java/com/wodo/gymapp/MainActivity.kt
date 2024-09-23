package com.wodo.gymapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.wodo.gymapp.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val splashProgressBar = findViewById<ProgressBar>(R.id.splashProgressBar)
        val forwardButton = findViewById<ImageView>(R.id.forwardButton)

        // Initially hide the ProgressBar
        splashProgressBar.visibility = View.GONE

        // Set onClickListener for the forward button
        forwardButton.setOnClickListener {
            // Show ProgressBar only when the forward button is clicked (i.e., simulating delay)
            splashProgressBar.visibility = View.VISIBLE

            // Simulate a delay (e.g., loading time) before navigating to the next activity
            Handler(Looper.getMainLooper()).postDelayed({
                // Navigate to LoginActivity when forward button is clicked
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Close MainActivity so the user can't return to it

                // Hide ProgressBar after transition
                splashProgressBar.visibility = View.GONE
            }, 1000) // Delay of 1 second for the progress simulation
        }
    }
}
