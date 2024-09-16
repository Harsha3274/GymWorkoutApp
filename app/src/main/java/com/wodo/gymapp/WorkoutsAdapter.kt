package com.wodo.gymapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Workout

class WorkoutsAdapter(
    private val context: Context,
    private val workoutList: List<Workout>
) : RecyclerView.Adapter<WorkoutsAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.exercise_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.workoutName.text = workout.name
        holder.workoutImage.setImageResource(workout.imageResId)

        // Handle card click for full card
        holder.workoutCard.setOnClickListener {
            val intent = Intent(context, WorkoutDetailActivity::class.java).apply {
                putExtra("WORKOUT_NAME", workout.name)
                putExtra("WORKOUT_DESCRIPTION", "Description of ${workout.name} goes here...")
            }
            context.startActivity(intent)
        }

        // Handle dropdown icon click
        holder.dropdownIcon.setOnClickListener {
            // Implement dropdown action here (e.g., show options or details)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    // Move WorkoutViewHolder class inside the adapter
    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.exerciseName)  // TextView for workout name
        val workoutImage: ImageView = itemView.findViewById(R.id.exerciseImage)  // ImageView for workout image
        val workoutCard: CardView = itemView.findViewById(R.id.workoutCard)  // CardView for workout card
        val dropdownIcon: ImageView = itemView.findViewById(R.id.exerciseIcon)  // ImageView for dropdown icon
    }

}
