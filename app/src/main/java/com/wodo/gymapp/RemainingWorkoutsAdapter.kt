package com.wodo.gymapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Workout

class RemainingWorkoutsAdapter(
    private val context: Context,
    private val remainingWorkouts: List<Workout>
) : RecyclerView.Adapter<RemainingWorkoutsAdapter.RemainingWorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemainingWorkoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_workout, parent, false)
        return RemainingWorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: RemainingWorkoutViewHolder, position: Int) {
        val workout = remainingWorkouts[position]
        holder.workoutName.text = workout.name
        holder.workoutImage.setImageResource(workout.imageResId)

        holder.itemView.setOnClickListener {
            // Add functionality if needed to handle remaining workout clicks
        }
    }

    override fun getItemCount(): Int {
        return remainingWorkouts.size
    }

    class RemainingWorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.workoutTitle)
        val workoutImage: ImageView = itemView.findViewById(R.id.workoutImage)
    }
}
