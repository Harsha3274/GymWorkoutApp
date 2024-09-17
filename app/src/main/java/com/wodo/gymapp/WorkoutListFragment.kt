package com.wodo.gymapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wodo.gymapp.model.Workout

class WorkoutListFragment : Fragment() {

    private lateinit var workoutLevel: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            workoutLevel = it.getString("WORKOUT_LEVEL") ?: "Beginner"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val workoutList = when (workoutLevel) {
            "Intermediate" -> getIntermediateWorkouts()
            "Advanced" -> getAdvancedWorkouts()
            else -> getBeginnerWorkouts()
        }

        val adapter = WorkoutsAdapter(requireContext(), workoutList)
        recyclerView.adapter = adapter
        return view
    }

    private fun getBeginnerWorkouts(): List<Workout> {
        return listOf(
            Workout("Chest Workout", R.drawable.chest_workout),
            Workout("Arms Workout", R.drawable.arms_workout),
            Workout("Cardio", R.drawable.cardio)
        )
    }

    private fun getIntermediateWorkouts(): List<Workout> {
        return listOf(
            Workout("Legs Workout", R.drawable.legworkouts),
            Workout("Back Workout", R.drawable.backworkout),
            Workout("Shoulders Workout", R.drawable.shoulderexercises)
        )
    }

    private fun getAdvancedWorkouts(): List<Workout> {
        return listOf(
            Workout("Full Body Workout", R.drawable.fullbody),
            Workout("Power Lifting", R.drawable.powerlifting),
            Workout("High Intensity", R.drawable.highintensity)
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(level: String) =
            WorkoutListFragment().apply {
                arguments = Bundle().apply {
                    putString("WORKOUT_LEVEL", level)
                }
            }

    }
}
