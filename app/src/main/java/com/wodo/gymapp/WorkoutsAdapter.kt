package com.wodo.gymapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Workout

class WorkoutsAdapter(
    private val context: Context,
    private val workoutList: List<Workout>
) : RecyclerView.Adapter<WorkoutsAdapter.WorkoutViewHolder>() {

    // Store the index of the currently expanded card (-1 means no card is expanded)
    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.exercise_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.workoutName.text = workout.name
        holder.workoutImage.setImageResource(workout.imageResId)

        // Check if this card is expanded
        val isExpanded = position == expandedPosition
        holder.dropdownRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.exerciseIcon.setImageResource(if (isExpanded) R.drawable.up_icon else R.drawable.forward_icon)

        // Handle workout card click to toggle dropdown
        holder.workoutCard.setOnClickListener {
            if (expandedPosition == position) {
                // Collapse if already expanded
                expandedPosition = -1
                notifyItemChanged(position)
            } else {
                // Collapse the previously expanded card and expand the new one
                val previousExpandedPosition = expandedPosition
                expandedPosition = position
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(position)
            }
        }

        // Only populate the dropdown when the card is expanded
        if (isExpanded) {
            populateDropdown(holder.dropdownRecyclerView, workout.name)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    // Function to populate dropdown RecyclerView with related exercises for the selected workout
    private fun populateDropdown(dropdownRecyclerView: RecyclerView, workoutName: String) {
        val similarWorkouts = getSimilarWorkouts(workoutName)
        val adapter = SubWorkoutAdapter(context, similarWorkouts)
        dropdownRecyclerView.layoutManager = LinearLayoutManager(context)
        dropdownRecyclerView.adapter = adapter
    }

    // Mock function to get similar workouts for the dropdown
    private fun getSimilarWorkouts(workoutName: String): List<Workout> {
        return listOf(
            Workout("Chest Press", R.drawable.pushups),
            Workout("Chest Fly", R.drawable.dumbbell_fly),
            Workout("Incline Press", R.drawable.benchpress)
        )
    }

    // WorkoutViewHolder class
    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.exerciseName)
        val workoutImage: ImageView = itemView.findViewById(R.id.exerciseImage)
        val workoutCard: CardView = itemView.findViewById(R.id.workoutCard)
        val dropdownRecyclerView: RecyclerView = itemView.findViewById(R.id.dropdownRecyclerView)
        val exerciseIcon: ImageView = itemView.findViewById(R.id.exerciseIcon)
    }
}
