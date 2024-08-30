package com.wodo.gymapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Workout

class WorkoutsList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val workoutList = listOf(
            Workout("Chest Workout", R.drawable.chest_workout),
            Workout("Arms Workout", R.drawable.arms_workout),
            Workout("Cardio", R.drawable.cardio),
            Workout("Legs Workout", R.drawable.legworkouts),
            Workout("Back Workout", R.drawable.backworkout),
            Workout("Shoulders Workout", R.drawable.shoulderexercises)
        )

        val adapter = WorkoutsAdapter(this, workoutList)
        recyclerView.adapter = adapter
    }
}
