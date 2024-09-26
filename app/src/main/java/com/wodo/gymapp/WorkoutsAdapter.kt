package com.wodo.gymapp

import android.content.Context
import android.content.Intent
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

    private val expandedPositions = mutableMapOf<Int, Boolean>() // Store expanded positions

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.exercise_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.workoutName.text = workout.name
        holder.workoutImage.setImageResource(workout.imageResId)

        // Sample sub-workout list, ideally this data should come from your model or backend
        val subWorkoutList = listOf(
            Workout("Push up", R.drawable.pushups),
            Workout("Bench press", R.drawable.benchpress),
            Workout("Dumbbell fly", R.drawable.dumbbell_fly),
            Workout("Deadlifts", R.drawable.deadlifts),
            Workout("Bicep curls", R.drawable.bicep_curls)
        )

        val subWorkoutCount = subWorkoutList.size
        holder.workoutCount.text = "$subWorkoutCount Workouts"

        // Toggle dropdown visibility
        val isExpanded = expandedPositions[position] == true
        holder.dropdownRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.exerciseIcon.setImageResource(if (isExpanded) R.drawable.up_icon else R.drawable.forward_icon)

        // Setup sub-workout RecyclerView
        val subAdapter = SubWorkoutAdapter(context, subWorkoutList)
        holder.dropdownRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.dropdownRecyclerView.adapter = subAdapter

        // Handle workout card click (expand/collapse)
        holder.workoutCard.setOnClickListener {
            val currentlyExpanded = expandedPositions[position] ?: false
            expandedPositions[position] = !currentlyExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.exerciseName)
        val workoutImage: ImageView = itemView.findViewById(R.id.exerciseImage)
        val workoutCount: TextView = itemView.findViewById(R.id.exerciseCount)
        val workoutCard: CardView = itemView.findViewById(R.id.workoutCard)
        val dropdownRecyclerView: RecyclerView = itemView.findViewById(R.id.dropdownRecyclerView)
        val exerciseIcon: ImageView = itemView.findViewById(R.id.exerciseIcon)
    }
}
