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

        holder.workoutCard.setOnClickListener {
            // Prepare an intent to navigate to WorkoutDetailActivity
            val intent = Intent(context, WorkoutDetailActivity::class.java).apply {
                putExtra("WORKOUT_NAME", workout.name)
                putExtra("WORKOUT_DESCRIPTION", "Description of ${workout.name} goes here...")
                putExtra("WORKOUT_VIDEO", R.raw.sample_video)  // Assuming you have a video for each workout
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.exerciseName)
        val workoutImage: ImageView = itemView.findViewById(R.id.exerciseImage)
        val workoutCard: CardView = itemView.findViewById(R.id.workoutCard)
    }
}
