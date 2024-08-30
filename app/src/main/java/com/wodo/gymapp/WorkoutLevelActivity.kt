package com.wodo.gymapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView

class WorkoutLevelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_level)

        val selectedEquipment = intent.getStringArrayListExtra("SELECTED_EQUIPMENT")
        val titleTextView: TextView = findViewById(R.id.equipmentTitle)
        titleTextView.text = "Selected: ${selectedEquipment?.joinToString(", ")}"

        findViewById<CardView>(R.id.beginnerCard).setOnClickListener {
            navigateToWorkout("Beginner")
        }

        findViewById<CardView>(R.id.intermediateCard).setOnClickListener {
            navigateToWorkout("Intermediate")
        }

        findViewById<CardView>(R.id.advancedCard).setOnClickListener {
            navigateToWorkout("Advanced")
        }
    }

    private fun navigateToWorkout(level: String) {
        // Start WorkoutsList activity and pass the selected level as an extra
        val intent = Intent(this, WorkoutsList::class.java)
        intent.putExtra("WORKOUT_LEVEL", level)
        startActivity(intent)
    }

    }
