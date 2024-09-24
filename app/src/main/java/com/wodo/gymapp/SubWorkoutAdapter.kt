package com.wodo.gymapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Workout

class SubWorkoutAdapter(
    private val context: Context,
    private val subWorkoutList: List<Workout>
) : RecyclerView.Adapter<SubWorkoutAdapter.SubWorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubWorkoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sub_exercise_item, parent, false)
        return SubWorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubWorkoutViewHolder, position: Int) {
        val subWorkout = subWorkoutList[position]
        holder.subWorkoutName.text = subWorkout.name
        holder.subWorkoutImage.setImageResource(subWorkout.imageResId)

        // Set click listener for each sub workout item
        holder.itemView.setOnClickListener {
            // Navigate to WorkoutDetailActivity when a sub workout is clicked
            val intent = Intent(context, WorkoutDetailActivity::class.java)
            intent.putExtra("WORKOUT_NAME", subWorkout.name)
            intent.putExtra("WORKOUT_IMAGE", subWorkout.imageResId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return subWorkoutList.size
    }

    // ViewHolder for sub-workout items
    class SubWorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subWorkoutName: TextView = itemView.findViewById(R.id.subExerciseName)
        val subWorkoutImage: ImageView = itemView.findViewById(R.id.subExerciseImage)
    }
}
